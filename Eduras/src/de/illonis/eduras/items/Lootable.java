package de.illonis.eduras.items;

/**
 * Indicates that an item is lootable.
 * 
 * @author illonis
 * 
 */
public interface Lootable {

	/**
	 * Called when this object is looted. It is automatically added to player's
	 * inventory and hidden to game, so only do the special stuff here, e.g.
	 * respawning etc.
	 * 
	 * @author illonis
	 */
	public void loot();
}
