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

	Client client;

	/**
	 * Creates a new NetworkManager with the given logic.
	 * 
	 * @param logic
	 *            The logic the client shall use.
	 * 
	 */
	NetworkManager(GameLogicInterface logic) {
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

}
