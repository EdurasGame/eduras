package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.logger.EduLog;

/**
 * Receives incoming messages for the client.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientReceiver extends Thread {

	private BufferedReader messageReader = null;

	private final GameLogicInterface logic;

	private NetworkEventListener networkEventListener;

	private boolean connectionAvailable = true;

	private final Client client;
	private Buffer inputBuffer;

	/**
	 * Retrieves messages from server.
	 * 
	 * @param logic
	 * @param socket
	 */
	public ClientReceiver(GameLogicInterface logic, Socket socket, Client client) {

		this.client = client;
		this.logic = logic;

		try {
			messageReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			EduLog.passException(e);
		}

	}

	@Override
	public void run() {

		inputBuffer = new Buffer();
		ClientParser p = new ClientParser(logic, inputBuffer,
				networkEventListener, client);
		p.start();

		while (connectionAvailable) {
			try {
				String messages = messageReader.readLine();
				if (messages != null) {
					EduLog.info("[CLIENT] Received message: " + messages);
					processMessages(messages);
				}
			} catch (IOException e) {
				connectionAvailable = false;
				EduLog.error("Connection to server closed.");
				p.interrupt();
				EduLog.passException(e);
			}
		}
	}

	/**
	 * Forwards messages to the ClientLogic, where they are deserialized and
	 * forwarded to the GameLogic.
	 * 
	 * @param messages
	 *            The message(s)-string to be forwarded.
	 */
	private void processMessages(String messages) {
		inputBuffer.append(messages);

	}

	/**
	 * Sets the networklistener whose methods are called when cast NetworkEvents
	 * arrive.
	 * 
	 * @param listener
	 *            The listener to set.
	 */
	public void setNetworkEventListener(NetworkEventListener listener) {
		this.networkEventListener = listener;
	}
}
