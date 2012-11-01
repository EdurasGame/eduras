package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.InetAddress;
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
	 * Connect to a server at given address using default port (
	 * {@link Server#DEFAULT_PORT}).
	 * 
	 * @param addr
	 *            server-address
	 */
	public void connect(InetAddress addr) {
		connect(addr, Server.DEFAULT_PORT);
	}

	/**
	 * Connects to a server on the given address and port.
	 * 
	 * @param addr
	 *            The server's address.
	 * @param port
	 *            The server's port.
	 */
	public void connect(InetAddress addr, int port) {
		try {
			System.out.println("[CLIENT] Connecting...");
			socket = new Socket(addr, port);
			new ClientReceiver(logic, socket).setNetworkEventListener(networkEventListener);
			sender = new ClientSender(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	public void setOwnerId(int userId) {
		this.ownerId = userId;
	}

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
