package de.illonis.eduras.events;

/**
 * A general event class for setting a single attribute on a game object.
 * 
 * @author illonis
 * 
 * @param <T>
 *            attribute type.
 */
public abstract class SetGameObjectAttributeEvent<T> extends ObjectEvent {

	private final T newValue;

	/**
	 * Creates a new attribute setter event.
	 * 
	 * @param type
	 *            event type.
	 * @param objectId
	 *            object that is affected.
	 * @param newValue
	 *            attribute's new value.
	 */
	protected SetGameObjectAttributeEvent(GameEventNumber type, int objectId,
			T newValue) {
		super(type, objectId);
		this.newValue = newValue;

		putArgument(newValue);
	}

	/**
	 * Returns the new value of the attribute.
	 * 
	 * @return The new value.
	 */
	public final T getNewValue() {
		return newValue;
	}

}
