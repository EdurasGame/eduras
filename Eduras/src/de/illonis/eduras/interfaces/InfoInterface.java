/**
 * 
 */
package de.illonis.eduras.interfaces;

import java.awt.Rectangle;
import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.Map;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.ObjectNotFoundException;

/**
 * This interface determines what information must be provided to the GUI.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface InfoInterface {

	/**
	 * Returns information about how big the map is. In detail, it returns the
	 * width and height of the map.
	 * 
	 * @return Returns the bounds of the map.
	 * @see Map#getBounds()
	 */
	public Rectangle getMapBounds();

	/**
	 * Returns the player which belongs to the client.
	 * 
	 * @return The player.
	 * @throws ObjectNotFoundException
	 *             Thrown if the player could not be found.
	 */
	public Player getPlayer() throws ObjectNotFoundException;

	/**
	 * Returns the game objects currently in the game.
	 * 
	 * @return The game objects as a hashmap.
	 */
	public ConcurrentHashMap<Integer, GameObject> getGameObjects();

}
