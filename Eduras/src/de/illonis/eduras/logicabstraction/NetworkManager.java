/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import java.net.InetAddress;

import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.networking.Client;

/**
 * This class provides a connection between the GUI and the network.
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 *
 */
public class NetworkManager {
	
	Client client;
	
	/**
	 * Creates a new NetworkManager that can connect the given client.
	 * @param client
	 */
	NetworkManager(GameLogicInterface logic) {
		client = new Client(logic);
	}
	
	public void connect(InetAddress addr, int port) {
		client.connect(addr, port);
	}
	
	public void connectToDefault(InetAddress addr) {
		client.connect(addr);
	}
	
	Client getClient() {
		return client;
	}

}
