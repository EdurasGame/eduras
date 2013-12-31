package de.illonis.eduras.logic;

import java.util.ArrayList;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.ai.movement.UnitNotControllableException;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;
import de.illonis.eduras.units.Unit;

/**
 * Allows objects to trigger specific events that will be interpreted by logic
 * and sent to other clients.
 * 
 * @author illonis
 * 
 */
public interface EventTriggerer {

	/**
	 * Launches a specific missile with given position information.
	 * 
	 * @param missileType
	 *            type of missile.
	 * @param owner
	 *            owner id of missile.
	 * @param position
	 *            spawn position.
	 * @param speedVector
	 *            speed of missile.
	 */
	void createMissile(ObjectType missileType, int owner, Vector2D position,
			Vector2D speedVector);

	/**
	 * Removes given object from game object list.
	 * 
	 * @param objectId
	 *            id of object.
	 */
	void removeObject(int objectId);

	/**
	 * Creates a dynamic polygon.
	 * 
	 * @param polygonVertices
	 *            vertices of polygon.
	 * @param position
	 *            position of polygon.
	 * @param owner
	 *            owner id of new polygon.
	 * 
	 * @author illonis
	 */
	void createDynamicPolygonAt(Vector2D[] polygonVertices, Vector2D position,
			int owner);

	/**
	 * Creates an object at given position.
	 * 
	 * @param object
	 *            type of object.
	 * @param position
	 *            position of new object
	 * @param owner
	 *            owner id of new object.
	 * @return the id of the created object.
	 * @see #createDynamicPolygonAt(Vector2D[], Vector2D, int)
	 */
	public int createObjectAt(ObjectType object, Vector2D position, int owner);

	/**
	 * Sets polygon data of a polygon with given id.
	 * 
	 * @param objectId
	 *            object id of polygon.
	 * @param polygonVertices
	 *            new vertices of polygon.
	 * @see #createDynamicPolygonAt(Vector2D[], Vector2D, int)
	 * 
	 * @author illonis
	 */
	public void setPolygonData(int objectId, Vector2D[] polygonVertices);

	/**
	 * Creates an object.
	 * 
	 * @param object
	 *            The type of the object.
	 * @param owner
	 *            The owner of the object.
	 * @return the id of the created object.
	 */
	public int createObject(ObjectType object, int owner);

	/**
	 * Creates loot events.
	 * 
	 * @param objectId
	 *            object that was looted.
	 * @param playerId
	 *            player that looted
	 */
	void lootItem(int objectId, int playerId);

	/**
	 * Changes an itemslot of a specific player.
	 * 
	 * @param slot
	 *            the item slot that changed.
	 * @param player
	 *            the player, whose item slot changed.
	 * @param newItem
	 *            the new item at given slot. Use null to clear slot.
	 * 
	 * @author illonis
	 */
	void changeItemSlot(int slot, int player, Item newItem);

	/**
	 * Moves a specific object to a new position instantly. Does not guarantee
	 * this event is reaches the clients.
	 * 
	 * @param objectId
	 *            id of object to moved.
	 * @param newPosition
	 *            target position.
	 */
	void maybeSetPositionOfObject(int objectId, Vector2D newPosition);

	/**
	 * Moves a specific object to a new position instantly. Does guarantee the
	 * event reaches the clients.
	 * 
	 * @param objectId
	 *            id of object to moved.
	 * @param newPosition
	 *            target position.
	 */
	void guaranteeSetPositionOfObject(int objectId, Vector2D newPosition);

	/**
	 * You can implement this method if you need to do some setup.
	 */
	void init();

	/**
	 * Set the health of an object to a new value.
	 * 
	 * @param id
	 *            The object's id.
	 * @param newHealth
	 *            the new value of the health.
	 */
	void setHealth(int id, int newHealth);

	/**
	 * Respawns a player. Assumes the player has not been removed yet.
	 * 
	 * @param player
	 *            The player to respawn.
	 */
	void respawnPlayer(PlayerMainFigure player);

	/**
	 * Called when a player changes his name.
	 * 
	 * @param ownerId
	 *            The player's owner id.
	 * @param newName
	 *            The new name of the player.
	 */
	void renamePlayer(int ownerId, String newName);

	/**
	 * Called when the match ends.
	 */
	void onMatchEnd();

	/**
	 * Resets statistics, respawns each player, resets health, and loads initial
	 * map objects.
	 */
	void restartRound();

	/**
	 * Sets the remaining time to 'remainingTime'.
	 * 
	 * @param remainingTime
	 */
	void setRemainingTime(long remainingTime);

	/**
	 * Changes current map to given map.
	 * 
	 * @param map
	 *            new map.
	 * 
	 * @author illonis
	 */
	void changeMap(Map map);

	/**
	 * Notifies of death of unit.
	 * 
	 * @param unit
	 *            killed unit.
	 * @param killer
	 *            owner id of killing object.
	 * 
	 * @author illonis
	 */
	void onDeath(Unit unit, int killer);

	/**
	 * Changes the gamemode to newMode.
	 * 
	 * @param newMode
	 *            the new gamemode.
	 */
	void changeGameMode(GameMode newMode);

	/**
	 * Changes the interaction mode for a player.
	 * 
	 * @param ownerId
	 *            the owner id of the player.
	 * @param newMode
	 *            the new mode for that player.
	 */
	void changeInteractMode(int ownerId, InteractMode newMode);

	/**
	 * Remaxes the health of the unit.
	 * 
	 * @param unit
	 *            the unit.
	 */
	void remaxHealth(Unit unit);

	/**
	 * Sends a unit to a given direction. This triggers the unit's AI to move
	 * the unit to given location using its preferred way of motion.
	 * 
	 * @param objectId
	 *            the object id of sent unit.
	 * @param target
	 *            the target location.
	 * @throws ObjectNotFoundException
	 *             if no gameobject with given id was found.
	 * @throws UnitNotControllableException
	 *             if that unit is not controllable regarding motion.
	 */
	void sendUnit(int objectId, Vector2D target)
			throws ObjectNotFoundException, UnitNotControllableException;

	/**
	 * Sets teams to given teams.
	 * 
	 * @param teams
	 *            the new teams.
	 * 
	 * @author illonis
	 */
	void setTeams(Team... teams);

	/**
	 * Adds a player to given team.
	 * 
	 * @param ownerId
	 *            owner id of player.
	 * @param team
	 *            team.
	 * 
	 * @author illonis
	 */
	void addPlayerToTeam(int ownerId, Team team);

	/**
	 * Kicks the player with the given id from the server. Removes the client's
	 * playermainfigure and closes the connection.
	 * 
	 * @param ownerId
	 *            The player's id.
	 */
	void kickPlayer(int ownerId);

	/**
	 * Sends all events in the given list to the client with the given id.
	 * 
	 * @param infos
	 * @param id
	 *            The client's id.
	 */
	public void sendRequestedInfos(ArrayList<GameEvent> infos, int id);

	// TODO: Shouldn't we send this information only to the client that is
	// affected by this event?
	/**
	 * 
	 * Notify all clients that a cooldown has started.
	 * 
	 * @param event
	 *            The event that indicates that the cooldown has started.
	 */
	public void notifyCooldownStarted(ItemEvent event);

	/**
	 * Notify all clients that an object's state has changed.
	 * 
	 * @param event
	 *            The event that indicate what has changed.
	 */
	public void notifyGameObjectStateChanged(
			SetGameObjectAttributeEvent<?> event);

	/**
	 * Notify all clients that an object has been created.
	 * 
	 * @param event
	 *            The event that indicates what has been created.
	 */
	public void notifyObjectCreated(ObjectFactoryEvent event);

	/**
	 * Notifies all clients that an object is on a new position.
	 * 
	 * @param o
	 *            The object that has changed position (with already updated
	 *            position).
	 */
	public void notifyNewObjectPosition(GameObject o);

	/**
	 * Informs the clients of the gameobjects current rotation.
	 * 
	 * @param gameObject
	 *            The object that changed the rotation angle.
	 */
	public void setRotation(GameObject gameObject);

}
