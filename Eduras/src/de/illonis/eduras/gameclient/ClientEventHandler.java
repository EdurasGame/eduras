package de.illonis.eduras.gameclient;

import de.illonis.eduras.events.ConnectionAbortedEvent;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.ConnectionQuitEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.interfaces.NetworkEventListener;

/**
 * A network event handler for gui. It distinguishes between different network
 * events and reacts.
 * 
 * @author illonis
 * 
 */
public class ClientEventHandler implements NetworkEventListener {

	private final NetworkEventReactor reactor;

	/**
	 * Creates a new client event handler.
	 * 
	 * @param reactor
	 *            the event reactor.
	 */
	public ClientEventHandler(NetworkEventReactor reactor) {
		this.reactor = reactor;
	}

	@Override
	public void onNetworkEventAppeared(NetworkEvent event) {
		switch (event.getType()) {
		case CONNECTION_ABORTED:
			ConnectionAbortedEvent ae = (ConnectionAbortedEvent) event;
			reactor.onConnectionLost(ae.getClient());
			break;
		case CONNECTION_ESTABLISHED:
			ConnectionEstablishedEvent ce = (ConnectionEstablishedEvent) event;
			reactor.onConnected(ce.getClient());
			break;
		case GAME_READY:
			reactor.onGameReady();
			break;
		case CONNECTION_QUIT:
			ConnectionQuitEvent qe = (ConnectionQuitEvent) event;
			reactor.onDisconnect(qe.getClient());
			break;
		case NO_EVENT:
			break;
		case UDP_READY:
			reactor.onUDPReady(event.getClient());
			break;
		default:
			break;
		}

	}
}