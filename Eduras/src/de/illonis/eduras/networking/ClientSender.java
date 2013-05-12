package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import de.illonis.eduras.exceptions.ConnectionLostException;
import de.illonis.eduras.logger.EduLog;

/**
 * Sends messages/events to the server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientSender {

	private Socket socket = null;
	private boolean active;
	private PrintWriter messageWriter = null;

	/**
	 * Creates a new ClientSender that sends messages via the given socket.
	 * 
	 * @param socket
	 *            The socket to send messages via.
	 */
	public ClientSender(Socket socket) {

		this.socket = socket;
		active = true;
		try {
			this.messageWriter = new PrintWriter(this.socket.getOutputStream(),
					true);
		} catch (IOException e) {
			active = false;
			EduLog.passException(e);
		}
	}

	/**
	 * Sends a message via the socket if available.
	 * 
	 * @param message
	 *            The message to send.
	 * @throws ConnectionLostException
	 *             when connection to server is lost. The client sender will not
	 *             accept any messages anymore.
	 */
	public void sendMessage(String message) throws ConnectionLostException {
		if (active) {
			EduLog.info("[CLIENT] Sending message: " + message);
			messageWriter.println(message);
			if (messageWriter.checkError()) {
				EduLog.error("[CLIENT][SENDER] Error sending message. Closing writer.");
				active = false;
				close();
				throw new ConnectionLostException();
			}
		}
	}

	/**
	 * Closes the output connection.
	 */
	public void close() {
		messageWriter.close();
	}

}
