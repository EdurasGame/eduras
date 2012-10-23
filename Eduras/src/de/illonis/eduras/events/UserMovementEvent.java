package de.illonis.eduras.events;

public class UserMovementEvent extends GameEvent {

	private int owner;

	public UserMovementEvent(GameEventNumber type, int owner) {
		super(type);
		this.owner = owner;
	}

	public int getOwner() {
		return owner;
	}

}
