package de.illonis.eduras.inventory;

import de.illonis.eduras.items.Item;

public class ItemSlot {

	private Item item = null;

	ItemSlot() {

	}

	boolean hasItem() {
		return item != null;
	}

	Item getItem() throws ItemSlotIsEmptyException {
		return item;
	}

	void putItem(Item item) {
		this.item = item;
	}

	void removeItem() {
		item = null;
	}

}