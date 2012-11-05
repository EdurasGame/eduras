package de.illonis.eduras.events;

public class ClientRenameEvent extends OwnerGameEvent {
	private String name;

	public ClientRenameEvent(GameEventNumber type, int owner, String newName) {
		super(type, owner);
		
		this.name = newName;
	}
	
	public String getName() {
		return name;
	}

}
