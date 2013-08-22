package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

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
	private ClientParser p;

	/**
	 * Retrieves messages from server.
	 * 
	 * @param logic
	 *            The logic used.
	 * @param socket
	 *            The socket receiving on.
	 * @param client
	 *            The associated client.
	 */
	public ClientReceiver(GameLogicInterface logic, Socket socket, Client client) {

		this.client = client;
		this.logic = logic;
		inputBuffer = new Buffer();
		setName("ClientReceiver for #" + client.getOwnerId());
		try {
			messageReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			EduLog.passException(e);
		}

	}

	@Override
	public void run() {
		p = new ClientParser(logic, inputBuffer, networkEventListener, client);
		p.start();

		UDPMessageReceiver udpMessageReceiver = new UDPMessageReceiver();
		udpMessageReceiver.start();

		while (connectionAvailable) {
			try {
				String messages = messageReader.readLine();
				if (messages != null) {
					EduLog.info("[CLIENT] Received message: " + messages);
					processMessages(messages);
				}
			} catch (IOException e) {
				connectionAvailable = false;
				EduLog.error("TCP Connection to server closed. See next exception.");
				EduLog.passException(e);
				p.interrupt();
			}
		}
	}

	@Override
	public void interrupt() {
		if (p != null)
			p.interrupt();
		super.interrupt();
	}

	/**
	 * Listens to UDP messages and processes them.
	 * 
	 * @author Florian Mai <florian.ren.mai@googlemail.com>
	 * 
	 */
	class UDPMessageReceiver extends Thread {

		private static final int MAX_UDP_SIZE = 1024;

		@Override
		public void run() {
			DatagramSocket udpSocket = null;
			try {
				udpSocket = new DatagramSocket(client.getPortNumber());
			} catch (SocketException e) {
				connectionAvailable = false;
				EduLog.error("UDP connection to server closed. See next exception.");
				EduLog.passException(e);
				p.interrupt();
			}

			while (connectionAvailable) {
				DatagramPacket packet = new DatagramPacket(
						new byte[MAX_UDP_SIZE], MAX_UDP_SIZE);
				try {
					udpSocket.receive(packet);
					String messages = new String(packet.getData(), 0,
							packet.getLength());
					processMessages(messages);
				} catch (IOException e) {
					connectionAvailable = false;
					EduLog.error("UDP connection to server closed. See next exception.");
					EduLog.passException(e);
					p.interrupt();
				}
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
