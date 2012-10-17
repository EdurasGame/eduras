package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Sends messages/events to the server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientSender  {

	private Socket socket = null;
	private PrintWriter messageWriter = null;

	/**
	 * Creates a new ClientSender that sends messages via
	 * the given socket.
	 * @param socket The socket to send messages via.
	 */
	public ClientSender(Socket socket) {

		this.socket = socket;
		try {
			this.messageWriter = new PrintWriter(this.socket.getOutputStream(),
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message via the socket.
	 * @param message The message to send.
	 */
	public void sendMessage(String message) {
		System.out.println("[CLIENT] Sending message: " + message);
		messageWriter.println(message);
	}

	/**
	 * Closes the output connection.
	 */
	public void close() {
		messageWriter.close();
	}

}
