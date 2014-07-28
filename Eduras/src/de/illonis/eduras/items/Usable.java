package de.illonis.eduras.items;

import de.illonis.eduras.exceptions.InsufficientResourceException;

/**
 * Indicates that an item is usable and has a cooldown.
 * 
 * @author illonis
 * 
 */
public interface Usable {

	/**
	 * Triggers use of this item.
	 * 
	 * @param info
	 *            usage information.
	 * 
	 * @author illonis
	 * @return true if usage was successful, false if usage failed.
	 * @throws InsufficientResourceException
	 *             if user has not enough resources to use this item.
	 */
	public boolean use(ItemUseInformation info)
			throws InsufficientResourceException;

	/**
	 * Starts cooldown. This should be called on every use.
	 */
	public void startCooldown();

	/**
	 * Returns current cooldown of item.
	 * 
	 * @return current cooldown.
	 * 
	 * @author illonis
	 */
	public long getCooldown();

	/**
	 * Returns default cooldown time of this item.
	 * 
	 * @return default cooldown time.
	 * 
	 * @author illonis
	 */
	public long getCooldownTime();

	/**
	 * Reduces the current cooldown of this item by given value. A cooldown will
	 * never be lower than 0.
	 * 
	 * @param value
	 *            cooldown reducing in milliseconds.
	 * 
	 * @author illonis
	 */
	public void reduceCooldown(long value);

	/**
	 * Returns true if this item cannot be used at this moment because it has
	 * cooldown.
	 * 
	 * @return true if item has cooldown.
	 * 
	 * @author illonis
	 */
	public boolean hasCooldown();

	/**
	 * Resets cooldown
	 * 
	 * @author illonis
	 */
	public void resetCooldown();

}
