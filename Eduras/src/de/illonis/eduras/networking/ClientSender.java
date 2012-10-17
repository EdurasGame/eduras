package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread {

	Socket socket = null;
	PrintWriter messageWriter = null;
	
	String message;
	
	
	public ClientSender(Socket socket, String message) {
		
		this.socket = socket;

		
		this.message = message;
		
	}
	
	
	@Override
	public void run() {
		
		try {
			this.messageWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		messageWriter.write(message);
		
	}
}
