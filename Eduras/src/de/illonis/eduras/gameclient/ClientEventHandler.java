package de.illonis.eduras.gameclient;

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

	private NetworkEventReactor reactor;

	public ClientEventHandler(NetworkEventReactor reactor) {
		this.reactor = reactor;
	}

	@Override
	public void onNetworkEventAppeared(NetworkEvent event) {
		switch (event.getType()) {
		case CONNECTION_ABORTED:
			reactor.onConnectionLost();
			break;
		case CONNECTION_ESTABLISHED:
			reactor.onConnected();
			break;
		case GAME_READY:
			reactor.onGameReady();
			break;
		case NO_EVENT:
			break;
		default:
			break;
		}

	}
}