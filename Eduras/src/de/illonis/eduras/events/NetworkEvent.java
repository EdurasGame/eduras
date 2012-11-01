/**
 * 
 */
package de.illonis.eduras.events;


/**
 * This serves as a super class for all events occuring on the network.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class NetworkEvent extends Event {

	private final NetworkEventNumber type;

	public enum NetworkEventNumber {
		CONNECTION_ESTABLISHED(201), CONNECTION_ABORTED(202), NO_EVENT(299);

		int number = 299;

		/**
		 * Creates a new NetworkEventNumber with the given number.
		 * 
		 * @param num
		 *            The number.
		 */
		NetworkEventNumber(int num) {
			this.number = num;
		}

		/**
		 * Returns the number of the NetworkEventType.
		 * 
		 * @return The number.
		 */
		public int getNumber() {
			return number;
		}
	}

	/**
	 * Creates a new NetworkEvent of the given type.
	 * 
	 * @param type
	 *            The type of the new NetworkEvent.
	 */
	public NetworkEvent(NetworkEventNumber type) {
		this.type = type;
	}

	/**
	 * Returns the type of the networkevent.
	 * 
	 * @return The type.
	 */
	public NetworkEventNumber getType() {
		return this.type;
	}
	
	/**
	 * Maps a number to its NetworkEventNumber representation. Returns NO_EVENT if
	 * the number cannot be mapped to a NetworkEventNumber.<br>
	 * 
	 * @param typeInt
	 *            The number to be mapped to a NetworkEventNumber.
	 * @return The NetworkEventNumber.
	 */
	public static NetworkEventNumber toNetworkEventNumber(int typeInt) {
		for (NetworkEventNumber evn : NetworkEventNumber.values())
			if (evn.getNumber() == typeInt)
				return evn;
		return NetworkEventNumber.NO_EVENT;
	}

}
