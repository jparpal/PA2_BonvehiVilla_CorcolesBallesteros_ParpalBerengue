package ex02_RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;

public class GuessGameObjectImpl extends UnicastRemoteObject implements GuessGameObject{
	
	private int countId=0;
	HashMap<Integer, ClientRep> clientMap = new HashMap<Integer, ClientRep>();
	
	protected GuessGameObjectImpl() throws RemoteException {}

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
		countId++;
		ClientRep clientRep = new ClientRep();
		clientRep.theNumber = new Random().nextInt(999)+1;
		clientMap.put(countId, clientRep);
		return countId;
	}

	public String check(int id, int number) throws RemoteException {
		if(!clientMap.containsKey(id)) throw new RemoteException("Id is unknown");	/*Throws an exception if the id is unknown */
		ClientRep clientRep = clientMap.get(id);
		clientRep.attempts++;
		if(number == clientRep.theNumber) {
			if(clientRep.justGuessed) throw new RemoteException("The number has been guessed in the last attempt but reset has not been invoked");	/* if invoked when the number has been guessed in the last attempt but reset has not been invoked, throws an exception. */
			clientRep.justGuessed=true;
			clientRep.guessed++;
			return "EQUAL";
		}else if(number < clientRep.theNumber) {
			return "LOWER";
		}else {
			return "HIGHER";
		}
	}

	public String reset(int id) throws RemoteException {
        if (!clientMap.containsKey(id))throw new RemoteException("Id is unknown");	/*Throws an exception if the id is unknown */
        int newRand = new Random().nextInt(999)+1;
        ClientRep clientRep = clientMap.get(id);
        clientRep.theNumber = newRand;
        clientRep.justGuessed=false;
        return "RESET_OK";
    }

	public String terminate(int id) throws RemoteException {
		//return "Number of attempts: " + attempts + "Number of guessed numbers: " + guessed;	
		ClientRep cr = clientMap.get(id);
        return ("GOODBYE. "+"You made "+cr.attempts +" guesses and got "+cr.guessed +" numbers right");
	}
	
}

// utility class to represent clients (stores all relevant info regarding a client)
class ClientRep {
	boolean justGuessed = false;
	int theNumber;
	int attempts = 0;
	int guessed = 0;
}
