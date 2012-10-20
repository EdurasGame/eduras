package de.illonis.eduras.events;

/**
 * A <code>ObjectFactoryEvent</code> indicates an event on an object. This can
 * be object creation or deletion. If sent from server, this event has an id,
 * otherwise id is -1.
 * 
 * @author illonis
 * 
 */
public class ObjectFactoryEvent extends GameEvent {

	public enum ObjectType {
		PLAYER(1), YELLOWCIRCLE(2);

		private int number;

		ObjectType(int num) {
			number = num;
		}

		public int getNumber() {
			return number;
		}
	}

	public enum ObjectAction {
		DELETE(1), CREATE(2), DO_NOTHING(3);

		private int number;

		ObjectAction(int num) {
			number = num;
		}

		public int getNumber() {
			return number;
		}
	}

	private ObjectType objectType;
	private ObjectAction objectAction;
	private int id;

	public ObjectFactoryEvent(GameEventNumber eventType, ObjectType objectType,
			ObjectAction objectAction) {
		super(eventType);
		this.objectAction = objectAction;
		this.objectType = objectType;
		id = -1;
	}

	public ObjectFactoryEvent(GameEventNumber eventType, ObjectType objectType) {
		this(eventType, objectType, ObjectAction.DO_NOTHING);
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public ObjectAction getObjectAction() {
		return objectAction;
	}

	public void setObjectAction(ObjectAction objectAction) {
		this.objectAction = objectAction;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean hasId() {
		return id >= 0;
	}

}
