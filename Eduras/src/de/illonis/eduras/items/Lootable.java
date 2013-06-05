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

	/**
	 * Returns the default respawn time of this object.
	 * 
	 * @return respawn time in milliseconds.
	 * 
	 * @author illonis
	 */
	public long getRespawnTime();

	/**
	 * Returns the remaining respawn time of this object.
	 * 
	 * @return remaining respawn time in milliseconds.
	 * 
	 * @author illonis
	 */
	public long getRespawnTimeRemaining();

	/**
	 * Reduces the remaining respawn time of this object by given value. A
	 * respawn time will never be lower than 0.
	 * 
	 * @param value
	 *            respawn time reducing in milliseconds.
	 * 
	 * @author illonis
	 * @return true if item respawns now.
	 */
	public boolean reduceRespawnRemaining(long value);
}
