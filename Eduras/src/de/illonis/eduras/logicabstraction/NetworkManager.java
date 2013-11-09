/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import java.io.IOException;
import java.net.InetAddress;

import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.networking.Client;

/**
 * This class provides a connection between the GUI and the network.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkManager {

	private final Client client;
	private boolean connected;

	/**
	 * Creates a new NetworkManager with the given logic.
	 * 
	 * @param logic
	 *            The logic the client shall use.
	 * 
	 */
	NetworkManager(GameLogicInterface logic) {
		connected = false;
		client = new Client(logic);
	}

	/**
	 * Connects to given server.
	 * 
	 * @param addr
	 *            server address.
	 * @param port
	 *            server port.
	 * @throws IOException
	 *             when connection establishing or initialization failed.
	 */
	public void connect(InetAddress addr, int port) throws IOException {
		client.connect(addr, port);
		connected = true;
	}

	Client getClient() {
		return client;
	}

	/**
	 * Determines where NetworkEvents are forwarded to.
	 * 
	 * @param listener
	 *            The listener to forward NetworkEvents to.
	 */
	public void setNetworkEventListener(NetworkEventListener listener) {
		client.setNetworkEventListener(listener);
	}

	/**
	 * Notifies network manager that connection to server has been closed.
	 * Network manager will notify all other components, then.
	 * 
	 * 
	 * @author illonis
	 */
	public void disconnect() {
		connected = false;
		client.disconnect();
	}

	public boolean isConnected() {
		return connected;
	}

	/**
	 * Notifies network manager that connection to server closed gracefully.
	 * Network manager will notify all other components then.
	 * 
	 * 
	 * @author illonis
	 */
	public void notifyConnectionClosed() {

	}
}
