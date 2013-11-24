package de.illonis.eduras.chat;

public class UserNotInRoomException extends Exception {

	public UserNotInRoomException(ChatUser user, ChatRoom chatRoom) {
		super("Cannot send message of user " + user.getNickName() + " in room "
				+ chatRoom.getName() + " because he is not in this room");
	}

	public UserNotInRoomException() {
		super("Cannot find user in the given set of rooms.");
	}
}
