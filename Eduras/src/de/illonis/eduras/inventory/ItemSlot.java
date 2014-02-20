package de.illonis.eduras.inventory;

import de.illonis.eduras.items.Item;

/**
 * Represents a slot in inventory.
 * 
 * @author illonis
 * 
 */
public class ItemSlot {

	private Item item = null;
	private int slot;

	ItemSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * Checks if slot holds an item.
	 * 
	 * @return true if slot holds an item, false otherwise.
	 */
	boolean hasItem() {
		return item != null;
	}

	/**
	 * Returns item in this slot.
	 * 
	 * @return item in this slot.
	 * @throws ItemSlotIsEmptyException
	 *             when slot is empty.
	 */
	Item getItem() throws ItemSlotIsEmptyException {
		if (item == null)
			throw new ItemSlotIsEmptyException(slot);
		return item;
	}

	/**
	 * Puts an item into this slot. Note that this method overwrites any
	 * existing item.
	 * 
	 * @param newItem
	 *            item to put into.
	 */
	void putItem(Item newItem) {
		this.item = newItem;
	}

	/**
	 * Removes item in slot.
	 */
	void removeItem() {
		item = null;
	}

}