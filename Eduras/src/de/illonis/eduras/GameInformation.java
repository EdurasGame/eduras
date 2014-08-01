package de.illonis.eduras;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.AddPlayerToTeamEvent;
import de.illonis.eduras.events.AreaConqueredEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.OwnerGameEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetMapEvent;
import de.illonis.eduras.events.SetPolygonDataEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.events.SetSettingsEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.GameModeNotSupportedByMapException;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.NoSpawnAvailableException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.FunMap;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.settings.S.SettingType;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Contains game information.
 * 
 * @author illonis
 * 
 */
public class GameInformation {
	private final static Logger L = EduLog.getLoggerFor(GameInformation.class
			.getName());

	private static final int ATTEMPT_PER_SPAWNPOINT = 100;

	private static final Random RANDOM = new Random();

	private final java.util.Map<Integer, GameObject> objects;
	private final java.util.Map<Integer, Player> players;
	private Map map;
	private EventTriggerer eventTriggerer;
	private GameSettings gameSettings;
	private final HashMap<Integer, Team> teams;
	private final HashMap<Team, SpawnType> spawnGroups;

	/**
	 * Creates a new game information object with emtpy object lists.
	 */
	public GameInformation() {
		objects = new ConcurrentHashMap<Integer, GameObject>();
		players = new ConcurrentHashMap<Integer, Player>();
		map = new FunMap();
		gameSettings = new GameSettings(this);
		teams = new HashMap<Integer, Team>();
		spawnGroups = new HashMap<Team, SpawnType>();
	}

	/**
	 * Returns the eventtriggerer to trigger events with.
	 * 
	 * @return The eventtriggerer.
	 */
	public EventTriggerer getEventTriggerer() {
		return eventTriggerer;
	}

	/**
	 * Returns all players that are connected.
	 * 
	 * @return a list of players.
	 * 
	 * @author illonis
	 */
	public Collection<Player> getPlayers() {
		return players.values();
	}

	/**
	 * Returns all teams.
	 * 
	 * @return a copy of the list of all teams.
	 * 
	 * @author illonis
	 */
	public Collection<Team> getTeams() {
		return new LinkedList<Team>(teams.values());
	}

	/**
	 * Clears teamlist.
	 */
	public void clearTeams() {
		teams.clear();
		spawnGroups.clear();
	}

	/**
	 * Adds a team to teamlist.
	 * 
	 * @param team
	 *            the new team.
	 */
	public void addTeam(Team team) {
		teams.put(team.getTeamId(), team);
		spawnGroups.put(team, getGameSettings().getGameMode()
				.getSpawnTypeForTeam(team));
	}

	/**
	 * Sets the eventtriggerer to trigger events with.
	 * 
	 * @param eventTriggerer
	 *            The eventtriggerer
	 */
	public void setEventTriggerer(EventTriggerer eventTriggerer) {
		this.eventTriggerer = eventTriggerer;
		eventTriggerer.init();
	}

	/**
	 * Returns map of current game.
	 * 
	 * @return map of current game.
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Adds an object to gameobjects. Objects are put in object list assigned to
	 * their id.
	 * 
	 * @param object
	 *            new object.
	 */
	public void addObject(GameObject object) {
		objects.put(object.getId(), object);
	}

	/**
	 * Returns all game objects.
	 * 
	 * @return game object list.
	 */
	public java.util.Map<Integer, GameObject> getObjects() {
		return objects;
	}

	/**
	 * Returns gameobject with given id. If no object is found, null is
	 * returned.
	 * 
	 * @param id
	 *            id to search for.
	 * @return object with given id.
	 */
	public GameObject findObjectById(int id) {
		return objects.get(id);
	}

	/**
	 * Returns team with given id. If no object is found, null is returned.
	 * 
	 * @param teamId
	 *            id to search for.
	 * @return team with given id.
	 */
	public Team findTeamById(int teamId) {
		return teams.get(teamId);
	}

	/**
	 * Returns a list of all gameobjects that are in range of a given position.
	 * 
	 * @param point
	 *            the location to search at.
	 * @param radius
	 *            the search radius.
	 * @return a list of nearby objects.
	 */
	public LinkedList<GameObject> findObjectsInDistance(Vector2df point,
			float radius) {
		// TODO: improve (using position is rather incorrect due to object's
		// dimensions)
		LinkedList<GameObject> objs = new LinkedList<GameObject>();
		for (GameObject object : objects.values()) {
			if (point.distance(object.getPositionVector()) <= radius) {
				objs.add(object);
			}
		}
		return objs;
	}

	/**
	 * Checks whether given point is in any object's bounding box.
	 * 
	 * @param point
	 *            the location to check.
	 * @param ignore
	 *            a gameobject to ignore while testing (can be null).
	 * @return true if there is an object, false otherwise.
	 */
	public boolean isVisionBlockingObjectAt(Vector2df point, GameObject ignore) {
		for (GameObject object : objects.values()) {
			if (!object.isVisionBlocking())
				continue;
			if (object.equals(ignore))
				continue;
			if (object.getShape().includes(point.x, point.y))
				return true;
		}
		return false;
	}

	/**
	 * Removes the first occurrence of the specified game object from gameobject
	 * list, if it is present. If the list does not contain the element, it is
	 * unchanged. More formally, removes the element with the lowest index
	 * <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
	 * (if such an element exists). Returns <tt>true</tt> if this list contained
	 * the specified element (or equivalently, if this list changed as a result
	 * of the call). If the game object is of type 'Player' the player is also
	 * removed from the players list.
	 * 
	 * @param go
	 *            element to be removed from this list, if present
	 * @return <tt>true</tt> if this list contained the specified element
	 */
	public boolean removeObject(GameObject go) {

		// FIXME: there is tried to remove null-objects.
		if (go == null)
			return true;
		boolean playerRemoveSuccess = true;

		return playerRemoveSuccess && (objects.remove(go.getId()) != null);
	}

	/**
	 * Adds a player to playerlist. Players are stored assigned to their
	 * owner-id.
	 * 
	 * @param player
	 *            player to add.
	 */
	public void addPlayer(Player player) {
		players.put(player.getPlayerId(), player);
	}

	/**
	 * Returns a specific player identified by owner id.
	 * 
	 * @param ownerId
	 *            owner id of player.
	 * @return player object of given owner.
	 * @throws ObjectNotFoundException
	 *             Thrown if there is no object found
	 */
	public Player getPlayerByOwnerId(int ownerId)
			throws ObjectNotFoundException {
		Player result = players.get(ownerId);
		if (result == null) {
			throw new ObjectNotFoundException(ownerId);
		}
		return players.get(ownerId);
	}

	/**
	 * Returns a specific player identified by object id.
	 * 
	 * @param objectId
	 *            The id of the object which is the player.
	 * @return Returns the player object relating to the given id.
	 * @throws ObjectNotFoundException
	 *             Thrown if there could be no player found that is related to
	 *             the given id.
	 */
	public PlayerMainFigure getPlayerByObjectId(int objectId)
			throws ObjectNotFoundException {
		PlayerMainFigure result = null;
		for (Player singlePlayer : players.values()) {
			if (singlePlayer.getPlayerMainFigure().getId() == objectId) {
				result = singlePlayer.getPlayerMainFigure();
				break;
			}
		}

		if (result == null) {
			throw new ObjectNotFoundException(objectId);
		}

		return result;

	}

	/**
	 * This method serializes all available current information about the game
	 * into events and returns them as a list.
	 * 
	 * @return a list of events representing current game state.
	 */
	public ArrayList<GameEvent> getAllInfosAsEvent() {

		ArrayList<GameEvent> infos = new ArrayList<GameEvent>();

		giveSettings(infos);

		announceAllPlayers(infos);

		ArrayList<NeutralBase> neutralBases = new ArrayList<NeutralBase>();
		putObjectInfos(infos, neutralBases);

		putCurrentSettings(infos);

		putCurrentStatsAndTeams(infos);

		putNeutralBaseOwnerInfos(infos, neutralBases);

		putGuiNotifications(infos);

		return infos;
	}

	private void giveSettings(ArrayList<GameEvent> infos) {
		File settingsFile;
		try {
			settingsFile = S.putSettingsInFile(SettingType.SERVER);
			infos.add(new SetSettingsEvent(settingsFile));
		} catch (IOException e) {
			L.log(Level.WARNING,
					"IOException when trying to create file from settings!", e);
			return;
		}
	}

	private void putGuiNotifications(ArrayList<GameEvent> infos) {
		for (Player player : players.values()) {
			infos.add(new OwnerGameEvent(GameEventNumber.INFO_PLAYER_JOIN,
					player.getPlayerId()));
		}
	}

	private void announceAllPlayers(ArrayList<GameEvent> infos) {
		for (Player player : players.values()) {
			infos.add(new OwnerGameEvent(GameEventNumber.PLAYER_JOINED, player
					.getPlayerId()));
		}
	}

	private void putNeutralBaseOwnerInfos(ArrayList<GameEvent> infos,
			ArrayList<NeutralBase> neutralBases) {
		for (NeutralBase base : neutralBases) {
			if (base.getCurrentOwnerTeam() != null) {
				AreaConqueredEvent baseConqueredNotification = new AreaConqueredEvent(
						base.getId(), base.getCurrentOwnerTeam().getTeamId());
				infos.add(baseConqueredNotification);
			}
		}
	}

	private void putCurrentStatsAndTeams(ArrayList<GameEvent> infos) {
		Statistic stats = gameSettings.getStats();

		for (Player player : players.values()) {
			int killsOfPlayer = stats.getKillsOfPlayer(player);
			SetIntegerGameObjectAttributeEvent setKillsEvent = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SET_KILLS, player.getPlayerId(),
					killsOfPlayer);
			infos.add(setKillsEvent);

			int deathsOfPlayer = stats.getDeathsOfPlayer(player);
			SetIntegerGameObjectAttributeEvent setDeathsEvent = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SET_DEATHS, player.getPlayerId(),
					deathsOfPlayer);
			infos.add(setDeathsEvent);
		}

		SetTeamsEvent teamEvent = new SetTeamsEvent();
		LinkedList<AddPlayerToTeamEvent> teamPlayerEvents = new LinkedList<AddPlayerToTeamEvent>();
		for (Team team : getTeams()) {
			teamEvent.addTeam(team);
			for (Player player : team.getPlayers()) {
				teamPlayerEvents.add(new AddPlayerToTeamEvent(player
						.getPlayerId(), team.getTeamId()));
			}
		}

		infos.add(teamEvent);
		infos.addAll(teamPlayerEvents);
	}

	private void putCurrentSettings(ArrayList<GameEvent> infos) {
		SetGameModeEvent e = new SetGameModeEvent(gameSettings.getGameMode()
				.getName());
		infos.add(e);

		SetRemainingTimeEvent remaining = new SetRemainingTimeEvent(
				gameSettings.getRemainingTime());
		infos.add(remaining);

		SetMapEvent setMapEvent = new SetMapEvent(map.getName());
		infos.add(setMapEvent);
	}

	private void putObjectInfos(ArrayList<GameEvent> infos,
			ArrayList<NeutralBase> neutralBases) {
		for (GameObject object : objects.values()) {
			ObjectFactoryEvent objectEvent = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_CREATE, object.getType(),
					object.getOwner());
			objectEvent.setId(object.getId());
			infos.add(objectEvent);

			if (object.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK
					|| object.getType() == ObjectType.MAPBOUNDS) {
				SetPolygonDataEvent polygonData = new SetPolygonDataEvent(
						object.getId(),
						((DynamicPolygonObject) object).getPolygonVertices());
				infos.add(polygonData);
			}

			if (object.getType() == ObjectType.NEUTRAL_BASE) {
				neutralBases.add((NeutralBase) object);
			}

			// send position immediately
			MovementEvent me = new MovementEvent(GameEventNumber.SET_POS_TCP,
					object.getId());
			me.setNewXPos(object.getXPosition());
			me.setNewYPos(object.getYPosition());
			infos.add(me);

			// send visible / collidable status
			SetVisibilityEvent visEvent = new SetVisibilityEvent(
					object.getId(), object.getVisibility());
			SetBooleanGameObjectAttributeEvent colEvent = new SetBooleanGameObjectAttributeEvent(
					GameEventNumber.SET_COLLIDABLE, object.getId(),
					object.isCollidable(null));
			infos.add(colEvent);
			infos.add(visEvent);

		}
		for (Player p : players.values()) {
			try {
				infos.add(new ClientRenameEvent(p.getPlayerId(), p.getName()));
			} catch (InvalidNameException e) {
				L.log(Level.WARNING, e.getLocalizedMessage(), e);
				continue;
			}
		}

	}

	/**
	 * Returns the game settings.
	 * 
	 * @return The game settings.
	 */
	public GameSettings getGameSettings() {
		return gameSettings;
	}

	/**
	 * Sets the game settings.
	 * 
	 * @param gameSettings
	 */
	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	/**
	 * Sets map to given map.
	 * 
	 * @param map
	 *            new map.
	 * 
	 * @author illonis
	 */
	public void setMap(Map map) {
		this.map = map;

	}

	private boolean isAnyOfObjectsWithinBounds(Rectangle bounds,
			Collection<GameObject> gameObjects) {
		for (GameObject o : gameObjects) {
			if (o.getShape().intersects(bounds))
				return true;
		}
		return false;
	}

	/**
	 * Checks whether an object collides with a given list of other objects.
	 * 
	 * @param object
	 *            the base object.
	 * @param otherObjects
	 *            A list of objects that should be checked for collision.
	 * @return a list containing every object from <i>otherObjects</i> that
	 *         collides with <i>object</i>.
	 */
	public Collection<GameObject> doesAnyOfOtherObjectsIntersect(
			GameObject object, Collection<GameObject> otherObjects) {
		Collection<GameObject> intersectingGameObjects = new LinkedList<GameObject>();

		for (GameObject o : otherObjects) {
			if (Geometry.shapeCollides(o.getShape(), object.getShape()))
				intersectingGameObjects.add(o);
		}

		return intersectingGameObjects;
	}

	/**
	 * Returns the spawning position for given player. This may change upon each
	 * call.
	 * 
	 * @param player
	 *            the player that spawns.
	 * @return the new spawning position.
	 * @throws GameModeNotSupportedByMapException
	 *             if current game does not support game mode.
	 * @throws NoSpawnAvailableException
	 *             thrown if no spawnpoint can be determined
	 */
	public Vector2df getSpawnPointFor(Player player)
			throws GameModeNotSupportedByMapException,
			NoSpawnAvailableException {

		SpawnType spawnType;
		try {
			Team team = player.getTeam();
			spawnType = spawnGroups.get(team);
		} catch (PlayerHasNoTeamException e1) {
			spawnType = SpawnType.ANY;
		}

		LinkedList<SpawnPosition> availableSpawnings = new LinkedList<SpawnPosition>();

		LinkedList<SpawnPosition> spawnAreas = new LinkedList<SpawnPosition>(
				getMap().getSpawnAreas());
		for (int i = 0; i < spawnAreas.size(); i++) {
			SpawnPosition p = spawnAreas.get(i);
			if (spawnType == SpawnType.ANY || p.getTeaming() == spawnType
					|| p.getTeaming() == SpawnType.ANY)
				availableSpawnings.add(p);
		}

		if (availableSpawnings.size() == 0)
			throw new GameModeNotSupportedByMapException(getGameSettings()
					.getGameMode(), getMap());

		// make it approximately random what spawn position will be used
		Collections.shuffle(availableSpawnings, new Random(System.nanoTime()));

		Shape playerShape = player.getPlayerMainFigure().getShape();
		Rectangle boundings = new Rectangle(0, 0, playerShape.getWidth(),
				playerShape.getHeight());

		for (SpawnPosition spawnPos : availableSpawnings) {

			boolean spawnPositionOkay = false;
			Vector2df newPos;
			try {
				int i = 0;
				do {
					i++;
					newPos = spawnPos.getAPoint(playerShape);

					boundings.setX(newPos.x);
					boundings.setY(newPos.y);

					if (i > ATTEMPT_PER_SPAWNPOINT) {
						break;
					}

					spawnPositionOkay = !isAnyOfObjectsWithinBounds(boundings,
							objects.values());
				} while (!spawnPositionOkay);

				if (spawnPositionOkay) {
					return new Vector2df(boundings.getX(), boundings.getY());
				} else {
					L.warning("There is no spawnpoint in the spawnposition at x : "
							+ spawnPos.getArea().getX()
							+ " y : "
							+ spawnPos.getArea().getY()
							+ " after "
							+ ATTEMPT_PER_SPAWNPOINT + " attempts.");
				}
			} catch (IllegalArgumentException e) {
				L.log(Level.SEVERE, e.getMessage(), e);
			}

		}

		throw new NoSpawnAvailableException();

	}

	/**
	 * Removes the team.
	 * 
	 * @param team
	 */
	public void removeTeam(Team team) {
		teams.remove(team.getTeamId());
	}

	/**
	 * Returns all objects of the given type.
	 * 
	 * @param type
	 *            an object must have this type.
	 * @return a collection of all objects of the given type.
	 */
	public Collection<GameObject> findObjectsByType(ObjectType type) {
		Collection<GameObject> objectsOfType = new LinkedList<GameObject>();
		Collection<GameObject> allObjects = getObjects().values();

		for (GameObject singleObject : allObjects) {
			if (singleObject.getType() == type) {
				objectsOfType.add(singleObject);
			}
		}

		return objectsOfType;
	}

	/**
	 * Remove a player.
	 * 
	 * @param clientId
	 *            the players clientId (ownerId).
	 */
	public void removePlayer(int clientId) {
		players.remove(clientId);
	}

	/**
	 * Searches for gameobjects at a given point in game.
	 * 
	 * @param point
	 *            the game coordinate.
	 * @return a list of objects that collide with that point.
	 */
	public LinkedList<GameObject> findObjectsAt(Vector2f point) {
		LinkedList<GameObject> objs = new LinkedList<GameObject>();
		for (GameObject object : objects.values()) {
			if (object.getType() == ObjectType.MAPBOUNDS)
				continue;
			if (object.getShape().contains(point.x, point.y)) {
				objs.add(object);
			}
		}
		return objs;
	}
}
