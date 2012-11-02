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
	private int ownerId;

	/**
	 * Creates a new ObjectFactoryEvent with given parameters.
	 * 
	 * @param eventType
	 *            typenumber of event.
	 * @param objectType
	 *            type of object (see {@link ObjectType}). Irrelevant if event
	 *            is {@link GameEventNumber#OBJECT_REMOVE}.
	 */
	public ObjectFactoryEvent(GameEventNumber eventType, ObjectType objectType) {
		super(eventType);
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

	/**
	 * Sets the id of the related object.
	 * 
	 * @param id
	 *            The new id of the related object.
	 */
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
	 * Returns the ownerId of the related object.
	 * 
	 * @return the ownerId
	 */
	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * Sets the ownerId of the related object.
	 * 
	 * @param ownerId
	 *            the ownerId to set.
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
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
