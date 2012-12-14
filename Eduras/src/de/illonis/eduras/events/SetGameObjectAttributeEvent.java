/**
 * 
 */
package de.illonis.eduras.events;

/**
 * A wrapper class for the SET_VISIBLE and SET_COLLIDABLE events.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetGameObjectAttributeEvent extends ObjectEvent {

	private final boolean newValue;

	/**
	 * Create a new SetGameObjectAttribute event with the given values.
	 * 
	 * @param type
	 *            The event type.
	 * @param objectId
	 *            The id of the object whose attribute shall be changed.
	 * @param val
	 *            The new value of the attribute.
	 */
	public SetGameObjectAttributeEvent(GameEventNumber type, int objectId,
			boolean val) {
		super(type, objectId);

		newValue = val;

	}

	/**
	 * Returns the new value of the attribute.
	 * 
	 * @return The new value.
	 */
	public boolean getNewValue() {
		return newValue;
	}

}
