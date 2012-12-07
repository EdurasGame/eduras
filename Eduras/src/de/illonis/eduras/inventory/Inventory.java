package de.illonis.eduras.inventory;

import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Item.ItemType;
import de.illonis.eduras.items.StackableItem;

/**
 * An inventory that holds items for a unit.
 * 
 * @author illonis
 * 
 */
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

	/**
	 * Returns gold amount hold in inventory.
	 * 
	 * @return current gold amount.
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * Spends given amount of money. If there is not enough money available, it
	 * doesn't spend it and returns false.
	 * 
	 * @param amount
	 *            amount to spend.
	 * @return true if money was spent, false otherwise.
	 */
	public boolean spendGold(int amount) {
		int newv = gold - amount;
		if (newv < 0)
			return false;
		else {
			gold = newv;
			return true;
		}
	}

	/**
	 * Returns number of items currently hold in inventory. Item stacks count as
	 * a single item.
	 * 
	 * @return number of items in inventory.
	 */
	public int getNumItems() {
		int n = 0;
		for (int i = 0; i < MAX_CAPACITY; i++) {
			if (itemSlots[i].hasItem())
				n++;
		}
		return n;
	}

	/**
	 * Adds given amount of money to inventory.
	 * 
	 * @param amount
	 *            amount to add.
	 */
	public synchronized void addGold(int amount) {
		gold += amount;
	}

	/**
	 * Buys an item and puts it into next free inventory slot.
	 * 
	 * @see #loot(Item)
	 * 
	 * @param item
	 *            item to buy.
	 * @throws InventoryIsFullException
	 *             when inventory is full.
	 */
	public synchronized void buy(Item item) throws InventoryIsFullException,
			NotEnoughMoneyException {

		if (gold >= item.getBuyValue()) {
			loot(item);
			spendGold(item.getBuyValue());

		} else {
			throw new NotEnoughMoneyException();
		}
	}

	/**
	 * Loots an item.
	 * 
	 * @param item
	 *            item to loot.
	 * @throws InventoryIsFullException
	 *             when inventory is full.
	 */
	public synchronized void loot(Item item) throws InventoryIsFullException {
		int target = findNextFreeInventorySlotForItem(item);
		if (target == -1)
			throw new InventoryIsFullException();
		itemSlots[target].putItem(item);
	}

	/**
	 * Returns first occurence of item of given {@link ItemType}. If there is no
	 * item of given type, -1 is returned.
	 * 
	 * @param type
	 *            item type to search for.
	 * @return index of item with given type or -1.
	 */
	private int getItemOfType(ItemType type) {
		return getItemOfTypeBetween(type, 0, MAX_CAPACITY);
	}

	/**
	 * Checks if given item exists in inventory. This method does not check if
	 * exactly this item is hold in inventory but only its type.
	 * 
	 * @see #hasItemOfType(ItemType)
	 * 
	 * @param item
	 *            item to check for.
	 * @return true if item exists, false otherwise.
	 */
	public boolean hasItem(Item item) {
		return hasItemOfType(item.getType());

	}

	/**
	 * Checks if an item of given {@link ItemType} exists in inventory.
	 * 
	 * @see #hasItem(Item)
	 * 
	 * @param itemType
	 *            itemtype to look for.
	 * @return true if an item of this type exists, false otherwise.
	 */
	public boolean hasItemOfType(ItemType itemType) {
		int pos = getItemOfType(itemType);
		return pos != -1;
	}

	/**
	 * Returns first occurence of item of given {@link ItemType} within given
	 * range. If there is no item of given type, -1 is returned.
	 * 
	 * @param type
	 *            item type to search for.
	 * @param from
	 *            lower index.
	 * @param to
	 *            upper index.
	 * @return index of item with given type within given range or -1.
	 */
	private int getItemOfTypeBetween(ItemType type, int from, int to) {
		for (int i = from; i < to; i++) {
			try {
				if (itemSlots[i].getItem().getType().equals(type))
					return i;
			} catch (ItemSlotIsEmptyException e) {
			}
		}
		return -1;
	}

	/**
	 * Finds next free item slot for given item. If no free slot was found, -1
	 * is returned. This method also considers stackable items, so it prefers to
	 * stack up stackable items first.
	 * 
	 * @param item
	 *            item to find a slot for.
	 * @return index of free slot or -1.
	 */
	private synchronized int findNextFreeInventorySlotForItem(Item item) {

		int targetPos = -1;

		if (item.stacks()) {
			int left = 0;
			do {
				targetPos = getItemOfTypeBetween(item.getType(), left,
						MAX_CAPACITY);
				if (targetPos >= 0) {
					try {
						if (((StackableItem) itemSlots[targetPos].getItem())
								.isFull()) {
							left++;
						} else {
							break;
						}
					} catch (ItemSlotIsEmptyException e) {
						e.printStackTrace();
					}
				}

			} while (left < MAX_CAPACITY);
		} else {
			targetPos = findNextFreeInventorySlot();
		}
		return targetPos;
	}

	/**
	 * Returns item of given item slot.
	 * 
	 * @param slot
	 *            item's slot.
	 * @return item in given slot.
	 * @throws ItemSlotIsEmptyException
	 *             when slot is empty.
	 */
	public Item getItemBySlot(int slot) throws ItemSlotIsEmptyException {
		if (itemSlots[slot].hasItem()) {
			return itemSlots[slot].getItem();
		}
		throw new ItemSlotIsEmptyException();
	}

	/**
	 * Returns first free inventory slot available. If no slot is free, it
	 * returns -1.
	 * 
	 * @return first free inventory slot or -1.
	 */
	private int findNextFreeInventorySlot() {
		for (int i = 0; i < MAX_CAPACITY; i++) {
			if (!itemSlots[i].hasItem())
				return i;
		}
		return -1;
	}

	/**
	 * Sells the item in given inventory slot and adds gained money to player
	 * gold. If selling item is stackable, only one item of stack is sold.
	 * 
	 * @param itemSlot
	 *            itemslot to sell.
	 * @throws ItemSlotIsEmptyException
	 *             if itemslot is empty.
	 */
	public synchronized void sell(int itemSlot) throws ItemSlotIsEmptyException {
		Item item = itemSlots[itemSlot].getItem();
		int price = item.getSellValue();
		addGold(price);
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