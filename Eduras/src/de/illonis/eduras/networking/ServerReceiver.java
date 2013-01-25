package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;

import de.illonis.eduras.logger.EduLog;

/**
 * The ServerReceiver receives messages from clients and pushes them to input
 * buffer.
 * 
 * @author illonis
 * 
 */
public class ServerReceiver extends Thread {

	private final Buffer inputBuffer;
	private final ServerClient client;
	private final Server server;

	/**
	 * Creates a new ServerReciever that listens for new messages on given
	 * clientsocket and hands them to given inputBuffer.
	 * 
	 * @param server
	 *            Server that this receiver is assigned to.
	 * @param client
	 *            Client that's inputstream should be used.
	 */
	public ServerReceiver(Server server, ServerClient client) {
		this.server = server;
		this.inputBuffer = server.getInputBuffer();
		this.client = client;
	}

	/**
	 * Puts given serialized messagestring on inputBuffer and wakes Serverlogic.
	 * 
	 * @param message
	 *            Message that should be pushed.
	 */
	private void pushToInputBuffer(String message) {
		inputBuffer.append(message);
	}

	@Override
	public void run() {
		synchronized (client) {
			if (!client.isConnected()) {
				try {
					client.wait();
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		}
		waitForMessages();
	}

	/**
	 * Waits for new messages from client. Once a message is received, it is
	 * passed for interpretation.
	 */
	private void waitForMessages() {
		EduLog.info("[SERVER] Waiting for messages...");
		try {
			BufferedReader br = client.getInputStream();
			while (client.isConnected()) {
				String line = br.readLine();
				if (line != null) {
					EduLog.info("[SERVER] Received message: " + line);
					pushToInputBuffer(line);
				} else {
					server.handleClientDisconnect(client);
				}

			}
		} catch (IOException e) {

			// remove the correlated player from the game
			server.handleClientDisconnect(client);

			EduLog.error("[SERVER] Connection to client closed.");
			EduLog.passException(e);
		}
	}

	/**
	 * Stops receiving message for the certain client.
	 */
	void stopRunning() {
		client.setConnected(false);
	}

}