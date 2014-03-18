package de.illonis.eduras.gameclient;

import de.eduras.eventingserver.ClientNetworkEventHandler;

/**
 * A network event handler for gui. It distinguishes between different network
 * events and reacts.
 * 
 * @author illonis
 * 
 */
public class ClientNetworkEventListener implements ClientNetworkEventHandler {

	private final GameClient reactor;

	/**
	 * Creates a new client event handler.
	 * 
	 * @param reactor
	 *            the event reactor.
	 */
	public ClientNetworkEventListener(GameClient reactor) {
		this.reactor = reactor;
	}

	@Override
	public void onClientConnected(int id) {
		// do nothing, handled seperately
	}

	@Override
	public void onClientDisconnected(int id) {
		reactor.onClientDisconnect(id);
	}

	@Override
	public void onClientKicked(int id, String reason) {
		reactor.onClientKicked(id, reason);
	}

	@Override
	public void onConnectionLost() {
		reactor.onClientConnectionLost(reactor.getOwnerID());
	}

	@Override
	public void onDisconnected() {
		onClientDisconnected(reactor.getOwnerID());
	}

	@Override
	public void onServerIsFull() {
		reactor.onServerIsFull();
	}

	@Override
	public void onPingReceived(long latency) {
		reactor.setPing(latency);
	}

	@Override
	public void onConnectionEstablished(int clientId) {
		reactor.onConnectionEstablished();
	}
}
