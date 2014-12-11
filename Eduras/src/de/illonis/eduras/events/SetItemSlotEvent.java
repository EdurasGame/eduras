package de.illonis.eduras.events;

/**
 * Indicates a change of a specific item slot on a player.
 * 
 * @author illonis
 * 
 */
public class SetItemSlotEvent extends ObjectEvent {

	private final int owner;
	private final int itemSlot;
	private final boolean isNew;

	/**
	 * Creates a new itemslot event.
	 * 
	 * @param objectId
	 *            the id of the affected item.
	 * @param owner
	 *            the owner of the slot.
	 * @param itemSlot
	 *            the affected item slot.
	 * @param isNew
	 *            true if this item was newly looted.
	 */
	public SetItemSlotEvent(int objectId, int owner, int itemSlot, boolean isNew) {
		super(GameEventNumber.SET_ITEM_SLOT, objectId);
		this.owner = owner;
		this.itemSlot = itemSlot;
		this.isNew = isNew;
		putArgument(owner);
		putArgument(itemSlot);
		putArgument(isNew);
	}

	/**
	 * Creates a new itemslot event for an item already in inventory.
	 * 
	 * @param objectId
	 *            the id of the affected item.
	 * @param owner
	 *            the owner of the slot.
	 * @param itemSlot
	 *            the affected item slot.
	 */
	public SetItemSlotEvent(int objectId, int owner, int itemSlot) {
		this(objectId, owner, itemSlot, false);
	}

	/**
	 * @return true if item was recently looted.
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * Returns owner, that's inventory changed.
	 * 
	 * @return owner.
	 */
	public int getOwner() {
		return owner;
	}

	/**
	 * Returns affected item slot.
	 * 
	 * @return affected item slot.
	 */
	public int getItemSlot() {
		return itemSlot;
	}
}
