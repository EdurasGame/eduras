package de.illonis.eduras.inventory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.StackableItem;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.settings.S;

/**
 * An inventory that holds items for a unit.
 * 
 * @author illonis
 * 
 */
public class Inventory {

	private final static Logger L = EduLog.getLoggerFor(Inventory.class
			.getName());

	/**
	 * Maximum number of items that can be stored.
	 */
	public final static int MAX_CAPACITY = S.Server.player_max_item_capacity;
	private final static ItemComparator itemSorter = new ItemComparator();

	private int gold;
	private final Item[] items;

	/**
	 * Creates an empty inventory.
	 */
	public Inventory() {
		items = new Item[MAX_CAPACITY];
	}

	/**
	 * Returns gold amount hold in inventory.
	 * 
	 * @return current gold amount.
	 */
	public int getCurrentMoney() {
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
	private boolean spendGold(int amount) {
		int newv = gold - amount;
		if (newv < 0)
			return false;
		else {
			gold = newv;
			return true;
		}
	}

	/**
	 * Sets given itemslot's item to given item. This should only be called if
	 * server sends this event.
	 * 
	 * @param slot
	 *            item slot.
	 * @param item
	 *            new item in slot.
	 */
	public synchronized void setItemAt(int slot, Item item) {
		items[slot] = item;
	}

	/**
	 * Adds given amount of income to the inventory's money.
	 * 
	 * @param amount
	 *            income amount.
	 * 
	 * @author illonis
	 */
	public synchronized void income(int amount) {
		if (amount <= 0)
			throw new IllegalArgumentException(
					"Amount must not be zero or negative (was " + amount + ").");
		gold += amount;
	}

	/**
	 * Returns number of items currently hold in inventory. Item stacks count as
	 * a single item.
	 * 
	 * @return number of items in inventory.
	 */
	public int getNumItems() {
		int n = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null)
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
	private synchronized void addGold(int amount) {
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
	 * @throws NotEnoughMoneyException
	 *             if user has not enough money to buy this item.
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
	 * Loots an item by putting it into inventory or increasing stack of already
	 * contained stackable item.
	 * 
	 * @param item
	 *            item to loot.
	 * @return slot where inventory was put into.
	 * @throws InventoryIsFullException
	 *             when inventory is full or inventory already contains an item
	 *             of given type and that is unique.
	 */
	public synchronized int loot(Item item) throws InventoryIsFullException {
		int slot = -1;
		try {
			slot = findItemSlotOfType(item.getType());
		} catch (NoSuchItemException e) {
		}
		if (!item.stacks() && item.isUnique() && slot >= 0) {
			throw new InventoryIsFullException();
		}
		// TODO: filter unique items and prevent double looting them.
		int target = findNextFreeInventorySlotForItem(item);
		if (target == -1)
			throw new InventoryIsFullException();
		L.info("putting item in " + target);
		items[target] = item;
		sort();
		try {
			int newSlot = findItemSlotOfType(item.getType());
			return newSlot;
		} catch (NoSuchItemException e) {
			// should not appear
			return -1;
		}
	}

	/**
	 * Returns first occurence of item of given {@link ObjectType}. If there is
	 * no item of given type, -1 is returned.
	 * 
	 * @param type
	 *            item type to search for.
	 * @throws NoSuchItemException
	 *             thrown if the inventory doesn't contain an item of the given
	 *             type
	 * @return index of item with given type or -1.
	 */
	public synchronized int findItemSlotOfType(ObjectType type)
			throws NoSuchItemException {
		int slotNumber = getItemslotOfTypeBetween(type, 0, MAX_CAPACITY);
		if (slotNumber == -1) {
			throw new NoSuchItemException(type);
		}
		return slotNumber;
	}

	/**
	 * Checks if given item exists in inventory. This method does not check if
	 * exactly this item is hold in inventory but only its type.
	 * 
	 * @see #hasItemOfType(de.illonis.eduras.ObjectFactory.ObjectType)
	 * 
	 * @param item
	 *            item to check for.
	 * @return true if item exists, false otherwise.
	 */
	public boolean hasItem(Item item) {
		return hasItemOfType(item.getType());
	}

	/**
	 * Checks if an item of given {@link ObjectType} exists in inventory.
	 * 
	 * @see #hasItem(Item)
	 * 
	 * @param itemType
	 *            itemtype to look for.
	 * @return true if an item of this type exists, false otherwise.
	 */
	public boolean hasItemOfType(ObjectType itemType) {
		try {
			findItemSlotOfType(itemType);
		} catch (NoSuchItemException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the item of given item type from inventory.
	 * 
	 * @param itemType
	 *            type of item to search.
	 * @return item of given type in inventory.
	 * @throws NoSuchItemException
	 *             when no item of given type is in inventory.
	 */
	public Item getItemOfType(ObjectType itemType) throws NoSuchItemException {
		int slot = findItemSlotOfType(itemType);
		if (slot >= 0) {
			return items[slot];
		} else
			throw new NoSuchItemException(itemType);
	}

	/**
	 * Returns the position in item index of item of given {@link ObjectType}
	 * within given range. If there is no item of given type, -1 is returned.
	 * 
	 * @param type
	 *            item type to search for.
	 * @param from
	 *            lower index (inclusive)
	 * @param to
	 *            upper index (exclusive)
	 * @return index of item with given type within given range or -1.
	 */
	private int getItemslotOfTypeBetween(ObjectType type, int from, int to) {
		for (int i = from; i < to; i++) {
			if (items[i] == null)
				continue;
			if (items[i].getType().equals(type))
				return i;
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
				targetPos = getItemslotOfTypeBetween(item.getType(), left,
						MAX_CAPACITY);
				if (targetPos >= 0) {
					if (((StackableItem) items[targetPos]).isFull()) {
						left++;
					} else {
						break;
					}
				}

			} while (left < MAX_CAPACITY);
		} else {
			targetPos = findNextFreeInventorySlot();
		}
		return targetPos;
	}

	/**
	 * Returns first free inventory slot available. If no slot is free, it
	 * returns -1.
	 * 
	 * @return first free inventory slot or -1.
	 */
	private int findNextFreeInventorySlot() {
		for (int i = 0; i < MAX_CAPACITY; i++) {
			if (items[i] == null)
				return i;
		}
		return -1;
	}

	/**
	 * Sells the item of given type from inventory and adds gained money to
	 * player gold. If selling item is stackable, only one item of stack is
	 * sold.
	 * 
	 * @param type
	 *            Object type of item to sell.
	 * @throws NoSuchItemException
	 *             if no item of given type is in inventory.
	 */
	public synchronized void sell(ObjectType type) throws NoSuchItemException {
		Item item = getItemOfType(type);
		int price = item.getSellValue();
		addGold(price);
		if (item.stacks()) {
			StackableItem si = (StackableItem) item;
			si.takeFromStack();
			if (si.isEmpty())
				trash(type);
		} else {
			trash(type);
		}
	}

	public void sort() {
		Arrays.sort(items, itemSorter);
	}

	/**
	 * Throws the item at given slot away.
	 * 
	 * @param type
	 *            the type of the item that should be trashed.
	 * 
	 * @return the item that was in that slot.
	 * @throws NoSuchItemException
	 *             if no item of given type is in inventory.
	 */
	public synchronized Item trash(ObjectType type) throws NoSuchItemException {
		int slot = getItemslotOfTypeBetween(type, 0, items.length);
		Item item = items[slot];
		items[slot] = null;
		sort();
		return item;
	}

	private static class ItemComparator implements Comparator<Item> {

		@Override
		public int compare(Item o1, Item o2) {
			if (o1 == null && o2 == null)
				return 0;
			if (o1 == null)
				return 1;
			if (o2 == null)
				return -1;
			return o1.getSortOrder() - o2.getSortOrder();
		}
	}

	/**
	 * Returns item in given slot.
	 * 
	 * @param slotNum
	 *            the slot number.
	 * @return item in that slot
	 * @throws ItemSlotIsEmptyException
	 *             if slot is empty.
	 */
	public synchronized Item getItemAt(int slotNum)
			throws ItemSlotIsEmptyException {
		if (items[slotNum] == null)
			throw new ItemSlotIsEmptyException(slotNum);
		return items[slotNum];
	}

	/**
	 * Returns the next slot that contains an item. Search starts at index
	 * <code>start + step</code> and is looping through items in steps of
	 * <code>step</code>. If inventory only contains one item that slot is
	 * returned. If inventory contains no item, -1 is returned.
	 * 
	 * @param start
	 *            index to start searching at (exclusive)
	 * @param step
	 *            step to go in each search step.
	 * @return slot number of nearest full item slot in direction of step.
	 */
	public synchronized int findNextFullSlot(int start, int step) {
		int i = 0;
		int index = BasicMath.calcModulo(start + step, items.length);
		while (i < items.length) {
			if (items[index] != null)
				return index;
			index = BasicMath.calcModulo(index + step, items.length);
			i++;
		}
		return -1;
	}

	public Item[] getAllItems() {
		return items;
	}

}