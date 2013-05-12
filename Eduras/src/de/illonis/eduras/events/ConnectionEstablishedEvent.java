/**
 * 
 */
package de.illonis.eduras.events;

/**
 * This event is thrown if a connection from the client could successfully be
 * established.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ConnectionEstablishedEvent extends NetworkEvent {

	/**
	 * Creates a new ConnectionEstablishedEvent. The given number specifies the
	 * id that was assigned to the client which connected successfully.
	 * 
	 * @param clientId
	 *            The clientId of the successfully connected client.
	 */
	public ConnectionEstablishedEvent(int clientId) {
		super(NetworkEventNumber.CONNECTION_ESTABLISHED, clientId);
	}

}
