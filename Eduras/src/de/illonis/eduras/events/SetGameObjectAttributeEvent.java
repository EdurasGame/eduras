package de.illonis.eduras.events;

public abstract class SetGameObjectAttributeEvent<T> extends ObjectEvent {

	private final T newValue;

	public SetGameObjectAttributeEvent(GameEventNumber type, int objectId,
			T newValue) {
		super(type, objectId);
		this.newValue = newValue;
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
