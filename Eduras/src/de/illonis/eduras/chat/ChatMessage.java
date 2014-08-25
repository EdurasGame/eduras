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

	private final long timeStamp;

	ChatMessage(ChatUser user, ChatRoom room, String message, long timeStamp) {
		postingUser = user;
		roomPostedIn = room;
		this.message = message;
		this.timeStamp = timeStamp;
	}

	/**
	 * Returns the time stamp of this chat message.
	 * 
	 * @return timestamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Returns the user who posted the message.
	 * 
	 * @return the user
	 */
	public final ChatUser getPostingUser() {
		return postingUser;
	}

	/**
	 * Returns the room in which the message is posted.
	 * 
	 * @return the room
	 */
	public final ChatRoom getRoomPostedIn() {
		return roomPostedIn;
	}

	/**
	 * Returns the message that was posted.
	 * 
	 * @return The message
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * @return a string representation of this message containing name of
	 *         sending user.
	 */
	public String toChatWindowString() {
		return getPostingUser().getNickName() + ": " + getMessage();
	}

	/**
	 * @return true if this is a system message, false otherwise.
	 */
	public boolean isSystemMessage() {
		return false;
	}
}
