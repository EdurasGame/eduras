package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.illonis.eduras.events.ConnectionAbortedEvent;
import de.illonis.eduras.events.ConnectionQuitEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.exceptions.ConnectionLostException;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.networking.ClientSender.PacketType;

/**
 * A client that connects to the game server and starts receiving and sending
 * events.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Client {

	/**
	 * Connection timeout when connecting to server (in ms).
	 */
	public final static int CONNECT_TIMEOUT = 10000;

	private Socket socket;

	private final GameLogicInterface logic;

	private NetworkEventListener networkEventListener;

	private ClientSender sender;
	private ClientReceiver receiver;

	private int ownerId;

	/**
	 * Creates a new Client.
	 * 
	 * @param logic
	 *            the logic that client uses.
	 */
	public Client(GameLogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * Connects to a server on the given address and port.
	 * 
	 * @param addr
	 *            The server's address.
	 * @param port
	 *            The server's port.
	 * @throws IOException
	 *             when connection establishing failed.
	 */
	public void connect(InetAddress addr, int port) throws IOException {
		EduLog.info("[CLIENT] Connecting to " + addr.toString() + " at " + port);
		socket = new Socket();
		InetSocketAddress iaddr = new InetSocketAddress(addr, port);
		socket.connect(iaddr, CONNECT_TIMEOUT);
		receiver = new ClientReceiver(logic, socket, this);
		receiver.setNetworkEventListener(networkEventListener);
		receiver.start();
		sender = new ClientSender(socket);
		sender.setUdpSocket(receiver.getUdpSocket());

		// createEchoSocket();
	}

	/**
	 * Sends a message to Server
	 * 
	 * @param message
	 *            message to send
	 * @param packetType
	 *            tells whether the packet is a UDP or TCP packet
	 */
	public void sendMessage(String message, PacketType packetType) {
		try {
			sender.sendMessage(message, packetType);
		} catch (ConnectionLostException e) {
			connectionLost();
		}

	}

	/**
	 * Sets owner id to given id.
	 * 
	 * @param userId
	 *            new owner id.
	 */
	public void setOwnerId(int userId) {
		this.ownerId = userId;
	}

	/**
	 * Returns current owner id.
	 * 
	 * @return current owner id.
	 */
	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * Returns the logic that is used by this client.
	 * 
	 * @return the used logic.
	 * 
	 * @author illonis
	 */
	public GameLogicInterface getLogic() {
		return logic;
	}

	/**
	 * Sets the network event listener. This replaces any old listener.
	 * 
	 * @param listener
	 *            the new listener.
	 * 
	 * @author illonis
	 */
	public void setNetworkEventListener(NetworkEventListener listener) {
		this.networkEventListener = listener;
	}

	/**
	 * Invokes connection lost action.
	 */
	public void connectionLost() {
		NetworkEvent ev = new ConnectionAbortedEvent(ownerId);
		networkEventListener.onNetworkEventAppeared(ev);
		receiver.interrupt();
	}

	/**
	 * Invokes disconnect action.
	 */
	public void disconnect() {
		NetworkEvent ev = new ConnectionQuitEvent(ownerId);
		networkEventListener.onNetworkEventAppeared(ev);

		if (receiver != null)
			receiver.interrupt();
		logic.onShutdown();
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
				EduLog.passException(e);
			}
	}

	/**
	 * Returns the number of the port this client's socket is bound to.
	 * 
	 * @return The number of the local port.
	 */
	public int getPortNumber() {
		return socket.getLocalPort();
	}
}
