/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import java.io.IOException;
import java.net.InetAddress;

import de.eduras.eventingserver.Client;
import de.eduras.eventingserver.ClientInterface;
import de.eduras.eventingserver.ClientNetworkEventHandler;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.interfaces.GameLogicInterface;

/**
 * This class provides a connection between the GUI and the network.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkManager {
	private ClientInterface client;
	private ClientNetworkEventHandler edurasGUINetworkHandler;
	private GameInformation gameInfo;

	/**
	 * Creates a new NetworkManager with the given logic.
	 * 
	 * @param logic
	 *            The logic the client shall use.
	 * 
	 */
	NetworkManager(GameLogicInterface logic) {

		client = new Client();
		gameInfo = logic.getGame();
		client.setNetworkEventHandler(new ClientNetworkEventHandler() {

			@Override
			public void onClientDisconnected(int clientId) {
				gameInfo.removePlayer(clientId);
			}

			@Override
			public void onClientConnected(int clientId) {
				edurasGUINetworkHandler.onClientConnected(clientId);

			}

			@Override
			public void onConnectionLost() {
				edurasGUINetworkHandler.onConnectionLost();
			}

			@Override
			public void onDisconnected() {
				edurasGUINetworkHandler.onDisconnected();
			}

			@Override
			public void onClientKicked(int clientId, String reason) {
				edurasGUINetworkHandler.onClientKicked(clientId, reason);
			}

			@Override
			public void onServerIsFull() {
				edurasGUINetworkHandler.onServerIsFull();
			}

			@Override
			public void onPingReceived(long latency) {
				edurasGUINetworkHandler.onPingReceived(latency);
			}

			@Override
			public void onConnectionEstablished(int clientId) {
				edurasGUINetworkHandler.onConnectionEstablished(clientId);
			}

		});
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

		// TODO: The IOException should be thrown in the connect method of
		// EventingServer.
		if (!client.connect(addr.getHostAddress(), port)) {
			throw new IOException("Cannot connect to server "
					+ addr.getHostAddress() + ":" + port);
		}

	}

	ClientInterface getClient() {
		return client;
	}

	/**
	 * @return the address of connected server.
	 */
	public InetAddress getServerAddress() {
		return client.getServerAddress();
	}

	/**
	 * @return the port of the server this client is connected to.
	 */
	public int getPort() {
		return client.getRemotePortNumber();
	}

	/**
	 * Returns the client's id.
	 * 
	 * @return The client's id.
	 */
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
		edurasGUINetworkHandler = handler;
	}

	/**
	 * Notifies network manager that connection to server has been closed.
	 * Network manager will notify all other components, then.
	 * 
	 * 
	 * @author illonis
	 */
	public void disconnect() {
		client.disconnect();
	}

	/**
	 * Indicates whether the client is currently connected to a server.
	 * 
	 * @return true, if the client is connected, false otherwise.
	 */
	public boolean isConnected() {
		return client.isConnected();
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

	/**
	 * pings the server.
	 */
	public void ping() {
		client.ping();
	}
}
