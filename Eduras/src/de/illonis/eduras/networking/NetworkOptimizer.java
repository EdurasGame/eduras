package de.illonis.eduras.networking;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.events.GameEvent.GameEventNumber;

/**
 * Provides several tools to reduce network traffic.
 * 
 * @author illonis
 * 
 */
public final class NetworkOptimizer {

	private NetworkOptimizer() {
		// this makes nobody can instantiate this class.
	}

	/**
	 * Removes obsolete messages from a message array.<br>
	 * For example, obsolete messages are multiple SET_POS events on a single
	 * gameobject.<br>
	 * This is done by replacing old events with new events of the same type.
	 * 
	 * @param s
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
			GameEventNumber n = NetworkMessageDeserializer
					.extractGameEventNumber(string);
			int key = Integer.parseInt(NetworkMessageDeserializer
					.getArgumentFromMessage(string, 1));
			switch (n) {
			case SET_POS:
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
		System.out.println("[NETWORKOPTIMIZER] Filtered " + n
				+ " obsolete messages (of " + unfilterable.size() + ")");
		return (String[]) unfilterable.toArray(new String[0]);
	}
}
