package de.illonis.eduras.events;

import javax.naming.InvalidNameException;

public class ClientRenameEvent extends OwnerGameEvent {
	private final String name;

	public ClientRenameEvent(int owner, String newName)
			throws InvalidNameException {
		super(GameEventNumber.CLIENT_SETNAME, owner);

		if (newName == null) {
			throw new InvalidNameException("The name was null!");
		}

		this.name = newName;
	}

	public String getName() {
		return name;
	}

}
