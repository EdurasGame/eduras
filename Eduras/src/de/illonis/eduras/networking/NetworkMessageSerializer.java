package de.illonis.eduras.networking;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MovementEvent;

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
	 * @throws Exception
	 *             Occurs if there is no serialization for the given event.
	 */
	public static String serialize(GameEvent event) throws Exception {

		String serializedEvent = "";

		serializedEvent += event.getType() + "#";

		if (event instanceof MovementEvent) {

			MovementEvent moveEvent = (MovementEvent) event;
			serializedEvent += moveEvent.getObjectId();
			serializedEvent += "#" + moveEvent.getNewXPos() + "#"
					+ moveEvent.getNewYPos();
			return "##" + serializedEvent;
		}

		throw new UnsupportedOperationException(
				"There does not exist a serialization for the given event yet!");

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
