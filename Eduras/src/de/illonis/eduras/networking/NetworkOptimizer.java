package de.illonis.eduras.networking;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.logger.EduLog;

/**
 * Provides several tools to reduce network traffic.
 * 
 * @author illonis
 * 
 */
public abstract class NetworkOptimizer {

	/**
	 * Removes obsolete messages from a message array.<br>
	 * For example, obsolete messages are multiple SET_POS events on a single
	 * gameobject.<br>
	 * This is done by replacing old events with new events of the same type.
	 * 
	 * @param unfilteredMessages
	 *            message array to filter.
	 * @return a message array with obsolete messages removed.
	 */
	public static String[] filterObsoleteMessages(String[] unfilteredMessages) {
		if (unfilteredMessages.length < 2)
			return unfilteredMessages;
		LinkedList<String> unfilterable = new LinkedList<String>();

		HashMap<Integer, String> posEvents = new HashMap<Integer, String>();
		HashMap<Integer, String> speedEvents = new HashMap<Integer, String>();
		HashMap<Integer, String> speedvectorEvents = new HashMap<Integer, String>();

		for (String string : unfilteredMessages) {

			if (string == null || string.isEmpty())
				continue;

			GameEventNumber n = NetworkMessageDeserializer
					.extractGameEventNumber(string);
			int key;
			try {
				key = Integer.parseInt(NetworkMessageDeserializer
						.getArgumentFromMessage(string, 1));
			} catch (NumberFormatException e) {
				continue;
			}
			switch (n) {
			case SET_POS_UDP:
				posEvents.put(key, string);
				break;
			case SETSPEED:
				speedEvents.put(key, string);
				break;
			case SETSPEEDVECTOR:
				speedvectorEvents.put(key, string);
				break;
			case NO_EVENT:
				continue;
			default:
				unfilterable.add(string);
				break;
			}
		}
		unfilterable.addAll(posEvents.values());
		unfilterable.addAll(speedEvents.values());
		unfilterable.addAll(speedvectorEvents.values());
		int n = unfilteredMessages.length - unfilterable.size();
		EduLog.info("[NETWORKOPTIMIZER] Filtered " + n
				+ " obsolete messages (of " + unfilterable.size() + ")");
		return unfilterable.toArray(new String[0]);
	}
}
