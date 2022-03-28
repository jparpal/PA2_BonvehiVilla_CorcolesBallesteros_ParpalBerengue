package ex01_CS;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import ex01_CS.Request.Type;

public class Server extends Thread {

	private Socket connection;
	private BufferedReader inputChannel;
	private PrintWriter outputChannel;

	/* MAIN IS THE LAUNCHER */
	public static void main(String[] args) throws IOException {
		
		Socket connection;
		ServerSocket serverSocket = new ServerSocket(6666);
		System.out.println("Server running and listening to port 6666");
		
		while(true) {
			connection = serverSocket.accept();
			new Server(connection).start();
		}
	}
	/** CODI NOU **/
	public Server(Socket connection) { this.connection = connection; }
	
	private void disconnect() throws IOException {
		this.connection.close();
		this.inputChannel.close();
		this.outputChannel.close();
	}
	private void createChannels() throws IOException{
		this.inputChannel = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		this.outputChannel = new PrintWriter(connection.getOutputStream(), true);
	}
	
	private void reply(String message) { this.outputChannel.println(message);this.outputChannel.flush(); }
	
	private String recive() throws IOException { return this.inputChannel.readLine(); }
	
	public void run() {
		try {
			innerRun();
		} catch (IOException ioex) {
			ioex.printStackTrace(System.err);
		}
	}
	
	public void innerRun() throws IOException {
		createChannels();
		Boolean terminate = false;
		while(!terminate) {
			//String message = recive();
			//Request request = new Request(message);
			switch (new Request(recive()).type) {
				case CHECK: reply("");
				break;
				case RESET: reply("");
				break;
				case TERMINATE: reply(""); terminate=true;
				break;
				case UNKNOWN: reply("");
				break;
			}
		}
		disconnect();
	}
	/** FI CODI NOU **/
}

// utility class. Makes requests out of strings
class Request {

	public enum Type {
		CHECK, RESET, TERMINATE, UNKNOWN
	};

	public int value;
	public Type type;
	public String message;

	// make a request object out of a message...
	public Request(String message) {
		this.message = message;
		String[] elements = message.split(" ");
		if (elements[0].equalsIgnoreCase("check")) {
			try {
				this.value = Integer.parseInt(elements[1]);
				this.type = Type.CHECK;
				return;
			} catch (Exception ex) {
				this.type = Type.UNKNOWN;
				return;
			}
		}
		if (elements[0].equalsIgnoreCase("reset")) {
			this.type = Type.RESET;
			return;
		}
		if (elements[0].equalsIgnoreCase("terminate")) {
			this.type = Type.TERMINATE;
			return;
		}
		this.type = Type.UNKNOWN;
	}
}
