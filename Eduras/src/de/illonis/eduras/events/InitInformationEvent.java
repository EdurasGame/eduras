package de.illonis.eduras.events;

import de.illonis.eduras.networking.ClientRole;

/**
 * This class wraps information that have to be sent to the server on connection
 * initialization.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class InitInformationEvent extends GameEvent {

	private ClientRole role;
	private String name;
	private int clientId;

	/**
	 * Creates a new InitInformationEvent with the clientrole info and the
	 * client's name.
	 * 
	 * @param role
	 *            The role.
	 * @param name
	 *            The name.
	 * @param clientId
	 *            the id of the client.
	 */
	public InitInformationEvent(ClientRole role, String name, int clientId) {
		super(GameEventNumber.INIT_INFORMATION);

		this.clientId = clientId;
		this.role = role;
		this.name = name;

		putArgument(role.toString());
		putArgument(name);
		putArgument(clientId);

	}

	/**
	 * Returns the client's role.
	 * 
	 * @return the role.
	 */
	public ClientRole getRole() {
		return role;
	}

	/**
	 * Returns the client's name.
	 * 
	 * @return The client's name.
	 */
	public String getName() {
		return name;
	}

	public int getClientId() {
		return clientId;
	}

}
