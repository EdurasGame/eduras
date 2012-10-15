package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The ServerReceiver receives messages from clients and pushes them to input
 * buffer.
 * 
 * @author illonis
 * 
 */
public class ServerReceiver extends Thread {

	private Buffer inputBuffer;
	private Socket client;
	private Server server;

	public ServerReceiver(Server server, Buffer inputBuffer, Socket client) {
		this.server = server;
		this.inputBuffer = inputBuffer;
		this.client = client;
	}

	private void pushToInputBuffer(String message) {
		synchronized (Buffer.SYNCER) {
			inputBuffer.append(message);
		}
	}

	@Override
	public void run() {
		waitForMessages();
	}

	private void waitForMessages() {
		String line;

		try {
			InputStream in = client.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while (true) {
				line = br.readLine();
				if (line != null)
					pushToInputBuffer(line);
			}
		} catch (IOException e) {
			server.removeClient(client);
			System.err.println("Connection to client closed.");
			e.printStackTrace();
		}

	}

}
