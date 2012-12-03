package de.illonis.eduras.inventory;

import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.StackableItem;

public class Inventory {

	public final static int MAX_CAPACITY = 6;

	private int gold;
	private ItemSlot[] itemSlots;

	public Inventory() {
		itemSlots = new ItemSlot[MAX_CAPACITY];
		for (int i = 0; i < MAX_CAPACITY; i++) {
			ItemSlot is = new ItemSlot();
			itemSlots[i] = is;
		}
	}

	public int getGold() {
		return gold;
	}

	public int getNumItems() {
		int n = 0;
		for (int i = 0; i < MAX_CAPACITY; i++) {
			if (itemSlots[i].hasItem())
				n++;
		}
		return n;
	}

	public synchronized void addGold(int amount) {
		gold += amount;
	}

	public void buy(Item item) {

	}

	/**
	 * Sells item in given inventory slot and adds gained money to player gold.
	 * If selling item is stackable, only one item of stack is selled.
	 * 
	 * @param itemSlot
	 *            itemslot to sell.
	 * @throws ItemSlotIsEmptyException
	 *             if itemslot is empty.
	 */
	public synchronized void sell(int itemSlot) throws ItemSlotIsEmptyException {
		Item item = itemSlots[itemSlot].getItem();
		int price = item.getSellValue();
		gold += price;
		if (item.stacks()) {
			StackableItem si = (StackableItem) item;
			si.takeFromStack();
			if (si.isEmpty())
				itemSlots[itemSlot].removeItem();
		} else {
			itemSlots[itemSlot].removeItem();
		}
	}

	/**
	 * Switch itemplace of two items. Item positions must be between 0
	 * (inclusive) and {@value #MAX_CAPACITY} (exclusive) to be valid. Switching
	 * empty itemplaces is allowed.
	 * 
	 * @param itemSlot1
	 *            position of first item.
	 * @param itemSlot2
	 *            position of second item.
	 */
	public synchronized void switchItems(int itemSlot1, int itemSlot2) {
		if (itemSlot1 < 0 || itemSlot1 >= MAX_CAPACITY || itemSlot2 < 0
				|| itemSlot2 >= MAX_CAPACITY)
			throw new IllegalArgumentException(
					"Inventory slot position is out of range.");

		Item i = null, i2 = null;
		try {
			i = itemSlots[itemSlot1].getItem();
			i2 = itemSlots[itemSlot2].getItem();
		} catch (ItemSlotIsEmptyException e) {
			// switching with empty slots is allowed.
		}

		itemSlots[itemSlot1].putItem(i2);
		itemSlots[itemSlot2].putItem(i);
	}
}