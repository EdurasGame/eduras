package de.illonis.eduras.events;

import de.illonis.eduras.gameobjects.GameObject;

/**
 * An event which indicates that a {@link GameObject}s double attribute has
 * changed.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetDoubleGameObjectAttributeEvent extends
		SetGameObjectAttributeEvent<Double> {

	/**
	 * Creates a new {@link SetDoubleGameObjectAttributeEvent}.
	 * 
	 * @param type
	 *            Denotes which event to create.
	 * @param objectId
	 *            The id of the game object which attribute is changed.
	 * @param newValue
	 *            The new value of the attribute.
	 */
	public SetDoubleGameObjectAttributeEvent(GameEventNumber type,
			int objectId, Double newValue) {
		super(type, objectId, newValue);

	}

}
