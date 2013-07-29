package de.illonis.eduras.logic;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.PlayerMainFigure;
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
	 * Creates an object at given position.
	 * 
	 * @param object
	 *            type of object.
	 * @param position
	 *            position of new object
	 * @param owner
	 *            owner id of new object.
	 * @return the id of the created object.
	 */
	int createObjectAt(ObjectType object, Vector2D position, int owner);

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
	 * Moves a specific object to a new position instantly.
	 * 
	 * @param objectId
	 *            id of object to moved.
	 * @param newPosition
	 *            target position.
	 */
	void setPositionOfObject(int objectId, Vector2D newPosition);

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
	 */
	void changeGameMode(GameMode newMode);

	/**
	 * Remaxes the health of the unit.
	 * 
	 * @param unit
	 */
	void remaxHealth(Unit unit);
}
