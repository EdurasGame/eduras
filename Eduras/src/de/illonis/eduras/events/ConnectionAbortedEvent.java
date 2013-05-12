/**
 * 
 */
package de.illonis.eduras.events;

/**
 * This event is triggered if the connection to a specific user closed.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ConnectionAbortedEvent extends NetworkEvent {

	/**
	 * Creates a new ConnectionAbortedEvent indicating that the connection to
	 * the client with the given id was closed.
	 * 
	 * @param clientId
	 *            The id to the client whose connection got closed.
	 */
	public ConnectionAbortedEvent(int clientId) {
		super(NetworkEventNumber.CONNECTION_ABORTED, clientId);
	}
}
