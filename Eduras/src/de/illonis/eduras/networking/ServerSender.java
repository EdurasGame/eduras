package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;

import de.illonis.eduras.exceptions.BufferIsEmptyException;
import de.illonis.eduras.logger.EduLog;

/**
 * A class that sends collected messages every {@value #SEND_INTERVAL} ms.
 * 
 * @author illonis
 * 
 */
public class ServerSender extends Thread {

	/**
	 * Message send interval
	 */
	private final static int SEND_INTERVAL = 33;

	private final HashMap<Integer, ServerClient> clients;
	private final Buffer outputBuffer;
	private final Server server;
	private boolean running;

	/**
	 * Creates a new ServerSender that sends messages from given Buffer.
	 * 
	 * @param outputBuffer
	 *            Buffer to fetch messages from.
	 * @param server
	 *            Target server.
	 */
	public ServerSender(Buffer outputBuffer, Server server) {
		this.outputBuffer = outputBuffer;
		clients = new HashMap<Integer, ServerClient>();
		this.server = server;
		running = true;
	}

	/**
	 * Sends a serialized message to all receivers.
	 * 
	 * @param message
	 *            Message to send.
	 */
	public void sendMessage(String message) {
		for (ServerClient serverClient : clients.values()) {
			PrintWriter pw = serverClient.getOutputStream();
			pw.println(message);
			EduLog.log(Level.INFO, "Send message " + "'" + message
					+ "' to client with id " + serverClient.getClientId());
		}
	}

	/**
	 * Sends a message to a single client identified by its id.
	 * 
	 * @param clientId
	 *            The id of the client.
	 */
	public void sendMessageToClient(int clientId, String message) {
		PrintWriter pw = clients.get(clientId).getOutputStream();
		pw.println(message);
	}

	/**
	 * Creates ServerClient of given socket and adds it to senderlist so it
	 * reveives messages from server.
	 * 
	 * @param client
	 *            Socket of the client to add.
	 * @return Returns the created ServerClient.
	 * @throws IOException
	 *             Is thrown if the socket is somehow broken.
	 */
	public ServerClient add(Socket client) throws IOException {
		int clientId = getFreeClientId();

		ServerClient serverClient = new ServerClient(clientId, client);
		clients.put(clientId, serverClient);
		return serverClient;

	}

	/**
	 * Returns a number that has not been assigned to any client currently
	 * connected.
	 * 
	 * @return The free number.
	 * 
	 *         Returns -1 if there cannot be found a number within INT_MAX. I
	 *         guess this will never ever happen.
	 */
	private int getFreeClientId() {

		for (int i = 0; i >= 0; i++) {
			if (!clients.containsKey(new Integer(i))) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Removes given client from senderlist so it does not receive any messages.
	 * 
	 * @param client
	 *            Client to remove.
	 */
	public void remove(ServerClient client) {
		clients.remove(client.getClientId());
	}

	@Override
	public void run() {
		while (running) {
			// if (System.currentTimeMillis() - lastConnectionCheck > 10000) {
			// checkConnections();
			// }
			sendAllMessages();
			try {
				Thread.sleep(SEND_INTERVAL);
			} catch (InterruptedException e) {
				EduLog.passException(e);
			}
		}
	}

	/**
	 * Retrieves all messages from outputBuffer and sends them to all clients.
	 */
	private void sendAllMessages() {
		try {
			String[] s = outputBuffer.getAll();
			String[] filtereds = NetworkOptimizer.filterObsoleteMessages(s);
			String message = NetworkMessageSerializer.concatenate(filtereds);

			if (message.equals("")) {
				EduLog.error("Message empty!!");
			}
			EduLog.info("[SERVER] Sent all messages.");
			sendMessage(message);
		} catch (BufferIsEmptyException e) {
			// do nothing if there is no message.
		}
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Stops the sender.
	 */
	public void stopSender() {
		running = false;
	}
}