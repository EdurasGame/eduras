package de.illonis.eduras.gameclient;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.chat.ChatActivityListener;
import de.illonis.eduras.chat.ChatClient;
import de.illonis.eduras.chat.ChatMessage;
import de.illonis.eduras.chat.ChatRoom;
import de.illonis.eduras.chat.ChatUser;
import de.illonis.eduras.chat.Invitation;
import de.illonis.eduras.chat.NotConnectedException;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;

/**
 * Handles incoming chat events on client side.
 * 
 * @author illonis
 * 
 */
public class ClientChatReceiver implements ChatActivityListener {

	private final static Logger L = EduLog
			.getLoggerFor(ClientChatReceiver.class.getName());

	private final GamePanelLogic panelLogic;
	private final ChatCache cache;
	private final ChatClient chat;
	private final String clientName;

	ClientChatReceiver(GamePanelLogic panelLogic, ChatClient chat,
			String clientName, ChatCache cache) {
		this.panelLogic = panelLogic;
		this.chat = chat;
		this.cache = cache;
		this.clientName = clientName;
	}

	@Override
	public void onNewMessage(ChatMessage message) {
		cache.pushMessage(message);
	}

	@Override
	public void onNameChanged(ChatUser user) {
		cache.pushUser(user);
		L.info("Chat name changed: " + user.getNickName());
	}

	@Override
	public void onInviteReceived(Invitation invitation) {
		L.info("Received invitation from " + invitation.getInvitingUser()
				+ " to " + invitation.getInvitedUser());
	}

	@Override
	public void onYouJoined(ChatRoom chatRoom) {
		cache.setCurrentRoom(chatRoom);
		cache.pushSystemMessage("You joined " + chatRoom.getName());
	}

	@Override
	public void onUserJoinedRoom(ChatRoom chatRoom, ChatUser user) {
		cache.pushUser(user);
		cache.pushSystemMessage("User joined: " + user.getNickName(), chatRoom);
	}

	@Override
	public void onConnectionEstablished() {
		try {
			chat.setName(clientName);
		} catch (NotConnectedException e) {
			L.log(Level.SEVERE, "Could not set my chat name.", e);
		}
		cache.pushSystemMessage("Chat established.");

		cache.setSelf(chat.getUser());
		LinkedList<ChatRoom> rooms = new LinkedList<ChatRoom>(chat.getRooms());
		for (ChatRoom chatRoom : rooms) {
			cache.pushRoom(chatRoom);
		}
		// TODO: Join room (how?)
	}

	@Override
	public void onConnectionAborted() {
		cache.pushSystemMessage("Chat connection terminated.");
	}
}
