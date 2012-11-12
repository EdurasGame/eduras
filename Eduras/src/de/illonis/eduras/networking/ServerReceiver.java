package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;

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
	 * @param inputBuffer
	 *            Buffer to write new messages into.
	 * @param client
	 *            Clientsocket that's inputstream should be used.
	 * @param clientId
	 *            The id of the client to receive messages from.
	 */
	public ServerReceiver(Server server, Buffer inputBuffer, ServerClient client) {
		this.server = server;
		this.inputBuffer = inputBuffer;
		this.client = client;
	}

	/**
	 * Puts given serialized messagestring on inputBuffer and wakes Serverlogic.
	 * 
	 * @param message
	 *            Message that should be pushed.
	 */
	private void pushToInputBuffer(String message) {
		System.out.println("[SERVER] Pushed to input Buffer: " + message);

		inputBuffer.append(message);

		System.out.println("[SERVER] Pushing ok");
		// server.wakeLogic();
	}

	@Override
	public void run() {
		waitForMessages();
	}

	/**
	 * Waits for new messages from client. Once a message is received, it is
	 * passed for interpretation.
	 */
	private void waitForMessages() {
		System.out.println("[SERVER] Waiting for messages...");
		try {
			BufferedReader br = client.getInputStream();
			while (true) {
				String line = br.readLine();
				if (line != null) {
					System.out.println("[SERVER] Received message: " + line);
					pushToInputBuffer(line);
				}

			}
		} catch (IOException e) {

			// remove the correlated player from the game
			server.handlePlayerDisconnect(client);

			System.err.println("[SERVER] Connection to client closed.");
			e.printStackTrace();
		}
	}
}