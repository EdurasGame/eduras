package de.illonis.eduras.interfaces;

import java.util.Collection;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
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
	public Player getPlayer() throws ObjectNotFoundException;

	/**
	 * Returns the game objects currently in the game.
	 * 
	 * @return The game objects as a hashmap.
	 */
	public java.util.Map<Integer, GameObject> getGameObjects();

	/**
	 * @return client data.
	 */
	public ClientData getClientData();

	/**
	 * Returns a list of all players online.
	 * 
	 * @return player list.
	 * 
	 * @author illonis
	 */
	public Collection<Player> getPlayers();

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
	 * @throws ObjectNotFoundException
	 * 
	 */
	public GameObject findObjectById(int id) throws ObjectNotFoundException;

	/**
	 * Returns the team with given id.
	 * 
	 * @param teamId
	 *            id of team.
	 * @return the team.
	 * 
	 */
	public Team findTeamById(int teamId);

	/**
	 * Returns all objects of the given type.
	 * 
	 * @param type
	 *            an object must have this type.
	 * @return a collection of all objects of the given type.
	 */
	public Collection<GameObject> findObjectsByType(ObjectType type);

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
	public Player getPlayerByOwnerId(int ownerId)
			throws ObjectNotFoundException;

	/**
	 * @param point
	 *            the game coordinate to look at.
	 * @return a list of objects colliding given point.
	 */
	public Collection<GameObject> findObjectsAt(Vector2f point);

	/**
	 * Returns the remaining game time.
	 * 
	 * @return remaining time in milliseconds.
	 */
	public long getRemainingTime();

	/**
	 * Returns the remaining time until the next respawn wave.
	 * 
	 * @return remaining time in milliseconds
	 */
	public long getRespawnTime();

	/**
	 * Tells whether an object of a given type would fit into the given base.
	 * 
	 * @param type
	 * @param base
	 * @return true if it fits
	 */
	public boolean fitsObjectInBase(ObjectType type, Base base);

	/**
	 * Tells whether the given player can blink to the target.
	 * 
	 * @param player
	 * @param target
	 * @return true if yes
	 */
	public boolean canBlinkTo(PlayerMainFigure player, Vector2f target);
}
