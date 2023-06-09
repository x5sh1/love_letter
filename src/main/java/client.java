import model.player;

import java.net.*;
import java.io.*;

// client side

public class client{

	// listen command from server
	public class readHandler implements Runnable{
		private Socket ser;
		private BufferedReader reader;
		public readHandler(Socket sock){
			ser = sock;
			try{
				InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(streamReader);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		public void run(){
			String temp = "";
			try{
				while((temp = reader.readLine()) != null){
					System.out.println("new response is received.");
					System.out.println(temp);
					synchronized(cmdHandler){
						cmdHandler.commandHandler(null, temp);
					}				
				}

			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

	}

	private final int player_limit = 4;
	private player[] clients = new player[player_limit];
	private Socket sock;
	private PrintWriter writer;
	private llClient cmdHandler;
	public client(){
		try{
			sock = new Socket("localhost", 52013);
			writer = new PrintWriter(sock.getOutputStream());
			Thread listener = new Thread(new readHandler(sock));
			cmdHandler = new llClient(clients, writer);
			listener.start();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	// run client
	public static void main(String[] args){
		client newClient = new client();
	}
}