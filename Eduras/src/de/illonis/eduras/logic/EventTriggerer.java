package de.illonis.eduras.logic;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.ai.movement.UnitNotControllableException;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.InteractMode;
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
	 * @return the id of the created missile
	 */
	int createMissile(ObjectType missileType, int owner, Vector2f position,
			Vector2f speedVector);

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
	 * @param type
	 *            The type of the dynamic polygon.
	 * @param polygonVector2fs
	 *            vertices of polygon.
	 * @param position
	 *            position of polygon.
	 * @param owner
	 *            owner id of new polygon.
	 * @return returns the id of the newly created object
	 * 
	 * @author illonis
	 */
	int createDynamicPolygonObjectAt(ObjectType type,
			Vector2f[] polygonVector2fs, Vector2f position, int owner);

	/**
	 * Creates an object at given position such that the top left corner of the
	 * newly created object's shape is at that position.
	 * 
	 * @param object
	 *            type of object.
	 * @param position
	 *            position of new object
	 * @param owner
	 *            owner id of new object.
	 * @return the id of the created object.
	 * @see #createDynamicPolygonObjectAt(ObjectType, Vector2f[], Vector2f, int)
	 */
	public int createObjectAt(ObjectType object, Vector2f position, int owner);

	/**
	 * Creates an object at random position within given shape.<br>
	 * Guarantees that a created objects location (topleft) will be within given
	 * shape and does not collide with any other existing game object. However
	 * it is not guaranteed that the created object is completely within given
	 * shape.
	 * 
	 * @param object
	 *            type of object.
	 * @param shape
	 *            area where the new object will be placed in.
	 * @param owner
	 *            owner id of new object.
	 * @return the id of the created object or -1 if not enough space to spawn
	 *         that object.
	 */
	public int createObjectIn(ObjectType object, Shape shape, int owner);

	/**
	 * Creates an object at random position within given base. Guarantees that a
	 * created objects location (topleft) will be within given shape and does
	 * not collide with any other existing game object. It is also guaranteed
	 * that the created object is completely within given shape.
	 * 
	 * @param object
	 *            type of object to create
	 * @param base
	 *            base to create it in
	 * @param owner
	 *            owner id of object to create
	 * @return id of newly created object
	 */
	public int createObjectAtBase(ObjectType object, Base base, int owner);

	/**
	 * Creates an object at given position such that the center of the newly
	 * created object is at that position.
	 * 
	 * @param object
	 *            type of object.
	 * @param position
	 *            position of new object
	 * @param owner
	 *            owner id of new object.
	 * @return the id of the created object.
	 * @see #createDynamicPolygonObjectAt(ObjectType, Vector2f[], Vector2f, int)
	 */
	public int createObjectWithCenterAt(ObjectType object, Vector2f position,
			int owner);

	/**
	 * Sets polygon data of a polygon with given id.
	 * 
	 * @param objectId
	 *            object id of polygon.
	 * @param polygonVector2fs
	 *            new vertices of polygon.
	 * @see #createDynamicPolygonObjectAt(ObjectType, Vector2f[], Vector2f, int)
	 * 
	 * @author illonis
	 */
	public void setPolygonData(int objectId, Vector2f[] polygonVector2fs);

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
	void maybeSetPositionOfObject(int objectId, Vector2df newPosition);

	/**
	 * Moves a specific object to a new position instantly. Does guarantee the
	 * event reaches the clients.
	 * 
	 * @param objectId
	 *            id of object to moved.
	 * @param newPosition
	 *            target position.
	 */
	void guaranteeSetPositionOfObject(int objectId, Vector2f newPosition);

	/**
	 * Moves a specific object to a new position instantly, where the position
	 * is to be where the center of the object shall be. Does guarantee the
	 * event reaches the clients.
	 * 
	 * @param objectId
	 *            id of object to moved.
	 * @param newPosition
	 *            target position.
	 */
	void guaranteeSetPositionOfObjectAtCenter(int objectId, Vector2f newPosition);

	/**
	 * Sets color and texture of an object. Color is only set for
	 * {@link DynamicPolygonObject}s.
	 * 
	 * @param o
	 *            the object id.
	 * @param color
	 *            the new color.
	 * @param texture
	 *            the new texture.
	 */
	void setRenderInfoForObject(GameObject o, Color color, TextureKey texture);

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
	 * Respawns a player at a random spawnpoint that suits the player's team.
	 * Assumes the player has not been removed yet.
	 * 
	 * @param player
	 *            The player to respawn.
	 */
	void respawnPlayerAtRandomSpawnpoint(Player player);

	/**
	 * Respawns a player at a given location.
	 * 
	 * @param player
	 *            Player to respawn
	 * @param location
	 *            Location to respawn the player at.
	 */
	void respawnPlayerAtPosition(Player player, Vector2df location);

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
	 * 
	 * @param winnerId
	 *            id of the winner.
	 */
	void onMatchEnd(int winnerId);

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
	 * Tells the clients when the next respawn wave is going to take place.
	 * 
	 * @param respawnTime
	 */
	void notififyRespawnTime(long respawnTime);

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
	void notifyDeath(Unit unit, int killer);

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
	void sendUnit(int objectId, Vector2f target)
			throws ObjectNotFoundException, UnitNotControllableException;

	/**
	 * Sets teams to given teams.
	 * 
	 * @param teams
	 *            the new teams.
	 * 
	 * @author illonis
	 */
	void setTeams(Collection<Team> teams);

	/**
	 * Sets the size of a triggerarea.
	 * 
	 * @param objectId
	 *            id of that trigger area.
	 * @param newWidth
	 *            new width.
	 * @param newHeight
	 *            new height.
	 * @throws ObjectNotFoundException
	 *             if no object with given id exists or that object is no
	 *             trigger area.
	 */
	void setTriggerAreaSize(int objectId, float newWidth, float newHeight)
			throws ObjectNotFoundException;

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
	 * Removes the {@link PlayerMainFigure} with the given ownerid from the
	 * server.
	 * 
	 * @param ownerId
	 *            The player's ownerId.
	 */
	void removePlayer(int ownerId);

	/**
	 * Sends all events in the given list to the client with the given id.
	 * 
	 * @param infos
	 * @param id
	 *            The client's id.
	 */
	public void sendRequestedInfos(ArrayList<GameEvent> infos, int id);

	// TODO: Shouldn't we send this information only to the client that is
	// affected by this event? (jme) yes (/jme) (fma) I'm glad we talked about
	// this :) (/fma)
	/**
	 * 
	 * Notify all clients that a cooldown has started.
	 * 
	 * @param event
	 *            The event that indicates that the cooldown has started.
	 */
	public void notifyCooldownStarted(ItemEvent event);

	/**
	 * 
	 * Notify all clients that a cooldown has finished.
	 * 
	 * @param idOfItem
	 *            The id of the item the cooldown has finished of
	 */
	public void notifyCooldownFinished(int idOfItem);

	/**
	 * Notify all clients that an object's state has changed.
	 * 
	 * @param event
	 *            The event that indicate what has changed.
	 */
	public void notifyGameObjectStateChanged(
			SetGameObjectAttributeEvent<?> event);

	/**
	 * Notify all clients that an object's visibility has changed.
	 * 
	 * @param event
	 *            The event that indicate what has changed.
	 */
	public void notifyGameObjectVisibilityChanged(SetVisibilityEvent event);

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

	/**
	 * Set the visibility of the given object.
	 * 
	 * @param objectId
	 *            The identifier of the object.
	 * @param newVal
	 *            The new value of the visibility.
	 */
	public void setVisibility(int objectId, Visibility newVal);

	/**
	 * Set the collidability of the given object.
	 * 
	 * @param objectId
	 *            The identifier of the object.
	 * @param newVal
	 *            The new value of the collidability.
	 */
	public void setCollidability(int objectId, boolean newVal);

	/**
	 * Set a {@link StatsProperty} of the player to the given value.
	 * 
	 * @param property
	 * @param player
	 * @param valueToSet
	 */
	public void setStats(StatsProperty property, Player player, int valueToSet);

	/**
	 * Increases the count of a player's {@link StatsProperty} by the given
	 * number.
	 * 
	 * @param prop
	 *            stat to increase (or decrease)
	 * @param player
	 *            player to change the stats of
	 * @param i
	 *            amount
	 */
	void changeStatOfPlayerByAmount(StatsProperty prop, Player player, int i);

	/**
	 * Notifies all clients that the {@link NeutralArea} was conquered by the
	 * given team.
	 * 
	 * @param neutralArea
	 * @param occupyingTeam
	 */
	void notifyAreaConquered(NeutralArea neutralArea, Team occupyingTeam);

	/**
	 * Increases the count of a team's resources by the given number.
	 * 
	 * @param team
	 *            The team to increase the resourcecount of
	 * @param amount
	 *            the amount to increase by (can be negative)
	 */
	void changeResourcesOfTeamByAmount(Team team, int amount);

	/**
	 * Notifies client that a player joined.
	 * 
	 * @param ownerId
	 *            the ownerid of joined player.
	 */
	void notifyPlayerJoined(int ownerId);

	/**
	 * Notifies client that a player left.
	 * 
	 * @param ownerId
	 *            the owner id of left player.
	 */
	void notifyPlayerLeft(int ownerId);

	/**
	 * Heals a {@link Unit} by the given amount and tells the clients.
	 * Thread-safe.
	 * 
	 * @param unitToHeal
	 * @param healAmount
	 *            should be positive, but isn't checked.
	 */
	void changeHealthByAmount(Unit unitToHeal, int healAmount);

	/**
	 * Sets all the inventory slots to null.
	 * 
	 * @param player
	 *            The player to clear the inventory of.
	 */
	void clearInventoryOfPlayer(Player player);

	/**
	 * Called when a player joins. Notifies the client. Is used differently to
	 * notifyPlayerJoined such that it is used to modify the
	 * {@link GameInformation}'s state on the client rather than the GUI.
	 * 
	 * @param newPlayer
	 */
	void onPlayerJoined(Player newPlayer);

	/**
	 * Notifies a client that he has received all the information he needs.
	 * 
	 * @param clientId
	 *            id of the client to notify that the game is ready to go.
	 */
	void notifyGameReady(int clientId);

	/**
	 * Loads the given settings file on the server and sends it to the clients.
	 * 
	 * @param settingsFile
	 */
	void loadSettings(File settingsFile);

	/**
	 * Adds an item of the given type to the player's inventory.
	 * 
	 * @param player
	 * @param itemType
	 * @throws WrongObjectTypeException
	 *             Thrown if the given {@link ObjectType} is not an item.
	 */
	void giveNewItem(Player player, ObjectType itemType)
			throws WrongObjectTypeException;

	/**
	 * Notifies a client that he cannot use a wepon because it has no ammo.
	 * 
	 * @param clientId
	 *            the client id.
	 * @param weaponType
	 *            the type of item he tried to use.
	 */
	void notifyWeaponAmmoEmpty(int clientId, ObjectType weaponType);

	/**
	 * Respawns the given player at the given base, if there's room.
	 * 
	 * @param player
	 * @param base
	 * @return returns the id of the object that just spawned
	 */
	int respawnPlayerAtBase(Player player, Base base);

	/**
	 * Set a setting property to a certain value and notify the clients about
	 * it.
	 * 
	 * @param settingName
	 * @param settingValue
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 *             thrown if the value given doesn't apply to the property's
	 *             type
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 *             thrown if there is no such property
	 */
	void setSetting(String settingName, String settingValue)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException;

	/**
	 * Sets the speed of the given {@link MoveableGameObject} to oldSpeed +
	 * amount.
	 * 
	 * @param objectToChangeSpeedOf
	 * @param amount
	 */
	void changeSpeedBy(MoveableGameObject objectToChangeSpeedOf, float amount);

	/**
	 * Sets the speed of the given {@link MoveableGameObject} to newValue.
	 * 
	 * @param object
	 * @param newValue
	 */
	void setSpeed(MoveableGameObject object, float newValue);

	/**
	 * Speeds up a {@link MoveableGameObject} by some value for some time.
	 * 
	 * @param objectToSpeedUp
	 * @param timeInMiliseconds
	 *            duration of speed up
	 * @param speedUpValue
	 */
	void speedUpObjectForSomeTime(MoveableGameObject objectToSpeedUp,
			long timeInMiliseconds, float speedUpValue);

	/**
	 * Makes a {@link GameObject} invisible for some time.
	 * 
	 * @param objectToMakeInvisible
	 * @param timeInMiliseconds
	 */
	void makeInvisibleForSomeTime(GameObject objectToMakeInvisible,
			long timeInMiliseconds);

	/**
	 * Changes the number of blink charges of a player by the given number.
	 * 
	 * @param player
	 * 
	 * @param charges
	 */
	void changeBlinkChargesBy(Player player, int charges);

	/**
	 * Sends a resource to a player
	 * 
	 * @param type
	 *            type of resource
	 * 
	 * @param owner
	 *            owner id of the player
	 * @param r
	 *            name of resource
	 * @param file
	 *            file to send
	 */
	void sendResource(GameEventNumber type, int owner, String r, Path file);

	/**
	 * Restarts the game.
	 */
	void restartGame();

	/**
	 * Notify client that AoE damage has been dealt by the given object type at
	 * the given position.
	 * 
	 * @param type
	 * @param centerPosition
	 */
	void notifyAoEDamage(ObjectType type, Vector2f centerPosition);

	/**
	 * Sets the team of a unit.
	 * 
	 * @param createdUnit
	 *            unit to set team of
	 * @param team
	 *            the team to set this unit to
	 */
	void setTeamOfUnit(Unit createdUnit, Team team);

	/**
	 * Called when a round ends.
	 * 
	 * @param teamId
	 *            id of winner of the round
	 */
	void onRoundEnd(int teamId);

	/**
	 * Set the score of a team.
	 * 
	 * @param team
	 * @param newScore
	 */
	void setTeamScore(Team team, int newScore);

	/**
	 * Notify clients that a player used blink.
	 * 
	 * @param playerId
	 *            owner id of player.
	 */
	void notifyBlinkUsed(int playerId);
}
