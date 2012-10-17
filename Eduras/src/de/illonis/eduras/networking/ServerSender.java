package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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

	private final ArrayList<PrintWriter> clients;
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
		clients = new ArrayList<PrintWriter>();
	}

	/**
	 * Sends a serialized message to all receivers.
	 * 
	 * @param message
	 *            Message to send.
	 */
	private void sendMessage(String message) {
		for (PrintWriter pw : clients) {
			pw.println(message);
		}
	}

	/**
	 * Adds outputstream of given socket to senderlist so it reveives messages
	 * from server.
	 * 
	 * @param client
	 *            Client to add.
	 */
	public void add(Socket client) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(client.getOutputStream(),true);
			clients.add(pw);
		} catch (IOException e) {
			System.out.println("[SERVER][SENDER] couldnt create printwriter.");
			server.removeClient(client);
			e.printStackTrace();
		}
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
			String message = NetworkMessageSerializer.concatenate(outputBuffer
					.getAll());
			System.out.println("[SERVER] Sent all messages.");
			sendMessage(message);
		} catch (BufferIsEmptyException e) {
			// do nothing if there is no message.
		}
	}
}