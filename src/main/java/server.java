import model.player;

import java.net.*;
import java.io.*;

// server side

public class server{
	//Thread used to listen request from clients.
	//each client has a listener.
	public class readHandler implements Runnable{
		private Socket sokt;
		private BufferedReader reader;
		private player client; 

		public readHandler(Socket clientSocket, player client){
			sokt = clientSocket;
			this.client = client;
			try{
				InputStreamReader isReader = new InputStreamReader(sokt.getInputStream());
				reader = new BufferedReader(isReader);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		public void run(){
			String temp = "";
			try{
				// when server recives 'close', it means that client quit
				while((temp = reader.readLine()) != null && !temp.equals("close")){
					System.out.println("New request is received");
					synchronized(game){
						game.commandHandler(client, temp);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	private ServerSocket LLSocket;
	private  player[] clients;
	private final int player_limit = 4;
	private Thread[] threadPool = new Thread[player_limit];
	// check if game start.
	private boolean gameStatus = false;
	// check if game is full.
	private boolean notFull = true;
	private int currentPlayer = 0;
	// love letter game
	private lover_letter game;

	// constructor
	public server(){
		clients = new player[player_limit];
		game = new lover_letter(clients, threadPool);
		try{
			LLSocket = new ServerSocket(52013);
			System.out.println("Server is running");
		}catch(Exception ioe){
			System.out.println("Sorry, server is down");
			ioe.printStackTrace();
		}
	}

	// start server
	public void start(){
		try{
			// keep server running
			while(true){
				// accept clients' connection.
				Socket sock = LLSocket.accept();
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				writer.println("ESTABLISHED:");
				writer.flush();
				// if there is still room for new model.player.
				// then let model.player in.
				if(!game.gameStart){
					writer.println("SORRY:");
					writer.flush();
					try{
						sock.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else if(game.cPlayer <= player_limit){
					// lock 'clients' up for avdoing other threads read or write it.
					for(int index = 0; index < player_limit; index++){
						if(clients[index] == null){
							player newPlayer = new player(writer, index);
							//create listner for client.
							threadPool[index] = new Thread(new readHandler(sock, newPlayer));
							threadPool[index].start();
							clients[index] = newPlayer;
							currentPlayer++;
							break;
						}
					}			
				}else{
					// if there is already 4 players.
					// new players are not allowed in.
					writer.println("SORRY:");
					writer.flush();		
					try{
						sock.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// run server.
	public static void main(String[] args){
		server newserver = new server();
		newserver.start();
	}
}