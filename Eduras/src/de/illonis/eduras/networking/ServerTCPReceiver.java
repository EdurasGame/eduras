package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;

import de.illonis.eduras.logger.EduLog;

/**
 * The ServerReceiver receives messages from a client and pushes them to input
 * buffer.
 * 
 * @author illonis
 * 
 */
public class ServerTCPReceiver extends Thread {

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
	public ServerTCPReceiver(Server server, ServerClient client) {
		this.server = server;
		setName("ServerTCPReceiver (Client " + client.getClientId() + ")");
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
		try {
			BufferedReader br = client.getInputStream();
			while (client.isConnected()) {
				String line = br.readLine();
				if (line != null) {
					EduLog.infoLF("Server.networking.msgreceive", line);
					pushToInputBuffer(line);
				} else {
					throw new IOException("Received message was null");
				}

			}
		} catch (IOException e) {
			EduLog.errorLF("Server.networking.clientbye", client.getClientId());
			EduLog.passException(e);
		} finally {
			server.handleClientDisconnect(client);
		}
	}

	/**
	 * Stops receiving message for the certain client.
	 */
	void stopRunning() {
		client.setConnected(false);
	}

}