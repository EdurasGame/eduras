package de.illonis.eduras.events;

public class UserMovementEvent extends OwnerGameEvent {

	public UserMovementEvent(GameEventNumber type, int owner) {
		super(type, owner);
	}

}
