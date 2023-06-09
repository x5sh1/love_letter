import model.card;
import model.player;

import java.io.*;
import javax.swing.*;
import java.awt.*;

class llClient implements rules{
	// orginial coord y 
	private int orY;
	// current turn 
	private int cuTurn;
	private player[] clients;
	private final int player_limit = 4;
	private int user;
	private String userN;
	int targetScore;
	// model.player status
	private final int regularS = 1;
	private final int readyS = 2;
	private final int defeatedS = 0;
	private final int immuneS = 3;
	private PrintWriter writer;
	// Gui
	private clientGUI gui;
	private JButton statusBu;
	private JButton quitBu;
	private JButton[] cards;
	// use skill
	private card usedCard;
	private String t1 = null;
	private String t2 = null;
	private String guess = null;
	// game end
	public boolean gameEnd = false;

	public llClient(player[] clients, PrintWriter writer){
		this.clients = clients;
		this.writer = writer;
		gui = new clientGUI(clients);
		statusBu = gui.statuSBu;
        statusBu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusBuClicked(evt);
            }
        });
        quitBu = gui.quitBu;
        quitBu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitBuClicked(evt);
            }
        });
        cards = gui.cards;
        gui.conBu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conBuClicked(evt);
            }
        });
        gui.disBu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disBuClicked(evt);
            }
        });
	}


	// ready button function
	public void statusBuClicked(java.awt.event.ActionEvent evt){
		if(clients[user].getStatus() == readyS){
			String msg = "REGULAR:" + user;
			send(msg);
			clients[user].setStatus(readyS);
			statusBu.setSize(80,80);
			statusBu.setText("Ready");
		}else{
			String msg = "READY:" + user;
			send(msg);
			clients[user].setStatus(regularS);
			statusBu.setSize(100,80);
			statusBu.setText("Cancel Ready");
		}
	}

	// quit button function
	public void quitBuClicked(java.awt.event.ActionEvent evt){
		String msg = "QUIT:" + user;
		send(msg);
		send("close");
		gameEnd = true;
		System.exit(0);
	}
	
	// command handler
	public void commandHandler(player sender, String message){
		String[] commands = message.split("/");
		int numCom = commands.length;
		for(int index = 0; index < numCom; index++){
			String command = commands[index];
			String[] listCom = command.split(":");
			switch(listCom[0]){
				case "ESTABLISHED":
					estaHanld();
					break;
				case "EXIST":
					exisHandle();
					break;
				case "WEL":
					welHandle();
					break;
				case "SORRY":
					sorrHandle();
					break;
				case "USRCODE":
					usrcHandle(listCom[1]);
					break;
				case "SCORE?":
					askScorHandle(listCom[1]);
					break;
				case "START":
					starHandle(listCom[1]);
					break;
				case "CARD":
					cardHandle(listCom[1]);
					break;
				case "TURN":
					turnHandle(listCom[1]);
					break;
				case "STRENGTH":
					streHandle(listCom[1]);
					break;
				case "WINNER":
					winnHandle(listCom[1]);
					break;
				case "FWINNER":
					fwinHandler(listCom[1]);
					break;
				case "EFFECT":
					effeHandle(listCom[1]);
					break;
				case "DEFEATED":
					defeHandle(listCom[1]);
					break;
				case "READY":
					readHandle(listCom[1]);
					break;
				case "IMMUNE":
					immuHandle(listCom[1]);
					break;
				case "REGULAR":
					reguHandle(listCom[1]);
					break;
				case "QUIT":
					quitHandle(listCom[1]);
					break;
				case "RESPONSE":
					respHandle(listCom[1]);
					break;
				case "DISC":
					clients[user].clear();
					if(cards[0] != null){
						cards[0].setVisible(false);
						gui.cardP.remove(cards[0]);
					}
					if(cards[1] != null){
						cards[1].setVisible(false);
						gui.cardP.remove(cards[1]);
					}
					cards[0] = null;
					cards[1] = null;
					break;
				default:
					break;
			}
			gui.setSelf(user);
			gui.updatePlayer();
		}
	}


	// target 1 function
	public void target1Clicked(java.awt.event.ActionEvent evt){
		t1 = ((JButton)evt.getSource()).getName();
	}

	// target 2 function
	public void target2Clicked(java.awt.event.ActionEvent evt){
		t2 = ((JButton)evt.getSource()).getName();
	}
	
	// guess fucntion
	public void guessClicked(java.awt.event.ActionEvent evt){
		guess = ((JButton)evt.getSource()).getName();
	}


	// command ESTABLISHED
	public void estaHanld(){
		String noti = "Please create username.(Do not contains ':', '-', '_', '&')";
		String cmd = "";
		cmd = JOptionPane.showInputDialog(noti);
		while(cmd == null || cmd.equals("") || (cmd.contains(new StringBuffer(":")) || cmd.contains(new StringBuffer("-")) || cmd.contains(new StringBuffer("_")) || cmd.contains(new StringBuffer("&")))){
			cmd = JOptionPane.showInputDialog(noti);
		}
		userN = cmd;
		usrnHandle(cmd);
	}

	// command EXIST
	public void exisHandle(){
		String noti = "Sorry, the username has existed, please create new one.";
		JOptionPane.showMessageDialog(null, noti);
		estaHanld();
	}

	// command WEL
	public void welHandle(){
		String noti = "Welcome! model.player.";
		JOptionPane.showMessageDialog(null, noti);
	}

	// Commadn QUIT
	public void quitHandle(String content){
		int uCode = Integer.parseInt(content);
		clients[uCode] = null;
	}

	// command SORRY
	public void sorrHandle(){
		String noti = "Sorry, you can't join the game because game is on or is full, please try later.";
		JOptionPane.showMessageDialog(null, noti);
		gameEnd = true;
	}

	// command USRCODE
	public void usrcHandle(String content){
		String[] playerList = null;
		if(content.contains("&")){
			playerList = content.split("&");
		}else{
			playerList = new String[1];
			playerList[0] = content;
		}
		for(int index = 0; index < playerList.length; index++){
			String temp = playerList[index];
			String[] playerInfo = temp.split("-");
			String usrname = playerInfo[0];
			int usrcode = Integer.parseInt(playerInfo[1]);
			int pStatus = Integer.parseInt(playerInfo[2]);
			int pScore = Integer.parseInt(playerInfo[3]);
			if(clients[usrcode] == null){
				clients[usrcode] = new player(null, usrcode);
			}
			clients[usrcode].setStatus(pStatus);
			clients[usrcode].setUsername(usrname);
			if(clients[usrcode].getUsername().equals(userN)){
				user = usrcode;
			}
		}
	}
	
	// if the string is digit 
	private boolean isDigit(String s){
		try{
			int temp = Integer.parseInt(s);
			if(temp >= 3 && temp <= 7){
				return true;
			}
			return false;
		}catch(Exception ex){
			return false;
		}
	}

	// command SCORE?
	public void askScorHandle(String content){
		statusBu.setVisible(false);
		quitBu.setVisible(false);
		int tar = Integer.parseInt(content);
		if(user == tar){
			String noti = "Please enter the amount of rounds.(The range of amount is 3-7)";
			String cmd = JOptionPane.showInputDialog(noti);
			while(!isDigit(cmd)){
				noti = "The range of amount is 3-7.";
				cmd = JOptionPane.showInputDialog(noti);
			}
			String commd = "SCORE:" + cmd;
			send(commd);
		}else{
			String msg = "Game will start soon, please wait model.player " + clients[tar].getUsername() + " for choosing game rounds.";
			JOptionPane.showMessageDialog(null, msg);
		}

	}

	// command START
	public void starHandle(String content){
		gui.desField.setVisible(true);
		gui.boxSc.setVisible(true);
		for(int index = 0; index < 4; index++){
			if(clients[index] != null){
				clients[index].setStatus(regularS);
			}
		}
		for(int i = 0; i < 4; i++){
        	if(gui.tar1Bu[i] != null){
        		gui.tar1Bu[i].addActionListener(new java.awt.event.ActionListener() {
            		public void actionPerformed(java.awt.event.ActionEvent evt) {
                		target1Clicked(evt);
            		}
        		});
        		gui.tar2Bu[i].addActionListener(new java.awt.event.ActionListener() {
            		public void actionPerformed(java.awt.event.ActionEvent evt) {
                		target2Clicked(evt);
            		}
        		});
        	}
        }

        for(int i = 0; i < 8; i++){
        	if(gui.gusb[i] != null){
        		gui.gusb[i].addActionListener(new java.awt.event.ActionListener() {
            		public void actionPerformed(java.awt.event.ActionEvent evt) {
                		guessClicked(evt);
            		}
        		});
        	}
        }
		targetScore = Integer.parseInt(content);
		gui.tarLabel.setText("Target Score: " + targetScore);
	}

	// command CARD
	public void cardHandle(String content){
		gui.cardP.setVisible(true);
		String[] cardInfo = content.split("-");
		String cCode = cardInfo[0];
		String cName = cardInfo[1];
		int cStrength = Integer.parseInt(cardInfo[2]);
		String cDesc = cardInfo[3];
		card newCard = new card(cCode, cName, cStrength, cDesc);
		clients[user].drawCard(newCard);
		String pic = cCode.split("_")[0];
		for(int i = 0; i < 2; i++){
			if(cards[i] == null){
				cards[i] = new JButton(null, new ImageIcon("image/" + pic + ".PNG"));
				cards[i].setName(cCode);
				cards[i].setToolTipText(cDesc);
				cards[i].setPreferredSize(new Dimension(150,200));
				gui.cardP.add(cards[i]);
				cards[i].addActionListener(new java.awt.event.ActionListener() {
            		public void actionPerformed(java.awt.event.ActionEvent evt) {
               			CardClicked(evt);
            		}
        		});
        		cards[i].addMouseListener(new java.awt.event.MouseAdapter() {
        			public void mouseExited(java.awt.event.MouseEvent evt) {
               			cardExit(evt);
            		}
            		public void mouseEntered(java.awt.event.MouseEvent evt) {
                		cardEntered(evt);
            		}
        		});
        		break;
			}
		}
	}
	// model.card enter
	 public void cardEntered(java.awt.event.MouseEvent evt) { 
	 	JButton temButton = (JButton)evt.getSource();
	 	int x = temButton.getX();
	 	int y = temButton.getY();
	 	orY = y;
	 	temButton.setLocation(x, y - 10);
	 	gui.desField.setText(temButton.getToolTipText());
	 }

	 // model.card exit
	 public void cardExit(java.awt.event.MouseEvent evt){
	 	JButton temButton = (JButton)evt.getSource();
	 	int x = temButton.getX();
	 	int y = temButton.getY();
	 	temButton.setLocation(x, orY);
	 	gui.desField.setText("");
	 }

	// model.card button
	public void CardClicked(java.awt.event.ActionEvent evt){
		if(user == cuTurn){
			gui.conBu.setVisible(true);
			gui.disBu.setVisible(true);
			usedCard = clients[user].getCard(((JButton)evt.getSource()).getName());
			if(usedCard.getCode().split("_")[0].equals("1")){
				gui.tar1p.setVisible(true);
				gui.tar2p.setVisible(false);
				gui.gusp.setVisible(true);
			}else if(usedCard.getCode().split("_")[0].equals("4") || usedCard.getCode().split("_")[0].equals("7") || usedCard.getCode().split("_")[0].equals("8")){
				gui.tar1p.setVisible(false);
				gui.tar2p.setVisible(false);
				gui.gusp.setVisible(false);
			}else if(usedCard.getCode().split("_")[0].equals("6")){
				gui.tar1p.setVisible(true);
				gui.tar2p.setVisible(true);
				gui.gusp.setVisible(false);
			}else{
				gui.tar1p.setVisible(true);
				gui.tar2p.setVisible(false);
				gui.gusp.setVisible(false);
			}
			JButton temButton = (JButton)evt.getSource();
	 		int x = temButton.getX();
	 		int y = temButton.getY();
	 		gui.desField.setText(temButton.getToolTipText());
	 		temButton.setLocation(x, orY);
		}
	}


	// confirm button
	public void conBuClicked(java.awt.event.ActionEvent evt){
		if(usedCard.getCode().split("_")[0].equals("1")){
			if(t1 == null || guess == null){
				JOptionPane.showMessageDialog(null, "Empty guess or target");
				return;
			}
			skilHandle(user + "", usedCard.getCode(), t1, guess);
		}else if(usedCard.getCode().split("_")[0].equals("4") || usedCard.getCode().split("_")[0].equals("7") || usedCard.getCode().split("_")[0].equals("8")){
			skilHandle(user + "", usedCard.getCode());
		}else if(usedCard.getCode().split("_")[0].equals("6")){
			if(t1 == null || t2 == null){
				JOptionPane.showMessageDialog(null, "Empty target");
				return;
			}
			skilHandle(user + "", usedCard.getCode(),t1, t2);
		}else{
			if(t1 == null){
				JOptionPane.showMessageDialog(null, "Empty guess or target");
				return;
			}
			skilHandle(user + "", usedCard.getCode(),t1);
		}
		gui.conBu.setVisible(false);
		gui.disBu.setVisible(false);
		gui.tar1p.setVisible(false);
		gui.tar2p.setVisible(false);
		gui.gusp.setVisible(false);
		clients[user].discard(usedCard.getCode());
		if(cards[0].getName().equals(usedCard.getCode())){
			cards[0].setVisible(false);
			gui.cardP.remove(cards[0]);
			cards[0] = null;
		}else{
			cards[1].setVisible(false);
			gui.cardP.remove(cards[1]);
			cards[1] = null;
		}

		t1 = null;
		t2 = null;
		guess = null;
		gui.desField.setText("");
	}


	// discard button
	public void disBuClicked(java.awt.event.ActionEvent evt){
		gui.conBu.setVisible(false);
		gui.disBu.setVisible(false);
		gui.tar1p.setVisible(false);
		gui.tar2p.setVisible(false);
		gui.gusp.setVisible(false);
		skilHandle(user + "", usedCard.getCode(),"11");
		clients[user].discard(usedCard.getCode());
		if(cards[0].getName().equals(usedCard.getCode())){
			cards[0].setVisible(false);
			gui.cardP.remove(cards[0]);
			cards[0] = null;
		}else{
			cards[1].setVisible(false);
			gui.cardP.remove(cards[1]);
			cards[1] = null;
		}

		t1 = null;
		t2 = null;
		guess = null;
	}

	// command TURN
	public void turnHandle(String content){
		cuTurn = Integer.parseInt(content);
		gui.turnLabel.setVisible(true);
		gui.turnLabel.setText("Now, it is " + clients[cuTurn].getUsername() + " turn.");
	}

	// command STRENGTH
	public void streHandle(String content){
		String[] list = content.split("&");
		String mes = "";
		for(String str:list){
			String[] strL = str.split("-");
			mes += clients[Integer.parseInt(strL[0])].getUsername() + ": " + strL[1] + "\n";
		}
		gui.effectField.append("Strength: " + mes);
		gui.effectField.append("----------------------------\n");
		gui.effectField.setCaretPosition(gui.effectField.getText().length());
	}

	// command WINNER
	public void winnHandle(String content){
		for(int i = 0; i < player_limit; i++){
			if(clients[i] != null){
				clients[i].reset();
			}
		}
		int uCode = Integer.parseInt(content);
		clients[uCode].setScore();
		gui.effectField.append(clients[uCode].getUsername() + " is winner.\n");
		gui.effectField.append("----------------------------\n");
		gui.effectField.setCaretPosition(gui.effectField.getText().length());
	}

	// command FWINNER
	public void fwinHandler(String content){
		int uCode = Integer.parseInt(content);
		JOptionPane.showMessageDialog(null, clients[uCode].getUsername() + " is final winner.");
		send("close");
		gameEnd = true;
		System.exit(0);
	}

	// command EFFECT
	public void effeHandle(String content){
		gui.effectField.append(content + "\n");
		gui.effectField.append("----------------------------\n");
		gui.effectField.setCaretPosition(gui.effectField.getText().length());
	}

	// command DEFEATED
	public void defeHandle(String content){
		int uCode = Integer.parseInt(content);
		clients[uCode].setStatus(defeatedS);
		if(uCode == user){
			gui.effectField.append("You are defeated\n");
			gui.effectField.append("----------------------------\n");
			gui.effectField.setCaretPosition(gui.effectField.getText().length());
		}
	}

	// command READY
	public void readHandle(String content){
		int uCode = Integer.parseInt(content);
		clients[uCode].setStatus(readyS);
	}

	// commadn IMMUNE
	public void immuHandle(String content){
		int uCode = Integer.parseInt(content);
		clients[uCode].setStatus(immuneS);
	}

	// command REGULAR
	public void reguHandle(String content){
		int uCode = Integer.parseInt(content);
		clients[uCode].setStatus(regularS);
	}

	// command RESPONSE
	public void respHandle(String content){
		if(!content.equals("none")){
			int uCode = Integer.parseInt(content.split("-")[0]);
			String coll = content.split("-")[1];
			String noti = clients[uCode].getUsername() + "' model.card is " + coll + ".";
			gui.effectField.append(noti + "\n");
			gui.effectField.append("----------------------------\n");
			gui.effectField.setCaretPosition(gui.effectField.getText().length());
		}
	}

	// send commnad
	public void send(String msg){
		writer.println(msg);
		writer.flush();
	}

	// command USRNAME
	public void usrnHandle(String content){
		String message = "USRNAME:"+ content;
		send(message);
	}

	// commnad SCORE
	public void scorHandle(String content){
		String message = "SCORE:" + content;
		send(message);
	}

	// command SKILL
	public void skilHandle(String sender, String content){
		String message = "SKILL:" + sender + "-" + content;
		send(message);
	}

	public void skilHandle(String sender, String content, String target1){
		String message = "SKILL:" + sender + "-" + content + "-" + target1;
		send(message);
	}

	public void skilHandle(String sender, String content, String target1, String target2){
		String message = "SKILL:" + sender + "-" + content + "-" + target1 + "-" + target2;
		send(message);
	}


}