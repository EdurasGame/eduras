package de.illonis.eduras.events;

/**
 * Sets ammunition of a weapon to a value. Used to indicate a refill to client.
 * 
 * @author illonis
 * 
 */
public class SetAmmunitionEvent extends ObjectEvent {

	private final int newValue;

	/**
	 * @param objectId
	 *            the object id of item changed.
	 * @param newValue
	 *            the new amount.
	 */
	public SetAmmunitionEvent(int objectId, int newValue) {
		super(GameEventNumber.SET_AMMU, objectId);
		this.newValue = newValue;
		putArgument(newValue);
	}

	/**
	 * @return the new ammunition amount.
	 */
	public int getNewValue() {
		return newValue;
	}

}
