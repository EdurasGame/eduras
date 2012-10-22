/**
 * 
 */
package de.illonis.eduras.interfaces;

import de.illonis.eduras.events.GameEvent;

/**
 * Interface for a game logic. A class handling the game logic should implement
 * the here declared methods.
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 *
 */
public interface GameLogicInterface {
	
	public void onGameEventAppeared(GameEvent event);

	/**
	 * @param client
	 */
	public void addGameEventListener(GameEventListener listener);

}
