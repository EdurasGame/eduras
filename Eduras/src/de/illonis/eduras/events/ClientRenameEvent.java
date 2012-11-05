package de.illonis.eduras.events;

public class ClientRenameEvent extends OwnerGameEvent {
	private String name;

	public ClientRenameEvent(int owner, String newName) {
		super(GameEventNumber.CLIENT_SETNAME, owner);

		this.name = newName;
	}

	public String getName() {
		return name;
	}

}
