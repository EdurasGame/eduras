package de.illonis.eduras.networking;

import de.illonis.eduras.Game;
import de.illonis.eduras.Logic;



public class Server {

	public final static int PORT = 4387;
	
	public Server() {
		Game g = new Game();
		Logic l = new Logic(g);
		Buffer inputBuffer = new Buffer();
		ServerReceiver sr =  new ServerReceiver(inputBuffer);
		
		ServerLogic sl = new ServerLogic(inputBuffer, l);
	}
	
	
	private class ConnectionListener extends Thread {
		
	}
	
}
