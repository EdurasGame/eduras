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
	 * {@inheritDoc}
	 * 
	 * @param type
	 * @param objectId
	 * @param newValue
	 */
	public SetIntegerGameObjectAttributeEvent(GameEventNumber type,
			int objectId, Integer newValue) {
		super(type, objectId, newValue);
	}

}
