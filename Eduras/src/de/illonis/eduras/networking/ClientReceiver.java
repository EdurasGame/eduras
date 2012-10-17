package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import de.illonis.eduras.GameLogicInterface;

public class ClientReceiver extends Thread {
	
	Socket socket = null;
	BufferedReader messageReader = null;
	
	GameLogicInterface logic;
	
	boolean connectionAvailable = true;

	public ClientReceiver(GameLogicInterface logic, Socket socket) {
		
		this.socket = socket;
		this.logic = logic;
		
		try {
			messageReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	@Override
	public void run() {
		
		while(connectionAvailable) {
			try {
				String messages = messageReader.readLine();
				processMessages(messages);
			} catch (IOException e) {
				System.err.println("Connection to server closed.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param messages
	 */
	private void processMessages(String messages) {
		
		ClientLogic clientLogic = new ClientLogic(this.logic,messages);
		clientLogic.start();
		
	}
}
