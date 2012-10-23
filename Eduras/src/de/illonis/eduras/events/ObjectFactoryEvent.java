package de.illonis.eduras.events;

import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * A <code>ObjectFactoryEvent</code> indicates an event on an object. This can
 * be object creation or deletion. If sent from server, this event has an id,
 * otherwise id is -1.
 * 
 * @author illonis
 * 
 */
public class ObjectFactoryEvent extends GameEvent {

	private ObjectType objectType;
	private int id;
	private int owner;

	/**
	 * Creates a new ObjectFactoryEvent with given parameters.
	 * 
	 * @param eventType
	 *            typenumber of event.
	 * @param objectType
	 *            type of object (see {@link ObjectType}). Irrelevant if event
	 *            is {@link GameEventNumber#OBJECT_REMOVE}.
	 * @param owner
	 *            owner of created object
	 */
	public ObjectFactoryEvent(GameEventNumber eventType, ObjectType objectType, int owner) {
		super(eventType);
		this.owner = owner;
		this.objectType = objectType;
		id = -1;
	}

	/**
	 * Returns {@link ObjectType} of event.
	 * 
	 * @return object type of event.
	 */
	public ObjectType getObjectType() {
		return objectType;
	}

	/**
	 * Sets object type of this event. An object of this type will be created or
	 * deleted with this event.<br>
	 * <b>Note:</b> Object type is neglected if type of event is
	 * {@link GameEventNumber#OBJECT_REMOVE}.
	 * 
	 * @see #getType()
	 * 
	 * @param objectType
	 */
	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns id of object that is affected by this event. If this is sent from
	 * client, there is no object id yet (<code>-1</code>).
	 * 
	 * @see #hasId()
	 * 
	 * @return id of affected object.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Checks whether event has an object-id attached or not.
	 * 
	 * @see #getId()
	 * 
	 * @return true if event has an object-id attached, false otherwise.
	 */
	public boolean hasId() {
		return id >= 0;
	}

}