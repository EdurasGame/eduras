package de.illonis.eduras.events;

/**
 * This event is triggered if a client quit his connection to server.
 * 
 * @author illonis
 * 
 */
public class ConnectionQuitEvent extends NetworkEvent {

	/**
	 * Creates a new ConnectionQuitEvent indicating that the connection to the
	 * server is/should be closed.
	 * 
	 * @param clientId
	 *            The id to the client whose connection has been quit.
	 */
	public ConnectionQuitEvent(int clientId) {
		super(NetworkEventNumber.CONNECTION_QUIT, clientId);
	}
}
