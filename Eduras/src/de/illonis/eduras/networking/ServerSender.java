package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import de.illonis.eduras.exceptions.BufferIsEmptyException;

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

	private final HashMap<Integer, PrintWriter> clients;
	private final Buffer outputBuffer;
	private Server server;

	/**
	 * Creates a new ServerSender that sends messages from given Buffer.
	 * 
	 * @param server
	 *            Server to assign this sender to.
	 * @param outputBuffer
	 *            Buffer to fetch messages from.
	 */
	public ServerSender(Server server, Buffer outputBuffer) {
		this.outputBuffer = outputBuffer;
		clients = new HashMap<Integer, PrintWriter>();
	}

	/**
	 * Sends a serialized message to all receivers.
	 * 
	 * @param message
	 *            Message to send.
	 */
	private void sendMessage(String message) {
		for (PrintWriter pw : clients.values()) {
			pw.println(message);
		}
	}

	/**
	 * Sends a message to a single client identified by its id.
	 * 
	 * @param clientId
	 *            The id of the client.
	 */
	public void sendMessageToClient(int clientId, String message) {
		PrintWriter pw = clients.get(clientId);
		pw.println(message);
	}

	/**
	 * Adds outputstream of given socket to senderlist so it reveives messages
	 * from server.
	 * 
	 * @param client
	 *            Client to add.
	 * @return Returns the id that was assigned to the new client.
	 */
	public int add(Socket client) {
		int clientId = getFreeClientId();
		PrintWriter pw;
		try {
			pw = new PrintWriter(client.getOutputStream(), true);
			clients.put(clientId, pw);
			return clientId;
		} catch (IOException e) {
			System.out.println("[SERVER][SENDER] couldnt create printwriter.");
			server.removeClient(client);
			e.printStackTrace();
			return -1;
		}
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
	public void remove(Socket client) {
		clients.remove(client);
	}

	@Override
	public void run() {
		while (true) {
			sendAllMessages();
			try {
				Thread.sleep(SEND_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Retrieves all messages from outputBuffer and sends them to all clients.
	 */
	private void sendAllMessages() {
		try {
			String[] s = outputBuffer.getAll();
			String message = NetworkMessageSerializer.concatenate(s);
			System.out.println("[SERVER] Sent all messages.");
			sendMessage(message);
		} catch (BufferIsEmptyException e) {
			// do nothing if there is no message.
		}
	}
}