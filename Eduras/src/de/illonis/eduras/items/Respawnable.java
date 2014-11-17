package de.illonis.eduras.items;

/**
 * Indicates that an item can respawn in general. However, if it can respawn
 * under specific conditions is specified by the implementation of the
 * {@link #isAbleToRespawn()} method.
 * 
 * @author Florian 'Ren' Mai
 * 
 */
public interface Respawnable {

	/**
	 * Returns the default respawn time of this object.
	 * 
	 * @return respawn time in milliseconds.
	 * 
	 * @author illonis
	 */
	public long getRespawnTime();

	/**
	 * Determines if the item is able to respawn right now.
	 * 
	 * @return true if it can respawn
	 */
	public boolean isAbleToRespawn();

	/**
	 * Sets if the item can respawn.
	 * 
	 * @param isAble
	 */
	public void setAbleToRespawn(boolean isAble);

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
