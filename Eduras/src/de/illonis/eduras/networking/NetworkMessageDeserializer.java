package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;
import de.illonis.eduras.exceptions.InvalidMessageFormatException;

/**
 * Deserializes different NetworkMessages.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkMessageDeserializer {

	/**
	 * Deserializes a string into a list of GameEvents.
	 * 
	 * @param eventString
	 *            The string to be deserialized.
	 * @return A list of deserialized GameEvents.
	 * 
	 *         Note: This function is more or less able to identify invalid
	 *         strings. If a string is rated to be invalid, the function
	 *         proceeds deserializing from the next hashtag and omits all
	 *         characters up to the hash.
	 */
	@Deprecated
	public static LinkedList<GameEvent> oldDeserialize(String eventString) {

		LinkedList<GameEvent> gameEvents = new LinkedList<GameEvent>();

		String restString = eventString;
		while (!restString.isEmpty()) {
			System.out.println("[DESERIALIZING] " + restString);

			// find next message start
			if (restString.startsWith("##")) {

				restString = restString.substring(2);

				// try to extract eventtype
				int nextHash = restString.indexOf("#");
				String typeStr = restString.substring(0, nextHash);
				int typeInt;
				try {
					typeInt = Integer.parseInt(typeStr);
				} catch (NumberFormatException e) {
					restString = restString.substring(nextHash);
					continue;
				}

				GameEventNumber typeNumber = GameEvent
						.toGameEventNumber(typeInt);
				GameEvent event = null;
				try {
					event = GameEvent.gameEventNumberToGameEvent(typeNumber);
					gameEvents.add(event);
				} catch (GivenParametersDoNotFitToEventException e) {
					restString = restString.substring(nextHash);
					continue;
				}

				// if MOVE_POS event, extract new position

				restString = restString.substring(nextHash);
				nextHash = restString.indexOf("#");

				if (event instanceof MovementEvent
						&& typeNumber == GameEventNumber.MOVE_POS) {
					restString = restString.substring(1);
					nextHash = restString.indexOf("#");

					int id = Integer
							.parseInt(restString.substring(0, nextHash));

					restString = restString.substring(nextHash + 1);
					nextHash = restString.indexOf("#");

					int newXPos = Integer.parseInt(restString.substring(0,
							nextHash));

					restString = restString.substring(nextHash + 1);
					nextHash = restString.indexOf("#");

					if (nextHash < 0) {
						nextHash = restString.length();
					}

					int newYPos = Integer.parseInt(restString.substring(0,
							restString.length()));

					MovementEvent moveEvent = (MovementEvent) event;
					moveEvent.setNewXPos(newXPos);
					moveEvent.setNewYPos(newYPos);
					moveEvent.setId(id);
				}

			} else {
				restString = restString.substring(1);
			}
		}
		return gameEvents;
	}

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
				System.out.println("IM: " + e.getInvalidMessage());
				e.printStackTrace();
			} catch (GivenParametersDoNotFitToEventException e) {
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
	 */
	private static GameEvent deserializeMessage(String msg)
			throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException {
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

		if (event instanceof MovementEvent
				&& typeNumber == GameEventNumber.MOVE_POS) {
			handleMovementPositionEvent(msg, args, typeNumber, event);
		}
		return event;
	}

	/**
	 * (jme) Handles a position event.
	 * 
	 * @param fullMessage
	 *            Full message
	 * @param splittedMessage
	 *            Message split at #.
	 * @param typeNumber
	 *            typenumber of gameevent.
	 * @param event
	 *            event to manipulate.
	 * @throws InvalidMessageFormatException
	 *             Thrown if message has an invalid format. Especially when it
	 *             has no or too less arguments.
	 * @throws GivenParametersDoNotFitToEventException
	 *             Thrown if generation of gameevent failed.
	 */
	private static void handleMovementPositionEvent(String fullMessage,
			String[] splittedMessage, GameEventNumber typeNumber,
			GameEvent event) throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException {
		if (splittedMessage.length != 4)
			throw new InvalidMessageFormatException(
					"Invalid number of arguments: " + splittedMessage.length
							+ " instead of 4.", fullMessage);

		try {
			int id = Integer.parseInt(splittedMessage[1]);
			int newXPos = Integer.parseInt(splittedMessage[2]);
			int newYPos = Integer.parseInt(splittedMessage[3]);
			MovementEvent moveEvent = (MovementEvent) event;
			moveEvent.setNewXPos(newXPos);
			moveEvent.setNewYPos(newYPos);
			moveEvent.setId(id);
		} catch (NumberFormatException e) {
			throw new GivenParametersDoNotFitToEventException(typeNumber,
					splittedMessage[1], splittedMessage[2], splittedMessage[3]);
		}
	}
}
