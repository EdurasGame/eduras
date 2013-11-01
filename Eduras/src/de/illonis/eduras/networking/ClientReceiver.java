package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.locale.Localization;

/**
 * Receives incoming messages for the client.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientReceiver extends Thread {

	private final static Logger L = EduLog.getLoggerFor(ClientReceiver.class
			.getName());

	private BufferedReader messageReader = null;

	private final GameLogicInterface logic;

	private NetworkEventListener networkEventListener;

	private boolean connectionAvailable = true;

	private final Client client;
	private final Buffer inputBuffer;
	private ClientParser p;

	private DatagramSocket udpSocket;

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
			L.log(Level.SEVERE, "error reader init", e);
		}

		try {
			udpSocket = new DatagramSocket(client.getPortNumber());
		} catch (SocketException e) {
			connectionAvailable = false;
			L.log(Level.SEVERE, Localization.getStringF(
					"Client.networking.udpopenerror", client.getPortNumber()),
					e);
			interrupt();
			return;
		}

	}

	/**
	 * Returns the DatagramSocket the receiver receives udp messages on.
	 * 
	 * @return The DatagramSocket.
	 */
	public DatagramSocket getUdpSocket() {
		return udpSocket;
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
					L.info(Localization.getStringF(
							"Client.networking.msgreceive", messages));
					processMessages(messages);
				}
			} catch (IOException e) {
				connectionAvailable = false;
				L.log(Level.SEVERE,
						Localization.getString("Client.networking.tcpclose"), e);
				interrupt();
				return;
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
					L.log(Level.SEVERE, Localization
							.getString("Client.networking.udpclose"), e);
					interrupt();
				}
			}
			udpSocket.close();
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
