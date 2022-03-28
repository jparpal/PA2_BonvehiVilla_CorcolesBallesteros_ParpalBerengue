package ex02_RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;

public class GuessGameObjectImpl extends UnicastRemoteObject implements GuessGameObject{
	private int ranNum;
	private Random ran = new Random();
	protected GuessGameObjectImpl() throws RemoteException {
			
	}

	// launcher
	public static void main (String [] args)  {
		try {
			Registry registry = LocateRegistry.createRegistry(1999);
			registry.bind("GUESS", new GuessGameObjectImpl());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Guess service bound and running");
	}
	
	public int startGame () throws RemoteException{
		ranNum = ran.nextInt();
		return ranNum;
	}

	public String check(int id, int number) throws RemoteException {
		/*	FALTA: Throws an exception if the id is unknown		*/
		if(number == ranNum) {
			return "EQUAL";
		}else if(number < ranNum) {
			return "LOWER";
		}else {
			return "HIGHER";
		} /*	FALTA: if invoked when the number has been guessed in the last attempt but reset has not been invoked, throws an exception.		*/
	}

	public String reset(int id) throws RemoteException {
		/*	FALTA: Throws an exception if the id is unknown		*/
		ranNum = ran.nextInt();
		return Integer.toString(ranNum);
	}

	public String terminate(int id) throws RemoteException {
		//return "Number of attempts: " + attempts + "Number of guessed numbers: " + guessed;	
		return null;
	}
	
}

// utility class to represent clients (stores all relevant info regarding a client)
class ClientRep {
	boolean justGuessed = false;
	int theNumber;
	int attempts = 0;
	int guessed = 0;
}
