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

	ItemSlot() {

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
		return item;
	}

	/**
	 * Puts an item into this slot. Note that this method overwrites any
	 * existing item.
	 * 
	 * @param item
	 *            item to put into.
	 */
	void putItem(Item item) {
		this.item = item;
	}

	/**
	 * Removes item in slot.
	 */
	void removeItem() {
		item = null;
	}

}