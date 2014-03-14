package de.illonis.eduras.ai.movement;

/**
 * Indicates that a unit is not controllable in the way it has been used.
 * 
 * @author illonis
 * 
 */
public class UnitNotControllableException extends Exception {

	private static final long serialVersionUID = 1L;
	private final int objectId;

	/**
	 * @param objectId
	 *            the object id of the object.
	 */
	public UnitNotControllableException(int objectId) {
		super("Object with id " + objectId + " is not controllable.");
		this.objectId = objectId;
	}

	/**
	 * @return the object id of the unit that failed to be controlled.
	 */
	public int getObjectId() {
		return objectId;
	}

}
