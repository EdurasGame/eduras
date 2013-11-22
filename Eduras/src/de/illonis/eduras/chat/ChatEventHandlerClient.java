package de.illonis.eduras.chat;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;

class ChatEventHandlerClient implements EventHandler {

	private ChatClientImpl chatClient;

	public ChatEventHandlerClient(ChatClientImpl client) {
		this.chatClient = client;
	}

	@Override
	public void handleEvent(Event event) {

		switch (event.getEventNumber()) {

		case Chat.NEW_MESSAGE:
			break;
		case Chat.NAME_CHANGED:
			break;
		case Chat.INVITE_TO_ROOM:

			break;
		case Chat.CONFIRM_ROOM_CREATE:

			break;
		case Chat.CONFIRM_ROOM_JOIN:

			break;
		case Chat.GIVE_USER_INFORMATION:

			break;
		case Chat.GIVE_ROOM_INFORMATION:

			break;
		case Chat.SHOW_ROOMS:

			break;
		case Chat.SHOW_USERS:

			break;
		case Chat.USER_JOINED_ROOM:

			break;
		case Chat.USER_LEFT_ROOM:

			break;
		}

	}
}
