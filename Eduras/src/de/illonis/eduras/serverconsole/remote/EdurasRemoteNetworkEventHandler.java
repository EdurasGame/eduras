package de.illonis.eduras.serverconsole.remote;

import de.eduras.remote.DefaultRemoteServerNetworkEventHandler;

/**
 * Handles management of client connections for {@link RemoteConsoleServer}.
 * 
 * @author illonis
 * 
 */
public class EdurasRemoteNetworkEventHandler extends
		DefaultRemoteServerNetworkEventHandler {
	private final RemoteConsoleServer server;

	/**
	 * @param server
	 *            the remote server.
	 */
	public EdurasRemoteNetworkEventHandler(RemoteConsoleServer server) {
		this.server = server;
	}

	@Override
	public void onClientConnected(int clientId) {
		super.onClientConnected(clientId);
		server.addClient(clientId);
	}

	@Override
	public void onClientDisconnected(int clientId) {
		super.onClientDisconnected(clientId);
		server.removeClient(clientId);

	};

}
