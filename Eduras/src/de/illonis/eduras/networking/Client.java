package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import de.illonis.eduras.Game;
import de.illonis.eduras.GameLogicInterface;
import de.illonis.eduras.Logic;


public class Client {

	Socket socket;
	GameLogicInterface logic;
	
	ClientReceiver receiver;
	ClientSender sender;
	
	public Client() {
		
		Game game = new Game();
		
		this.logic = new Logic(game);
		
	}
	
	public void connect(InetAddress addr, int port) {
		try {
			socket = new Socket(addr, 4387);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		receiver = new ClientReceiver(logic,socket);
	}
}
