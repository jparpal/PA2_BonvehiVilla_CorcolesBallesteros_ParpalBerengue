package ex01_CS;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {

	private ServerSocket serverSocket;
	private Socket connection;
	private BufferedReader inputChannel;
	private PrintWriter outputChannel;

	/* MAIN IS THE LAUNCHER */
	public static void main(String[] args) throws IOException {
		
		Server server = new Server();
		server.run();
	}
	
	/** INICI CODI NOU **/
	
	private void acceptConnection() throws IOException {
		this.connection = this.serverSocket.accept();
		this.inputChannel = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
		this.outputChannel = new PrintWriter(this.connection.getOutputStream(), true);
	}
	
	private void disconnect() throws IOException {
		this.connection.close();
		this.inputChannel.close();
		this.outputChannel.close();
	}
	
	private void sendReply(String message) {
		this.outputChannel.println(message);
	}
	
	public void run() {
		try {
			innerRun();
		} catch (IOException ioex) {
			ioex.printStackTrace(System.err);
		}
	}
	
	public void innerRun() throws IOException {
		Request request;
		String answer = "";
		serverSocket = new ServerSocket(6666);
		System.out.println("Server running and listening to port 6666");
		while(true) {
			acceptConnection();
			String message = this.inputChannel.readLine();
			request = new Request(message);
			switch (request.type) {
			
			}
		}
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
