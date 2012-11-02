package de.illonis.eduras.gui;

import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.interfaces.NetworkEventListener;

public class NetworkEventHandler implements NetworkEventListener {

	private Gui gui;

	public NetworkEventHandler(Gui gui) {
		this.gui = gui;
	}

	@Override
	public void onNetworkEventAppeared(NetworkEvent event) {
		switch (event.getType()) {
		case CONNECTION_ABORTED:
			break;
		case CONNECTION_ESTABLISHED:
			gui.onConnected();
		case NO_EVENT:
			break;
		default:
			break;
		}

	}
}