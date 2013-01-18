package de.illonis.eduras.events;

import de.illonis.eduras.networking.ServerClient.ClientRole;

/**
 * This class wraps information that have to be sent to the server on connection
 * initialization.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class InitInformationEvent extends NetworkEvent {

	private ClientRole role;
	private String name;

	/**
	 * Creates a new InitInformationEvent with the clientrole info and the
	 * client's name.
	 * 
	 * @param role
	 *            The role.
	 * @param name
	 *            The name.
	 */
	public InitInformationEvent(ClientRole role, String name) {
		super(NetworkEventNumber.INIT_INFORMATION);

		this.role = role;
		this.name = name;

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

}
