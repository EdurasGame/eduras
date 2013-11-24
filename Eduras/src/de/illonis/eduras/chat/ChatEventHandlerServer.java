package de.illonis.eduras.chat;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.ServerInterface;

class ChatEventHandlerServer implements EventHandler {

	ServerInterface server;

	public ChatEventHandlerServer(ServerInterface server) {
		this.server = server;
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.getEventNumber()) {
		case Chat.SEND_MESSAGE:
			break;
		case Chat.SET_NAME:
			break;
		case Chat.CREATE_ROOM:
			break;
		case Chat.JOIN_ROOM:

			break;
		case Chat.INVITE_USER:

			break;
		case Chat.ACCEPT_INVITE:
			break;
		}
	}
}