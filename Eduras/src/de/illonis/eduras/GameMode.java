package de.illonis.eduras;

import de.illonis.eduras.units.Unit;

/**
 * This interface serves a number of methods which are called when a certain
 * event appears and an implementation shall reflect a game mode's behavior on
 * such an event.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface GameMode {

	/**
	 * Returns the game mode's name.
	 * 
	 * @return The name of the game mode.
	 */
	public String getName();

	/**
	 * Called when someon died.
	 * 
	 * @param killedUnit
	 *            The player who died.
	 * @param killingUnit
	 *            The player who killed the other player. Null if there is none.
	 */
	public void onDeath(Unit killedUnit, Unit killingUnit);

	/**
	 * Called when the time is up.
	 */
	public void onTimeUp();

	/**
	 * Called when a player connects.
	 */
	public void onConnect(int ownerId);

}
