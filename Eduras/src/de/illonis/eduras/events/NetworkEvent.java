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
	private final int client;

	/**
	 * The network event number identifying the type of the event.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum NetworkEventNumber {
		CONNECTION_ESTABLISHED(201), CONNECTION_ABORTED(202), INIT_INFORMATION(
				203), GAME_READY(204), CONNECTION_QUIT(205), NO_EVENT(299), UDP_HI(
				206), UDP_READY(207);

		int number = 299;

		/**
		 * Creates a new NetworkEventNumber with the given number.
		 * 
		 * @param num
		 *            The number.
		 * 
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
	 * @param client
	 *            The client on that event appeared.
	 */
	public NetworkEvent(NetworkEventNumber type, int client) {
		this.type = type;
		this.client = client;
	}

	/**
	 * Returns the client event occured on.
	 * 
	 * @return affected client.
	 * 
	 * @author illonis
	 */
	public int getClient() {
		return client;
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
	 * Maps a number to its NetworkEventNumber representation. Returns NO_EVENT
	 * if the number cannot be mapped to a NetworkEventNumber.<br>
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
