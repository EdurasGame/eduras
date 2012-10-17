package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;

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
	public static LinkedList<GameEvent> deserialize(String eventString) {

		LinkedList<GameEvent> gameEvents = new LinkedList<GameEvent>();

		String restString = eventString;
		int i = 0;
		while (!restString.isEmpty()) {
			System.out.println("DESERIALIZING " + restString);
			i++;
			if (i > 30)
				break;
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

					int newXPos = Integer.parseInt(restString.substring(0,
							nextHash));

					restString = restString.substring(nextHash + 1);
					nextHash = restString.indexOf("#");

					int newYPos = Integer.parseInt(restString.substring(0,
							nextHash));

					MovementEvent moveEvent = (MovementEvent) event;
					moveEvent.setNewXPos(newXPos);
					moveEvent.setNewYPos(newYPos);
				}

			} else {
				restString = restString.substring(1);
			}

		}

		return gameEvents;
	}
}
