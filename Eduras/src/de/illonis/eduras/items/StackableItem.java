package de.illonis.eduras.items;

import de.illonis.eduras.GameInformation;

/**
 * An item that is stackable. Stackable means you can hold multiple of this item
 * in a single slot. On each use, stack size is reduced by one.
 * 
 * @author illonis
 * 
 */
public abstract class StackableItem extends Item {

	private int maxStackSize = 1;
	protected int stackSize = 1;

	public StackableItem(ItemType type, GameInformation gi) {
		super(type, gi);
		setMaxStackSize();
	}

	/**
	 * Sets the maximum stack size of this item to size specified by itemtype.
	 */
	private void setMaxStackSize() {
		// TODO: item-type dependent set of stacksize (use switch-case).
		maxStackSize = 3;
	}

	/**
	 * 
	 * @return
	 */
	public int getStackSize() {
		return stackSize;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

	/**
	 * Adds given quantity to this item. If {{@link #maxStackSize} is not big
	 * enough to add all items, false is returned and nothing is added.
	 * 
	 * @param num
	 *            quantity to add.
	 * @return true when addition was successful, false otherwise.
	 */
	public synchronized boolean addToStack(int num) {
		if (stackSize + num > maxStackSize)
			return false;
		stackSize += num;
		return true;
	}

	/**
	 * Takes a single entity from this item-stack. Will return false when there
	 * is no element in stack (i.e. {@link #getStackSize()} returns 0).
	 * 
	 * @return true if item could be taken, false otherwise.
	 */
	public synchronized boolean takeFromStack() {
		if (stackSize > 0) {
			stackSize--;
			return true;
		}
		return false;
	}

}
