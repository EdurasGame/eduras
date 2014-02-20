package de.illonis.eduras.items;

/**
 * Indicates an item is consumable, that means it or parts of it are removed
 * when used.
 * 
 * @author illonis
 * 
 */
public interface Consumable extends Usable {

	/**
	 * Will be called as soon the item is consumed completely. This happens when
	 * a stackable item has an empty stack or a consumable is used.
	 */
	void onAllGone();

}
