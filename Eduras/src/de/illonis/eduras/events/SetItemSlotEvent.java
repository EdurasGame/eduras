package de.illonis.eduras.events;

/**
 * Indicates a change of a specific item slot on a player.
 * 
 * @author illonis
 * 
 */
public class SetItemSlotEvent extends ObjectEvent {

	private int owner;
	private int itemSlot;

	public SetItemSlotEvent(int objectId, int owner, int itemSlot) {
		super(GameEventNumber.ITEM_SLOT_CHANGED, objectId);
		this.owner = owner;
		this.itemSlot = itemSlot;
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
