package de.illonis.eduras.chat;

public class Invitation {

	private final ChatRoom room;
	private final ChatUser invitingUser;
	private final ChatUser invitedUser;

	Invitation(ChatRoom room, ChatUser invitingUser, ChatUser invitedUser) {
		this.room = room;
		this.invitedUser = invitedUser;
		this.invitingUser = invitingUser;
	}

	public ChatRoom getRoom() {
		return room;
	}

	public ChatUser getInvitingUser() {
		return invitingUser;
	}

	public ChatUser getInvitedUser() {
		return invitedUser;
	}

}
