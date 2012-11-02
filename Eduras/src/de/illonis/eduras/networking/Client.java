package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;

/**
 * A client that connects to the game server and starts receiving and sending
 * events.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Client {

	private Socket socket;

	private final GameLogicInterface logic;

	private NetworkEventListener networkEventListener;

	private ClientSender sender;

	private int ownerId;

	/**
	 * Creates a new Client.
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
		System.out.println("[CLIENT] Connecting...");
		socket = new Socket();
		InetSocketAddress iaddr = new InetSocketAddress(addr, port);
		socket.connect(iaddr, 10000);
		new ClientReceiver(logic, socket).setNetworkEventListener(networkEventListener);
		sender = new ClientSender(socket);
	}

	/**
	 * Sends a message to Server
	 * 
	 * @param message
	 *            message to send
	 */
	public void sendMessage(String message) {
		sender.sendMessage(message);

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

	public GameLogicInterface getLogic() {
		return logic;
	}

	public void setNetworkEventListener(NetworkEventListener listener) {
		this.networkEventListener = listener;
	}
}
