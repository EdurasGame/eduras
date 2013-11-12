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

	private int objectId = -1;

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
	}

	/**
	 * Returns the id of the object to set the new owner of.
	 * 
	 * @return The object.
	 */
	public int getObjectId() {
		return objectId;
	}

	@Override
	public Object getArgument(int i) {
		switch (i) {
		case 0:
			return getOwner();
		default:
			return objectId;
		}
	}

	@Override
	public int getNumberOfArguments() {
		return 2;
	}
}
