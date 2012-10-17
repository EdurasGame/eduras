package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread {

	private Socket socket = null;
	private PrintWriter messageWriter = null;

	public ClientSender(Socket socket) {

		this.socket = socket;
		try {
			this.messageWriter = new PrintWriter(this.socket.getOutputStream(),
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	public void sendMessage(String message) {
		System.out.println("[CLIENT] Sending message: " + message);
		messageWriter.println(message);
	}

	public void close() {
		messageWriter.close();
	}

}
