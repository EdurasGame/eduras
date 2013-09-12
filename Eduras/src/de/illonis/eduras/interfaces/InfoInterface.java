/**
 * 
 */
package de.illonis.eduras.interfaces;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.Statistic;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.units.PlayerMainFigure;

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
	public PlayerMainFigure getPlayer() throws ObjectNotFoundException;

	/**
	 * Returns the game objects currently in the game.
	 * 
	 * @return The game objects as a hashmap.
	 */
	public ConcurrentHashMap<Integer, GameObject> getGameObjects();

	/**
	 * Returns a list of all players online.
	 * 
	 * @return player list.
	 * 
	 * @author illonis
	 */
	public Collection<PlayerMainFigure> getPlayers();

	/**
	 * @return copy of the team list.
	 * 
	 * @author illonis
	 */
	public Collection<Team> getTeams();

	/**
	 * Returns game statistics.
	 * 
	 * @return a statistics object.
	 */
	public Statistic getStatistics();

	/**
	 * Returns current game mode.
	 * 
	 * @return game mode.
	 */
	public GameMode getGameMode();

	/**
	 * Returns the game object with given id.
	 * 
	 * @param id
	 *            id of object.
	 * @return the game object.
	 * 
	 * @author illonis
	 */
	public GameObject findObjectById(int id);

	/**
	 * Returns the player that has given ownerid.
	 * 
	 * @param ownerId
	 *            owner id.
	 * @return the player.
	 * 
	 * @author illonis
	 * @throws ObjectNotFoundException
	 *             if player does not exist.
	 */
	public PlayerMainFigure getPlayerByOwnerId(int ownerId)
			throws ObjectNotFoundException;

	/**
	 * Returns the remaining game time.
	 * 
	 * @return remaining time in milliseconds.
	 */
	public long getRemainingTime();
}
