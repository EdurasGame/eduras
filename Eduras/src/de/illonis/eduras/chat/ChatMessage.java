package de.illonis.eduras.chat;

/**
 * This class wraps a message posted by a user in a certain room.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ChatMessage {

	private final ChatUser postingUser;

	private final ChatRoom roomPostedIn;

	private final String message;

	ChatMessage(ChatUser user, ChatRoom room, String message) {
		postingUser = user;
		roomPostedIn = room;
		this.message = message;
	}

	/**
	 * Returns the user who posted the message.
	 * 
	 * @return the user
	 */
	public ChatUser getPostingUser() {
		return postingUser;
	}

	/**
	 * Returns the room in which the message is posted.
	 * 
	 * @return the room
	 */
	public ChatRoom getRoomPostedIn() {
		return roomPostedIn;
	}

	/**
	 * Returns the message that was posted.
	 * 
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}

}
