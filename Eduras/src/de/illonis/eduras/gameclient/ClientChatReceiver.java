package de.illonis.eduras.gameclient;

import java.util.LinkedList;

import de.illonis.eduras.chat.ChatActivityListener;
import de.illonis.eduras.chat.ChatClient;
import de.illonis.eduras.chat.ChatMessage;
import de.illonis.eduras.chat.ChatRoom;
import de.illonis.eduras.chat.ChatUser;
import de.illonis.eduras.chat.Invitation;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;

public class ClientChatReceiver implements ChatActivityListener {
	private final GamePanelLogic panelLogic;
	private final ChatClient chat;

	public ClientChatReceiver(GamePanelLogic panelLogic, ChatClient chat) {
		this.panelLogic = panelLogic;
		this.chat = chat;
	}

	@Override
	public void onNewMessage(ChatMessage message) {
		System.out.println("new chatmessage: ["
				+ message.getPostingUser().getNickName() + "] "
				+ message.getMessage());
		// TODO Auto-generated method stub

	}

	@Override
	public void onNameChanged(ChatUser user) {
		System.out.println("name changed: " + user.getNickName());

	}

	@Override
	public void onInviteReceived(Invitation invitation) {
		System.out.println("Received invitation from "
				+ invitation.getInvitingUser() + " to "
				+ invitation.getInvitedUser());
		// TODO Auto-generated method stub

	}

	@Override
	public void onYouJoined(ChatRoom chatRoom) {
		System.out.println("joined " + chatRoom.getName());
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserJoinedRoom(ChatRoom chatRoom, ChatUser user) {
		System.out
				.println(user.getNickName() + " joined " + chatRoom.getName());
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionEstablished() {
		System.out.println("chat established");
		LinkedList<ChatRoom> rooms = new LinkedList<ChatRoom>(chat.getRooms());
		// TODO: Join room.

	}

	@Override
	public void onConnectionAborted() {
		System.out.println("chat aborted");
		// TODO Auto-generated method stub

	}

}
