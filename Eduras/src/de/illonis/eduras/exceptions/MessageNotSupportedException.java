package de.illonis.eduras.exceptions;

import de.illonis.eduras.events.GameEvent.GameEventNumber;

/**
 * Exception thrown when a received message is not supported because there does
 * not exist any logic interpretation for it.
 * 
 * @author illonis
 * 
 */
public class MessageNotSupportedException extends Exception {

	private static final long serialVersionUID = 1L;
	private GameEventNumber gameEventNumber;
	private String eventMessage;

	/**
	 * Creates a new MessageNotSupportedException.
	 * 
	 * @param gameEventNumber
	 *            {@link GameEventNumber} that is not supported.
	 * @param eventMessage
	 *            original message string.
	 */
	public MessageNotSupportedException(GameEventNumber gameEventNumber,
			String eventMessage) {
		super();
		this.gameEventNumber = gameEventNumber;
		this.eventMessage = eventMessage;
	}

	/**
	 * Returns original message string of unsupported message.
	 * 
	 * @return original message string.
	 */
	public String getEventMessage() {
		return eventMessage;
	}

	/**
	 * Returns {@link GameEventNumber} of message.
	 * 
	 * @return {@link GameEventNumber} of message.
	 */
	public GameEventNumber getGameEventNumber() {
		return gameEventNumber;
	}
}
