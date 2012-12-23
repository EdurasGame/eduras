package de.illonis.eduras.events;

/**
 * Sets a single integer attribute on a gameobject.
 * 
 * @author illonis
 * 
 */
public class SetIntegerGameObjectAttributeEvent extends
		SetGameObjectAttributeEvent<Integer> {

	/**
	 * Creates a new attribute events that sets an integer value.
	 * 
	 * @param type
	 *            event type.
	 * @param objectId
	 *            object that's attribute is changed.
	 * @param newValue
	 *            attribute's new value.
	 */
	public SetIntegerGameObjectAttributeEvent(GameEventNumber type,
			int objectId, Integer newValue) {
		super(type, objectId, newValue);
	}

}
