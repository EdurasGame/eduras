package de.illonis.eduras.items;

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
	 */
	public void use(ItemUseInformation info);

	/**
	 * Returns current cooldown of item.
	 * 
	 * @return current cooldown.
	 * 
	 * @author illonis
	 */
	public int getCooldown();

	/**
	 * Returns default cooldown time of this item.
	 * 
	 * @return default cooldown time.
	 * 
	 * @author illonis
	 */
	public int getCooldownTime();

	/**
	 * Reduces the cooldown of this item by given value. A cooldown will never
	 * be lower than 0.
	 * 
	 * @param value
	 *            cooldown reducing in seconds.
	 * 
	 * @author illonis
	 */
	public void reduceCooldown(int value);

	/**
	 * Resets cooldown
	 * 
	 * @author illonis
	 */
	public void resetCooldown();

}
