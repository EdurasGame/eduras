package de.illonis.eduras.events;

import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * Indicates that the usage of an item failed.
 * 
 * @author illonis
 * 
 */
public class ItemUseFailedEvent extends OwnerGameEvent {
	private final ObjectType itemType;
	private final Reason reason;

	/**
	 * The reason for failing.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum Reason {
		AMMO_EMPTY, INVALID_TARGET, UNKNOWN;
	}

	/**
	 * @param owner
	 *            the owner.
	 * @param itemType
	 *            the type of item used.
	 * @param reason
	 *            the reason for failing.
	 */
	public ItemUseFailedEvent(int owner, ObjectType itemType, Reason reason) {
		super(GameEventNumber.ITEM_USE_FAILED, owner);
		this.itemType = itemType;
		this.reason = reason;
		putArgument(itemType);
		putArgument(reason.name());
	}

	/**
	 * @return the type of item affected.
	 */
	public ObjectType getItemType() {
		return itemType;
	}

	/**
	 * @return the reason for failing.
	 */
	public Reason getReason() {
		return reason;
	}
}
