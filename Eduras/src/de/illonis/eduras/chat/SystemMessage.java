package de.illonis.eduras.chat;

/**
 * Message generated by system and not any user.
 * 
 * @author illonis
 * 
 */
public class SystemMessage extends ChatMessage {

	/**
	 * @param message
	 *            the message string.
	 */
	public SystemMessage(String message) {
		this(message, null, 0);
	}

	/**
	 * @param message
	 *            the message string.
	 * @param room
	 *            the room this message belongs to.
	 * @param timeStamp
	 *            this messages timestamp
	 */
	public SystemMessage(String message, ChatRoom room, long timeStamp) {
		super(null, room, message, timeStamp);
	}

	@Override
	public String toChatWindowString() {
		return "SYSTEM: " + getMessage();
	}

	@Override
	public boolean isSystemMessage() {
		return true;
	}
}
