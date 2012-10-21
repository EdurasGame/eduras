package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;
import de.illonis.eduras.exceptions.InvalidMessageFormatException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;

/**
 * Deserializes different NetworkMessages.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkMessageDeserializer {

	/**
	 * (jme) Deserializes a string into a list of GameEvents.
	 * 
	 * @param eventString
	 *            The string to be deserialized.
	 * @return A list of deserialized GameEvents.<br>
	 *         <b>Note:</b> This function is more or less able to identify
	 *         invalid strings. If a string is rated to be invalid, the function
	 *         proceeds deserializing from the next hashtag and omits all
	 *         characters up to the hash.
	 */
	public static LinkedList<GameEvent> deserialize(String eventString) {
		LinkedList<GameEvent> gameEvents = new LinkedList<GameEvent>();
		System.out.println("[DESERIALIZE] orig: " + eventString);
		String[] messages = eventString.substring(2).split("##");

		for (String msg : messages) {
			try {
				GameEvent ge = deserializeMessage(msg);
				gameEvents.add(ge);
			} catch (InvalidMessageFormatException e) {
				e.printStackTrace();
			} catch (GivenParametersDoNotFitToEventException e) {
				e.printStackTrace();
			} catch (MessageNotSupportedException e) {
				e.printStackTrace();
			}
		}

		return gameEvents;
	}

	/**
	 * (jme) Deserializes given single message and returns a {@link GameEvent}
	 * representing it.<br>
	 * See {@link #deserialize(String)}.
	 * 
	 * @param msg
	 *            Message to deserialized.
	 * @return A GameEvent describing serialized message.
	 * @throws InvalidMessageFormatException
	 *             Thrown if message has an invalid format. Especially when it
	 *             has no or too less arguments.
	 * @throws GivenParametersDoNotFitToEventException
	 *             Thrown if generation of gameevent failed.
	 * @throws MessageNotSupportedException
	 *             Thrown if retrieved message is not supported.
	 * 
	 */
	private static GameEvent deserializeMessage(String msg)
			throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException,
			MessageNotSupportedException {
		if (msg.isEmpty())
			throw new InvalidMessageFormatException(
					"Message is empty (length 0)", msg);
		String[] args = msg.split("#");
		if (args.length < 2)
			throw new InvalidMessageFormatException(
					"Message has not enough arguments (less than two).", msg);

		// try to extract event type
		int typeInt;
		try {
			typeInt = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			throw new InvalidMessageFormatException(
					"Event id of message is no valid integer value: " + args[0],
					msg);
		}

		GameEventNumber typeNumber = GameEvent.toGameEventNumber(typeInt);
		GameEvent event = GameEvent.gameEventNumberToGameEvent(typeNumber);

		switch (typeNumber) {
		case SET_POS:
			event = handleMovementPositionEvent(msg, args, typeNumber);
			break;
		case MOVE_DOWN_PRESSED:
		case MOVE_RIGHT_PRESSED:
		case MOVE_LEFT_PRESSED:
		case MOVE_UP_PRESSED:
			event = handleStartMovementEvent(msg, args, typeNumber);
			break;
		default:
			throw new MessageNotSupportedException(typeNumber, msg);
		}

		return event;
	}

	private static GameEvent handleStartMovementEvent(String msg,
			String[] args, GameEventNumber typeNumber)
			throws GivenParametersDoNotFitToEventException {

		int owner = Integer.parseInt(args[1]);
		if (owner < 0)
			throw new GivenParametersDoNotFitToEventException(typeNumber, args);
		return new UserMovementEvent(typeNumber, owner);
	}

	// TODO: fix and enhance javadoc
	/**
	 * (jme) Handles a position event.
	 * 
	 * @param fullMessage
	 *            Full message
	 * @param splittedMessage
	 *            Message split at #.
	 * @param typeNumber
	 *            typenumber of gameevent.
	 * @throws InvalidMessageFormatException
	 *             Thrown if message has an invalid format. Especially when it
	 *             has no or too less arguments.
	 * @throws GivenParametersDoNotFitToEventException
	 *             Thrown if generation of gameevent failed.
	 */
	private static GameEvent handleMovementPositionEvent(String fullMessage,
			String[] splittedMessage, GameEventNumber typeNumber)
			throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException {
		if (splittedMessage.length != 4)
			throw new InvalidMessageFormatException(
					"Invalid number of arguments: " + splittedMessage.length
							+ " instead of 4.", fullMessage);

		try {
			int id = Integer.parseInt(splittedMessage[1]);
			int newXPos = Integer.parseInt(splittedMessage[2]);
			int newYPos = Integer.parseInt(splittedMessage[3]);
			MovementEvent moveEvent = new MovementEvent(typeNumber, id);
			moveEvent.setNewXPos(newXPos);
			moveEvent.setNewYPos(newYPos);
			return moveEvent;
		} catch (NumberFormatException e) {
			throw new GivenParametersDoNotFitToEventException(typeNumber,
					splittedMessage[1], splittedMessage[2], splittedMessage[3]);
		}
	}
}
