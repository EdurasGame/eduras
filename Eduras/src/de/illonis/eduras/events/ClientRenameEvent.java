package de.illonis.eduras.events;

import de.illonis.eduras.exceptions.InvalidNameException;

/**
 * Indicates that a client has changed or initially set his name.
 * 
 * @author illonis
 * 
 */
public class ClientRenameEvent extends OwnerGameEvent {
	private final String name;

	/**
	 * Creates a new rename event.
	 * 
	 * @param owner
	 *            the owner whose name changed.
	 * @param newName
	 *            the new name.
	 * @throws InvalidNameException
	 *             if new name is invalid.
	 */
	public ClientRenameEvent(int owner, String newName)
			throws InvalidNameException {
		super(GameEventNumber.CLIENT_SETNAME, owner);

		if (newName == null) {
			throw new InvalidNameException("The name was null!");
		}

		this.name = newName;
		putArgument(newName);
	}

	/**
	 * 
	 * @return the name of the client.
	 * 
	 * @author illonis
	 */
	public String getName() {
		return name;
	}

}
