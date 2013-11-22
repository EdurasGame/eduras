package de.illonis.eduras.chat;

public class ChatMessage {

	private final ChatUser postingUser;

	private final ChatRoom roomPostedIn;

	private final String message;

	ChatMessage(ChatUser user, ChatRoom room, String message) {
		postingUser = user;
		roomPostedIn = room;
		this.message = message;
	}

	public ChatUser getPostingUser() {
		return postingUser;
	}

	public ChatRoom getRoomPostedIn() {
		return roomPostedIn;
	}

	public String getMessage() {
		return message;
	}

}
