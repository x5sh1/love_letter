class lover_letter implements rules{
	// current player
	public int cPlayer = 0;
	private player[] clients;
	private Thread[] pool;
	private boolean gameStatus = false;
	private int player_limit = 4;
	private int numPlayer = 0;
	//game start
	public boolean gameStart = true;
	//player status
	private final int regularS = 1;
	private final int readyS = 2;
	private final int defeatedS = 0;
	private final int immuneS = 3;
	//cards 
	private card[] cards;
	//the number of cards
	private final int cardNum = 16;
	//current round cards
	private int roundCard = 1;
	//collaborator number
	private final int guard = 1;
	private final int priest = 2;
	private final int baron = 3;
	private final int handmaiden = 4;
	private final int prince = 5;
	private final int king = 6;
	private final int countess = 7;
	private final int princess = 8;
	private int currentPlayer = (int)(Math.random() * player_limit - 1);
	private int target_scroe = 3;
	private int targetS_selector = 0;
	public lover_letter(player[] clients, Thread[] pool){
		this.pool = pool;
		this.clients = clients;
		cards = new card[cardNum];
		createCards();
	}

	// generate cards
	public void createCards(){
		//generate cards
		int ide = 10;
		String cCode = "";
		String description = "";
		String cName = "";
		int strength = 0;
		for(int index = 0; index < cardNum; index++){
			cards[index] = new card("","",0,"");
			if(index < 5){
				cCode = guard + "_" + ide++;
				description = "You can choose any player to guess card, if you are right, the target player lose, else nothing happen.";
				cName = "guard";
				strength = guard;
			}
			if(index == 4){
				ide = 20;
			}
			if(index > 4 && index < 7){
				cCode = priest + "_" + ide++;
				description = "You can choose any player to see card.";
				cName = "priest";
				strength = priest;
			}
			if(index == 6){
				ide = 30;
			}
			if(index > 6 && index < 9){
				cCode = baron + "_" + ide++;
				description = "You can choose any player to compare cards' strength, player with lower strength lose.";
				cName = "baron";
				strength = baron;
			}
			if(index == 8){
				ide = 40;
			}
			if(index > 8 && index < 11){
				cCode = handmaiden + "_" + ide++;
				description = "You will not be effected unitl your next turn.";
				cName = "handmaiden";
				strength = handmaiden;
			}
			if(index == 10){
				ide = 50;
			}
			if(index > 10 && index < 13){
				cCode = prince + "_" + ide++;
				description = "You can choose any player to discard his or her card and he or she will draw a new card.";
				cName = "prince";
				strength = prince;
			}
			if(index == 13){
				cCode = king + "_" + 60;
				description = "You can choose any two players to exchange their cards.";
				cName = "king";
				strength = king;
			}
			if(index == 14){
				cCode = countess + "_" + 70;
				description = "You must discard this card when you have 'King' and 'Princess'.";
				cName = "countess";
				strength = countess;
			}
			if(index == 15){
				cCode = princess + "_" + 80;
				description = "You must protect this card, try not to discard this card.";
				cName = "princess";
				strength = princess;
			}
			cards[index].setCode(cCode);
			cards[index].setName(cName);
			cards[index].setStrength(strength);
			cards[index].setDesc(description);
		}
	}

	// process commands
	public void commandHandler(player sender, String message){
		String[] commands = message.split("/");
		int numCom = commands.length;
		for(int index = 0; index < numCom; index++){
			String command = commands[index];
			String[] listCom = command.split(":");
			switch(listCom[0]){
				case "USRNAME":
					userNameH(sender, listCom[1]);
					break;
				case "READY":
					readyH(listCom[1]);
					break;
				case "REGULAR":
					regularH(listCom[1]);
					break;
				case "QUIT":
					quitH(listCom[1]);
					break;
				case "SCORE":
					scoreH(listCom[1]);
					break;
				case "SKILL":
					skillH(listCom[1]);
				default:
					break;
			}
		}
	}

	// broadcast to all clients.
	public void broadcast(String msg){
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null && !clients[index].getUsername().equals("")){
				clients[index].sendMsg(msg);
			}
		}
	}


	// handle command 'EXIST'
	public void existH(player sender){
		String msg = "EXIST:";
		sender.sendMsg(msg);
	}

	// handle command 'WEL'
	public void welH(player sender){
		String msg = "WEL:";
		cPlayer++;
		sender.sendMsg(msg);
	}

	// handle command 'username'.
	public void userNameH(player sender, String content){
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null && content.equals(clients[index].getUsername())){
				existH(sender);
				return;
			}
		}
		// set username for the player.
		sender.setUsername(content);
		// welcome
		welH(sender);
		// use USRCODE command
		usrcodeH();
	}

	// handle command 'ready'.
	public synchronized void readyH(String content){
		int usrid = Integer.parseInt(content);
		clients[usrid].setStatus(readyS);
		String message = "READY:" + usrid;
		broadcast(message);
		boolean result = true;
		numPlayer = 0;
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null){
				result = result && clients[index].getStatus() == 2;
				numPlayer++;
			}
		}
		result = (result && (numPlayer > 1));
		gameStatus = result;
		if(gameStatus){
			gameStart = false;
			askScoreH();
		}
	}


	// 'card' command
	public synchronized void cardH(){
		if(roundCard == 16){
			strengthH();
		}else{
			if(currentPlayer == 4){
				currentPlayer = 0;
			}
			while(clients[currentPlayer] == null || clients[currentPlayer].getStatus() == defeatedS){
				currentPlayer++;
				if(currentPlayer == 4){
					currentPlayer = 0;
				}
			}
			card distri = cards[roundCard];
			String message = "CARD:" + distri.getCode() + "-" + distri.getName() + "-" + distri.getStrength() + "-" + distri.getDesc();
			if(clients[currentPlayer].getStatus() == immuneS){
				clients[currentPlayer].setStatus(regularS);
				regularH(currentPlayer + "");
			}
			clients[currentPlayer].sendMsg(message);
			clients[currentPlayer].drawCard(distri);
			distri.setStatus();
			turnH(currentPlayer);
			roundCard++;
			currentPlayer++;
		}
	}


	// shuffle cards and discard one card randomly.
	public synchronized void shuffle(){
		for(int i = 0; i < cardNum; i++){
			cards[i].reset();
			int rand = (int)(Math.random()*cardNum);
			card temp = cards[i];
			cards[i] = cards[rand];
			cards[rand] = temp;
		}
		cards[0].setStatus();
	}


	// handle command 'quit'.
	public synchronized void quitH(String content){
		int usrid = Integer.parseInt(content);
		clients[usrid] = null;
		pool[usrid] = null;
		String message = "QUIT:" + usrid;
		cPlayer--;
		broadcast(message);
	}

	// handle command 'USRCODE'
	public synchronized void usrcodeH(){
		String command = "USRCODE:";
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null && clients[index].getUsername() != null){
				command += clients[index].getUsername() + "-" + clients[index].getUserid() + "-" + clients[index].getStatus() + "-" + clients[index].getScore() + "&";
			}
		}
		
		broadcast(command.substring(0,command.length() - 1));
	}

	// handle command 'START'
	public synchronized void startH(){
		// game starts.
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null){
				clients[index].setStatus(regularS);
			}
		}
		String message  = "START:" + target_scroe;
		broadcast(message);
	}

	// handle command 'STRENGTH'
	public synchronized void strengthH(){
		String message = "STRENGTH:";
		int tempSt = 0;
		int tempU = 0;
		String t = "";
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null && (clients[index].getStatus() == 1 || clients[index].getStatus() == 3) ){
				t += "&" + clients[index].getUserid() + "-" + clients[index].getStrength();
				if(clients[index].getStrength() > tempSt){
					tempSt = clients[index].getStrength();
					tempU = index;
				}
			}
		}
		message += t.substring(1, t.length());
		broadcast(message);
		winnerH(tempU);
	}

	// handle command 'SCORE'
	public synchronized void scoreH(String content){
		int temp = Integer.parseInt(content);
		target_scroe = temp;
		startH();
		shuffle();
		for(int i = 0; i < numPlayer + 1; i++){
			cardH();
		}
	}


	// handle command 'SCORE?'
	public synchronized void askScoreH(){
		String message  = "SCORE?:";
		int pc = 0;
		for(player p:clients){
			if(p != null){
				pc++;
			}
		}
		targetS_selector = (int)(Math.random() * pc);
		broadcast(message + targetS_selector);
	}

	// handle command 'REGULAR':
	public synchronized void regularH(String msg){
		int temp = Integer.parseInt(msg);
		clients[temp].setStatus(regularS);
		String message = "REGULAR:" + temp;
		broadcast(message);
	}
	
	// handle command 'TURN'
	public synchronized void turnH(int userId){
		String msg = "TURN:" + userId;
		broadcast(msg);
	}

	// handle command 'DEFEATED'
	public synchronized void defeadH(int userId){
		String msg = "DEFEATED:"+userId;
		clients[userId].setStatus(defeatedS);
		broadcast(msg);
	}

	// handle commadn 'IMMUNE'
	public synchronized void immuneH(int userId){
		String msg = "IMMUNE:" + userId;
		clients[userId].setStatus(immuneS);
		broadcast(msg);
	}


	// handle command 'RESPONSE'
	public synchronized void responseH(int senderCode, String content){
		String msg = "RESPONSE:" + content;
		if(content.equals("")){
			msg = "RESPONSE:none";
		}
		clients[senderCode].sendMsg(msg);
	}

	// handle command 'EFFECT'
	public synchronized void effectH(String content){
		String msg = "EFFECT:" + content;
		broadcast(msg);
	}


	// handle command 'SKILL'
	public synchronized void skillH(String msg){
		String[] parameters = msg.split("-");
		int senderCode = Integer.parseInt(parameters[0]);
		String cardCode = parameters[1];
		clients[senderCode].discard(cardCode);
		int skillCode = Integer.parseInt(cardCode.split("_")[0]);
		System.out.println(skillCode);
		int target1 = 0;
		int target2 = 0;
		String guess = "";
		String content = "";
		String eContent = "";
		card tempCard = null;
		if(parameters.length == 3 && parameters[2].equals("11")){
			skillCode = -1;
		}
		switch(skillCode){
			// guard
			case 1:
				target1 = Integer.parseInt(parameters[2]);
				guess = parameters[3];
				eContent = clients[senderCode].getUsername() + " uses Guard to guess that " + clients[target1].getUsername() + " is " + guess +". ";
				if(guess.equals(clients[target1].getIden())){
					//guess is right
					defeadH(target1);
					eContent += "It is right, " + clients[target1].getUsername() + " is defeated.";
				}else{
					eContent += "It is wrong.";
				}
				break;

			// Priest
			case 2:
				target1 = Integer.parseInt(parameters[2]);
				eContent = clients[senderCode].getUsername() + " uses Priest to see card of " + clients[target1].getUsername() + ". ";
				content = target1 + "-" +clients[target1].getIden();
				break;

			// Baron
			case 3:
				target1 = Integer.parseInt(parameters[2]);
				eContent = clients[senderCode].getUsername() + " uses Baron to compare strength with " + clients[target1].getUsername() + ". ";
				if(clients[senderCode].getStrength() >= clients[target1].getStrength()){
					defeadH(target1);
					eContent += clients[target1].getUsername() + " is defeated.";
				}else{
					defeadH(senderCode);
					eContent += clients[senderCode].getUsername() + "is defeated.";

				}
				break;

			// Handmaiden
			case 4:
				immuneH(senderCode);
				eContent = clients[senderCode].getUsername() + " uses Handmaiden, he or she can not be effected until next turn.";
				break;		

			// Prince
			case 5:
				target1 = Integer.parseInt(parameters[2]);
				eContent = clients[senderCode].getUsername() + " uses Prince to " + clients[target1].getUsername() + ", " + clients[target1].getIden() + " is discarded. ";
				if(clients[target1].getIden() == "princess"){
					eContent += clients[target1].getUsername() + " is defeated.";
					defeadH(target1);
				}else{
					clients[target1].clear();
					clients[target1].sendMsg("DISC:");
					// draw new card
					if(roundCard == 16){
						strengthH();
					}else{
						clients[target1].drawCard(cards[roundCard]);
						cards[roundCard].setStatus();
						String message = "CARD:" + cards[roundCard].getCode() + "-" + cards[roundCard].getName() + "-" + cards[roundCard].getStrength() + "-" + cards[roundCard].getDesc();
						clients[target1].sendMsg(message);
						roundCard++;
					}
				}
				break;

			// King
			case 6:				
				target1 = Integer.parseInt(parameters[2]);
				target2 = Integer.parseInt(parameters[3]);
				eContent = clients[senderCode].getUsername() + " uses King to exchange cards of " + clients[target1].getUsername() + " and " + clients[target2].getUsername() + ".";
				if(target1 != target2){
					tempCard = clients[target1].getCard();
					clients[target1].drawCard(clients[target2].getCard());
					clients[target2].drawCard(tempCard);
				}
				break;

			// Countess
			case 7:
				eContent = clients[senderCode].getUsername() + " uses Countess.";
				break;

			// Princess
			case 8:
				eContent = clients[senderCode].getUsername() + " uses Princess";
				defeadH(senderCode);
				break;
			default:
			break;
		}
		if(skillCode != -1){
			responseH(senderCode, content);
			effectH(eContent);
			int judge = 0;
			int tempusr = 0;
			for(int index = 0; index < player_limit; index++){
				if(clients[index] != null && clients[index].getStatus() != 0){
					judge++;
					tempusr = index;
				}
			}
			if(judge == 1){
				winnerH(tempusr);
			}else{
				cardH();
			}
		}else{
			cardH();
		}

	}

	// handle command 'WINNER'
	public synchronized void winnerH(int userId){
		reset();
		clients[userId].setScore();
		String msg = "WINNER:" + userId;
		broadcast(msg);
		broadcast("DISC:");
		usrcodeH();
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null && clients[index].getScore() == target_scroe){
				fwinnerH(index);
				return;
			}
		}
		currentPlayer = userId;
		for(int i = 0; i < cPlayer + 1; i++){
			cardH();
		}
	}

	// handle command 'FWINNER'
	public synchronized void fwinnerH(int userId){
		String msg = "FWINNER:" + userId;
		broadcast(msg);
		reset();
		for(int index = 0; index < player_limit; index++){
			clients[index] = null;
			pool[index] = null;
		}
		cPlayer = 0;
		gameStart = true;
	}

	// reset game
	public synchronized void reset(){
		roundCard = 1;
		for(int index = 0; index < player_limit; index++){
			if(clients[index] != null){
				clients[index].reset();
			}
		}
		for(int i = 0; i < cardNum; i++){
			cards[i].reset();
		}
		shuffle();
	}

}