package de.illonis.eduras.events;

/**
 * An event that is involved with a gameobject specified by id.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class ObjectEvent extends GameEvent {

	private int objectId;

	/**
	 * Creates a new ObjectEvent belonging to the object identified by the given
	 * id.
	 * 
	 * @param type
	 *            the event type.
	 * 
	 * @param id
	 *            Id of the associated object.
	 */
	public ObjectEvent(GameEventNumber type, int id) {
		super(type);
		this.objectId = id;
	}

	/**
	 * Returns the id of the object this event belongs to.
	 * 
	 * @return Returns the id of the GameObject the event is associated with.
	 */
	public int getObjectId() {
		return objectId;
	}
}
