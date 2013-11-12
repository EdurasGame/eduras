/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import java.io.IOException;
import java.net.InetAddress;

import de.eduras.eventingserver.Client;
import de.eduras.eventingserver.ClientInterface;
import de.eduras.eventingserver.ClientNetworkEventHandler;
import de.illonis.eduras.interfaces.GameLogicInterface;

/**
 * This class provides a connection between the GUI and the network.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkManager {

	ClientInterface client;

	/**
	 * Creates a new NetworkManager with the given logic.
	 * 
	 * @param logic
	 *            The logic the client shall use.
	 * 
	 */
	NetworkManager(GameLogicInterface logic) {
		client = new Client();
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
		client.connect(addr.getHostAddress(), port);
	}

	ClientInterface getClient() {
		return client;
	}

	public int getClientId() {
		return client.getClientId();
	}

	/**
	 * Determines where NetworkEvents are forwarded to.
	 * 
	 * @param handler
	 *            The listener to forward NetworkEvents to.
	 */
	public void setNetworkEventHandler(ClientNetworkEventHandler handler) {
		client.setNetworkEventHandler(handler);
	}

	/**
	 * Notifies network manager that connection to server has been closed.
	 * Network manager will notify all other components, then.
	 * 
	 * 
	 * @author illonis
	 */
	public void notifyDisconnect() {
		client.disconnect();
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
