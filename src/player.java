import java.util.*;
import java.io.*;

// player object
// store inforamtion of player
public class player{
	// player id 
	private int user_id;
	// player name
	private String username = "";
	// cards which player has
	private Hashtable<String, card> cards;
	// writer which used for delivring message from server to client
	private PrintWriter writer;
	// player status 
	// ready status = 2
	// regular status = 1
	// defeated status = 0
	// immune status = -1
	private int status = 1;
	// payer's score
	private int score = 0;

	//constructor
	public player(PrintWriter writer, int user_id){
		this.writer = writer;
		this.user_id = user_id;
		cards = new Hashtable<String, card>();
	}

	// draw new card
	public void drawCard(card newCard){
		cards.put(newCard.getCode(), newCard);
	}

	// discard card 
	public void discard(String card_code){
		cards.remove(card_code);
	}

	// discard all cards
	public void clear(){
		cards.clear();
	}

	// set player status
	public void setStatus(int status){
		this.status = status;
	}

	// set player name
	public void setUsername(String username){
		this.username = username;
	}

	// add one score when player wins
	public void setScore(){
		score++;
	}

	// get score 
	public int getScore(){
		return score;
	}

	//get player name
	public String getUsername(){
		return username;
	}

	// get player id 
	public int getUserid(){
		return user_id;
	}

	// get writer
	public PrintWriter getWriter(){
		return writer;
	}

	// get player's status
	public int getStatus(){
		return status;
	}
	
	// get strength of card
	public int getStrength(){
		Collection values = cards.values();
		card fCard = (card)values.toArray()[0];
		return fCard.getStrength();
	}

	// get card name
	public String getIden(){
		Collection values = cards.values();
		card fCard = (card)values.toArray()[0];
		return fCard.getName();
	}


	// get card
	public card getCard(){
		Collection values = cards.values();
		card fCard = (card)values.toArray()[0];
		return fCard;
	}


	// get card
	public card getCard(String c){
		return cards.get(c);
	}

	// send message to player
	public void sendMsg(String message){
		try{
			writer.println(message);
			writer.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	// reset player's status
	public void reset(){
		status = 1;
		cards.clear();
	}
}