package de.illonis.eduras.events;

public class ClientRenameEvent extends OwnerGameEvent {
	private final String name;

	public ClientRenameEvent(int owner, String newName) throws Exception {
		super(GameEventNumber.CLIENT_SETNAME, owner);

		if (newName == null) {
			throw new Exception("The name was null!");
		}

		this.name = newName;
	}

	public String getName() {
		return name;
	}

}
