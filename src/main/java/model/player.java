package model;

import lombok.Data;

import java.io.PrintWriter;
import java.util.HashMap;

@Data
public class player {
	// model.player id
	private int userId;
	// model.player name
	private String userName;
	// cards which model.player has
	private HashMap<String, card> cards;
	// writer which used for delivring message from server to client
	private PrintWriter writer;
	/*
	-1: immune
	0: defeated
	1: regular
	2: ready
	 */
	private int status = 1;
	// payer's score
	private int score = 0;

	public player(PrintWriter writer, int userId){
		this.writer = writer;
		this.userId = userId;
		cards = new HashMap<>();
	}

	// draw new model.card
	public void drawCard(card newCard){
		cards.put(newCard.getCardCode(), newCard);
	}

	// discard model.card
	public void discard(String cardCode){
		cards.remove(cardCode);
	}

	// discard all cards
	public void clear(){
		cards = new HashMap<>();
	}

	// add one score when model.player wins
	public void addScore(){
		score++;
	}

	// get model.card
	public card getCard(String c){
		return cards.get(c);
	}

	// send message to model.player
	public void sendMsg(String message){
		try{
			writer.println(message);
			writer.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	// reset model.player's status
	public void reset(){
		status = 1;
		cards = new HashMap<>();
	}
}