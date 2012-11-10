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

	private final int clientId;

	/**
	 * Creates a new ConnectionAbortedEvent indicating that the connection to
	 * the client with the given id was closed.
	 * 
	 * @param clientId
	 *            The id to the client whose connection got closed.
	 */
	public ConnectionAbortedEvent(int clientId) {
		super(NetworkEventNumber.CONNECTION_ABORTED);
		this.clientId = clientId;
	}

	/**
	 * Returns the id of the client who disconnected.
	 * 
	 * @return The client id.
	 */
	public int getClientId() {
		return clientId;
	}

}
