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

	/**
	 * Creates a new itemslot event.
	 * 
	 * @param objectId
	 *            the id of the affected item.
	 * @param owner
	 *            the owner of the slot.
	 * @param itemSlot
	 *            the affected item slot.
	 */
	public SetItemSlotEvent(int objectId, int owner, int itemSlot) {
		super(GameEventNumber.SET_ITEM_SLOT, objectId);
		this.owner = owner;
		this.itemSlot = itemSlot;
		putArgument(owner);
		putArgument(itemSlot);
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
