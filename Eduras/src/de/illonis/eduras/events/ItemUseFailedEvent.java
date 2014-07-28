package de.illonis.eduras.events;

/**
 * Indicates that the usage of an item failed.
 * 
 * @author illonis
 * 
 */
public class ItemUseFailedEvent extends OwnerGameEvent {
	private final int slot;
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
	 * @param slot
	 *            the item slot affected.
	 * @param reason
	 *            the reason for failing.
	 */
	public ItemUseFailedEvent(int owner, int slot, Reason reason) {
		super(GameEventNumber.ITEM_USE_FAILED, owner);
		this.slot = slot;
		this.reason = reason;
		putArgument(slot);
		putArgument(reason.name());
	}

	/**
	 * @return the item slot affected.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the reason for failing.
	 */
	public Reason getReason() {
		return reason;
	}
}
