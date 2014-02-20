package de.illonis.eduras.serverconsole.remote;

import de.eduras.remote.DefaultRemoteServerNetworkEventHandler;

public class EdurasRemoteNetworkEventHandler extends
		DefaultRemoteServerNetworkEventHandler {
	private final RemoteConsoleServer server;

	public EdurasRemoteNetworkEventHandler(RemoteConsoleServer server) {
		this.server = server;
	}

	@Override
	public void onClientConnected(int clientId) {
		super.onClientConnected(clientId);
		server.addClient(clientId);
	}

	public void onClientDisconnected(int clientId) {
		super.onClientDisconnected(clientId);
		server.removeClient(clientId);

	};

}
