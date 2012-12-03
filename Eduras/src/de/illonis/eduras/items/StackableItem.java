package de.illonis.eduras.items;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;

/**
 * An item that is stackable. Stackable means you can hold multiple of this item
 * in a single slot. On each use, stack size is reduced by one. Maximum stack
 * size must not be above stack size at any time.
 * 
 * @author illonis
 * 
 */
public abstract class StackableItem extends Item implements Consumable {

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
	 * Returns current stack size of this item. Default stack size is 1.
	 * 
	 * @return current stack size.
	 */
	public int getStackSize() {
		return stackSize;
	}

	/**
	 * Returns maximum stack size of this item.
	 * 
	 * @return maximum stack size.
	 */
	public int getMaxStackSize() {
		return maxStackSize;
	}

	/**
	 * Checks if stack is full and cannot take more elements.
	 * 
	 * @return true if stack is full, false otherwise.
	 */
	public synchronized boolean isFull() {
		return stackSize >= maxStackSize;
	}

	/**
	 * Checks if stack is empty (i.e. {@link #stackSize} == 0).
	 * 
	 * @return true if stack is empty, false otherwise.
	 */
	public synchronized boolean isEmpty() {
		return stackSize == 0;
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

	@Override
	public final void use(GameObject unit) {
		if (takeFromStack()) {
			consumeAction();
		}
	}

	/**
	 * Called when item is consumed.
	 */
	protected abstract void consumeAction();
}
