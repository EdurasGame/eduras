package de.illonis.eduras.events;

/**
 * Indicates that a user moved.
 * 
 * @author illonis
 * 
 */
public class UserMovementEvent extends OwnerGameEvent {

	/**
	 * Creates a new movement event.
	 * 
	 * @param type
	 *            the event type.
	 * @param owner
	 *            the owner id of moved user.
	 */
	public UserMovementEvent(GameEventNumber type, int owner) {
		super(type, owner);
	}

}
