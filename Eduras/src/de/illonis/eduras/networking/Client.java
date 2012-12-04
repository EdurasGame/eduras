package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.logger.EduLog;

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
		EduLog.info("[CLIENT] Connecting to " + addr.toString() + " at " + port);
		socket = new Socket();
		InetSocketAddress iaddr = new InetSocketAddress(addr, port);
		socket.connect(iaddr, 10000);
		ClientReceiver r = new ClientReceiver(logic, socket, this);
		r.setNetworkEventListener(networkEventListener);
		r.start();
		sender = new ClientSender(socket);

		// createEchoSocket();
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

	public void disconnect() {
		logic.onShutdown();
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
				EduLog.passException(e);
			}
	}
}
