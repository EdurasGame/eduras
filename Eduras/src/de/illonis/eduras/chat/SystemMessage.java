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
		this(message, null);
	}

	/**
	 * @param message
	 *            the message string.
	 * @param room
	 *            the room this message belongs to.
	 */
	public SystemMessage(String message, ChatRoom room) {
		super(null, room, message);
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
