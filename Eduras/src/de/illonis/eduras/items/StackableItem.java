package de.illonis.eduras.items;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * An item that is stackable. Stackable means you can hold multiple of this item
 * in a single slot. On each use, stack size is reduced by one. The stack has a
 * maximum size that cannot be exceeded.
 * 
 * @author illonis
 * 
 */
public abstract class StackableItem extends Item implements Consumable {

	private int maxStackSize = 1;
	protected int stackSize = 1;

	/**
	 * Creates a new stackable item.
	 * 
	 * @param type
	 *            item type.
	 * @param gi
	 *            game information.
	 * @param id
	 *            item object id.
	 */
	public StackableItem(ObjectType type, GameInformation gi, int id) {
		super(type, gi, id);
		setMaxStackSize(5);
	}

	/**
	 * Sets the maximum stack size of this item to size specified by itemtype.
	 * 
	 * @param newSize
	 *            the new maximum stack size, must be greater than zero.
	 */
	private void setMaxStackSize(int newSize) {
		maxStackSize = newSize;
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
			if (stackSize == 0)
				onAllGone();
			return true;
		}
		return false;
	}

	@Override
	public final boolean use(ItemUseInformation info) {
		if (takeFromStack()) {
			consumeAction();
			return true;
		}
		return false;
	}

	/**
	 * Called when item is consumed.
	 */
	protected abstract void consumeAction();
}
