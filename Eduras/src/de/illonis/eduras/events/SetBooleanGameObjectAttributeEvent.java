package de.illonis.eduras.events;

/**
 * A wrapper class for the SET_VISIBLE and SET_COLLIDABLE events.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetBooleanGameObjectAttributeEvent extends
		SetGameObjectAttributeEvent<Boolean> {

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
	public SetBooleanGameObjectAttributeEvent(GameEventNumber type,
			int objectId, boolean val) {
		super(type, objectId, val);
	}

}