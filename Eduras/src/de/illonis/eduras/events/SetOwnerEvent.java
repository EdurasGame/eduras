/**
 * 
 */
package de.illonis.eduras.events;

/**
 * Set the owner of an object to a new value.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetOwnerEvent extends OwnerGameEvent {

	private final int objectId;

	/**
	 * Creates a new SetOwnerEvent. The owner of the object that belongs to the
	 * given 'objectId' is set to the value 'ownerId'.
	 * 
	 * @param ownerId
	 *            The new owner of the object.
	 * @param objectId
	 *            The object's id.
	 */
	public SetOwnerEvent(int ownerId, int objectId) {

		super(GameEventNumber.SET_OWNER, ownerId);
		this.objectId = objectId;
		putArgument(objectId);
	}

	/**
	 * Returns the id of the object to set the new owner of.
	 * 
	 * @return The object.
	 */
	public int getObjectId() {
		return objectId;
	}

}
