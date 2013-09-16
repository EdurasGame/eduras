package de.illonis.eduras.events;

/**
 * Event that can be used for all the event types, that do not require further
 * information than the type and the client.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkEventImpl extends NetworkEvent {

	/**
	 * Create a NetworkEventImpl.
	 * 
	 * @param type
	 *            The event's type.
	 * @param client
	 *            The id of the client this id belongs to.
	 */
	public NetworkEventImpl(NetworkEventNumber type, int client) {
		super(type, client);
	}

}
