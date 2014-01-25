package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;
import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * A <code>ObjectFactoryEvent</code> indicates an event on an object. This can
 * be object creation or deletion. If sent from server, this event has an id,
 * otherwise id is -1.
 * 
 * @author illonis
 * 
 */
public class ObjectFactoryEvent extends OwnerGameEvent {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	private ObjectType objectType;
	private int id;

	/**
	 * Creates a new ObjectFactoryEvent with given parameters.
	 * 
	 * @param eventType
	 *            typenumber of event.
	 * @param objectType
	 *            type of object (see {@link ObjectType}). Irrelevant if event
	 *            is
	 *            {@link de.illonis.eduras.events.GameEvent.GameEventNumber#OBJECT_REMOVE}
	 *            .
	 * @param ownerId
	 *            the owner of the object.
	 */
	public ObjectFactoryEvent(GameEventNumber eventType, ObjectType objectType,
			int ownerId) {
		super(eventType, ownerId);
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
	 * {@link de.illonis.eduras.events.GameEvent.GameEventNumber#OBJECT_REMOVE}.
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
	 * Checks whether event has an object-id attached or not.
	 * 
	 * @see #getId()
	 * 
	 * @return true if event has an object-id attached, false otherwise.
	 */
	public boolean hasId() {
		return id >= 0;
	}

	@Override
	public Object getArgument(int i) throws TooFewArgumentsExceptions {
		switch (getType()) {
		case OBJECT_CREATE:
			switch (i) {
			case 0:
				return getId();
			case 1:
				return getOwner();
			case 2:
				return objectType.getNumber();
			default:
				throw new TooFewArgumentsExceptions(i, 2);
			}
		case OBJECT_REMOVE:
			switch (i) {
			case 0:
				return getId();
			default:
				throw new TooFewArgumentsExceptions(i, 0);
			}
		default:
			L.warning("There is an invalid event in ObjectFactoryEvent: "
					+ getType().getNumber());
			return null;
		}
	}

	@Override
	public int getNumberOfArguments() {
		switch (getType()) {
		case OBJECT_CREATE:
			return 3;
		case OBJECT_REMOVE:
			return 1;
		default:
			return 0;
		}
	}
}
