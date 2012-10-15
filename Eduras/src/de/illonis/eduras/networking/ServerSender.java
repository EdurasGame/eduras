package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSender extends Thread {

	private final static int SEND_INTERVAL = 33;
	private ArrayList<PrintWriter> clients;
	private Buffer outputBuffer;

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
			pw = new PrintWriter(client.getOutputStream());
			clients.add(pw);
		} catch (IOException e) {
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
			outputBuffer.getAll();
			// TODO: serialize them
			// TODO: send them
			try {
				Thread.sleep(SEND_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
