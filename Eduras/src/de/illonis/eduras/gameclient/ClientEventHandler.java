package de.illonis.eduras.gameclient;

import de.eduras.eventingserver.ClientNetworkEventHandler;

/**
 * A network event handler for gui. It distinguishes between different network
 * events and reacts.
 * 
 * @author illonis
 * 
 */
public class ClientEventHandler implements ClientNetworkEventHandler {

	private final GameClient reactor;

	/**
	 * Creates a new client event handler.
	 * 
	 * @param reactor
	 *            the event reactor.
	 */
	public ClientEventHandler(GameClient reactor) {
		this.reactor = reactor;
	}

	@Override
	public void onClientConnected(int id) {

		reactor.onConnected(id);

	}

	@Override
	public void onClientDisconnected(int id) {
		reactor.onDisconnect(id);
	}

	@Override
	public void onClientKicked(int id) {
		reactor.onDisconnect(id);

	}

	@Override
	public void onConnectionLost() {
		reactor.onConnectionLost(reactor.getOwnerID());

	}

	@Override
	public void onDisconnected() {
		reactor.onDisconnect(reactor.getOwnerID());

	}

	// @Override
	// public void onNetworkEventAppeared(NetworkEvent event) {
	// switch (event.getType()) {
	// case CONNECTION_ABORTED:
	// ConnectionAbortedEvent ae = (ConnectionAbortedEvent) event;
	// reactor.onConnectionLost(ae.getClient());
	// break;
	// case CONNECTION_ESTABLISHED:
	// ConnectionEstablishedEvent ce = (ConnectionEstablishedEvent) event;
	// reactor.onConnected(ce.getClient());
	// break;
	// case GAME_READY:
	// reactor.onGameReady();
	// break;
	// case CONNECTION_QUIT:
	// ConnectionQuitEvent qe = (ConnectionQuitEvent) event;
	// reactor.onDisconnect(qe.getClient());
	// break;
	// case NO_EVENT:
	// break;
	// case UDP_READY:
	// reactor.onUDPReady(event.getClient());
	// break;
	// default:
	// break;
	// }
	//
	// }
}