package de.illonis.eduras.inventory;

/**
 * Thrown when an item from an empty item slot was tried to receive.
 * 
 * @author illonis
 * 
 */
public class ItemSlotIsEmptyException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link ItemSlotIsEmptyException}.
	 * 
	 * @param slot
	 *            the slot that is empty.
	 */
	public ItemSlotIsEmptyException(int slot) {
		super("Item slot " + slot + " is empty.");
	}
}
