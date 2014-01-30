package de.illonis.eduras.events;

/**
 * A game event that additionally holds an owner-id. If no ownerid is set, it's
 * -1.
 * 
 * @author illonis
 * 
 */
public class OwnerGameEvent extends GameEvent {

	private final int owner;

	/**
	 * Creates a new Gameevent with given number and owner.
	 * 
	 * @param type
	 *            game event number.
	 * @param owner
	 *            ownerid.
	 */
	public OwnerGameEvent(GameEventNumber type, int owner) {
		super(type);
		this.owner = owner;
		putArgument(owner);
	}

	/**
	 * Returns the ownerId of the related object.
	 * 
	 * @return the ownerId
	 */
	public final int getOwner() {
		return owner;
	}
}
