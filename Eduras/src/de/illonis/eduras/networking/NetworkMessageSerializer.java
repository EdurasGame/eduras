package de.illonis.eduras.networking;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.UserMovementEvent;

/**
 * Serializes different NetworkMessages.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkMessageSerializer {

	/**
	 * Serializes a GameEvent into a String
	 * 
	 * @param event
	 *            The event to be serialized.
	 * @return The string.
	 * @throws UnsupportedOperationException
	 *             Occurs if there is no serialization for the given event.
	 */
	public static String serialize(GameEvent event)
			throws UnsupportedOperationException {

		String serializedEvent = "";

		serializedEvent += event.getType().getNumber() + "#";

		switch (event.getType()) {
		case DEATH:
			break;
		case MOVE_DOWN_PRESSED:
		case MOVE_DOWN_RELEASED:
		case MOVE_LEFT_PRESSED:
		case MOVE_LEFT_RELEASED:
		case MOVE_RIGHT_PRESSED:
		case MOVE_RIGHT_RELEASED:
		case MOVE_UP_PRESSED:
		case MOVE_UP_RELEASED:
			serializedEvent += ((UserMovementEvent) event).getOwner() + "";
			break;
		case NO_EVENT:
			break;
		case OBJECT_CREATE:
			break;
		case OBJECT_REMOVE:
			break;
		case SETHEALTH:
			break;
		case SETSPEED:
			break;
		case SETSPEEDVECTOR:
			break;
		case SET_POS:
			MovementEvent moveEvent = (MovementEvent) event;
			serializedEvent += moveEvent.getObjectId();
			serializedEvent += "#" + moveEvent.getNewXPos() + "#"
					+ moveEvent.getNewYPos();
			break;
		case SHOOT_PRESSED:
			break;
		case SHOOT_RELEASED:
			break;
		default:
			break;
		}
		if (serializedEvent.endsWith("#"))
			throw new UnsupportedOperationException(
					"There does not exist a serialization for the given event yet!");
		return "##" + serializedEvent;

	}

	/**
	 * Concatenates given strings.
	 * 
	 * @param messages
	 *            Messages to concatenate
	 * @return concatenated messages
	 */
	public static String concatenate(String... messages) {
		StringBuilder b = new StringBuilder(messages[0]);
		for (int i = 1; i < messages.length; i++) {
			b.append(messages[i]);
		}
		return b.toString();
	}

}
