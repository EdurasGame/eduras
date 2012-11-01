/**
 * 
 */
package de.illonis.eduras.events;

/**
 * This event is thrown if a connection from the client could successfully be
 * thrown.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ConnectionEstablishedEvent extends NetworkEvent {

	private final int clientId;

	/**
	 * Creates a new ConnectionEstablishedEvent. The given number specifies the
	 * id that was assigned to the client which connected successfully.
	 * 
	 * @param id
	 *            The clientId of the successfully connected client.
	 */
	public ConnectionEstablishedEvent(int id) {
		super(NetworkEventNumber.CONNECTION_ESTABLISHED);
		this.clientId = id;
	}

	/**
	 * Returns the id of the client which connected successfully.
	 * 
	 * @return The id of the client.
	 */
	public int getClientId() {
		return clientId;
	}

}
