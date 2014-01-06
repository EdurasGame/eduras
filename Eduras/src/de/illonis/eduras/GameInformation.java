package de.illonis.eduras;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.AddPlayerToTeamEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.exceptions.GameModeNotSupportedByMapException;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.FunMap;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Holds all game information of the current game.
 * 
 * @author illonis
 * 
 */
public class GameInformation {
	private final static Logger L = EduLog.getLoggerFor(GameInformation.class
			.getName());

	private static final Random RANDOM = new Random();

	private final ConcurrentHashMap<Integer, GameObject> objects;
	private final ConcurrentHashMap<Integer, PlayerMainFigure> players;
	private Map map;
	private EventTriggerer eventTriggerer;
	private GameSettings gameSettings;
	private final LinkedList<Team> teams;
	private final HashMap<Team, SpawnType> spawnGroups;

	/**
	 * Creates a new game information object with emtpy object lists.
	 */
	public GameInformation() {
		objects = new ConcurrentHashMap<Integer, GameObject>();
		players = new ConcurrentHashMap<Integer, PlayerMainFigure>();
		map = new FunMap();
		gameSettings = new GameSettings(this);
		teams = new LinkedList<Team>();
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
	public Collection<PlayerMainFigure> getPlayers() {
		return players.values();
	}

	/**
	 * Returns all teams.
	 * 
	 * @return a copy of the list of all teams.
	 * 
	 * @author illonis
	 */
	public LinkedList<Team> getTeams() {
		return new LinkedList<Team>(teams);
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
		teams.add(team);
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
	public ConcurrentHashMap<Integer, GameObject> getObjects() {
		return objects;
	}

	/**
	 * Checks if there will be a collision of the given object trying to move to
	 * the target position.
	 * 
	 * @param gameObject
	 *            The object which wants to move.
	 * @param target
	 *            The target position.
	 * @return Returns the objects position after the move. Note that the
	 *         objects new position won't be set.
	 */
	@Deprecated
	public Vector2D checkCollision(GameObject gameObject, Vector2D target) {
		ObjectShape shape = gameObject.getShape();
		Vector2D result = shape.checkCollisionOnMove(this, gameObject, target);
		return result;
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
	 * Returns a list of all gameobjects that are in range of a given position.
	 * 
	 * @param point
	 *            the location to search at.
	 * @param radius
	 *            the search radius.
	 * @return a list of nearby objects.
	 */
	public LinkedList<GameObject> findObjectsInDistance(Vector2D point,
			double radius) {
		// TODO: improve (using position is rather incorrect due to object's
		// dimensions)
		LinkedList<GameObject> objs = new LinkedList<GameObject>();
		for (GameObject object : objects.values()) {
			if (point.calculateDistance(object.getPositionVector()) <= radius) {
				objs.add(object);
			}
		}
		return objs;
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

		if (go instanceof PlayerMainFigure) {
			playerRemoveSuccess = players.remove(go.getOwner()) != null;
		}

		return playerRemoveSuccess && (objects.remove(go.getId()) != null);
	}

	/**
	 * Adds a player to playerlist. Players are stored assigned to their
	 * owner-id.
	 * 
	 * @param player
	 *            player to add.
	 */
	public void addPlayer(PlayerMainFigure player) {
		players.put(player.getOwner(), player);
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
	public PlayerMainFigure getPlayerByOwnerId(int ownerId)
			throws ObjectNotFoundException {
		PlayerMainFigure result = players.get(ownerId);
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
		for (PlayerMainFigure singlePlayer : players.values()) {
			if (singlePlayer.getId() == objectId) {
				result = singlePlayer;
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

		for (GameObject object : objects.values()) {
			ObjectFactoryEvent objectEvent = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_CREATE, object.getType());
			objectEvent.setOwner(object.getOwner());
			objectEvent.setId(object.getId());
			infos.add(objectEvent);

			// send position immediately
			MovementEvent me = new MovementEvent(GameEventNumber.SET_POS_TCP,
					object.getId());
			me.setNewXPos(object.getXPosition());
			me.setNewYPos(object.getYPosition());
			infos.add(me);

			// send visible / collidable status
			SetBooleanGameObjectAttributeEvent visEvent = new SetBooleanGameObjectAttributeEvent(
					GameEventNumber.SET_VISIBLE, object.getId(),
					object.isVisible());
			SetBooleanGameObjectAttributeEvent colEvent = new SetBooleanGameObjectAttributeEvent(
					GameEventNumber.SET_COLLIDABLE, object.getId(),
					object.isCollidable());
			infos.add(colEvent);
			infos.add(visEvent);

		}
		for (PlayerMainFigure p : players.values()) {
			try {
				infos.add(new ClientRenameEvent(p.getOwner(), p.getName()));
			} catch (InvalidNameException e) {
				L.log(Level.WARNING, e.getLocalizedMessage(), e);
				continue;
			}
		}
		SetGameModeEvent e = new SetGameModeEvent(gameSettings.getGameMode()
				.getName());
		infos.add(e);
		SetRemainingTimeEvent remaining = new SetRemainingTimeEvent(
				gameSettings.getRemainingTime());
		infos.add(remaining);

		// send statistics
		Statistic stats = gameSettings.getStats();

		for (PlayerMainFigure player : players.values()) {
			int killsOfPlayer = stats.getKillsOfPlayer(player);
			SetIntegerGameObjectAttributeEvent setKillsEvent = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SET_KILLS, player.getOwner(), killsOfPlayer);
			infos.add(setKillsEvent);

			int deathsOfPlayer = stats.getDeathsOfPlayer(player);
			SetIntegerGameObjectAttributeEvent setDeathsEvent = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SET_DEATHS, player.getOwner(),
					deathsOfPlayer);
			infos.add(setDeathsEvent);
		}

		SetTeamsEvent teamEvent = new SetTeamsEvent();
		LinkedList<AddPlayerToTeamEvent> teamPlayerEvents = new LinkedList<AddPlayerToTeamEvent>();
		for (Team team : getTeams()) {
			teamEvent.addTeam(team.getColor(), team.getName());
			for (PlayerMainFigure player : team.getPlayers()) {
				teamPlayerEvents.add(new AddPlayerToTeamEvent(
						player.getOwner(), team.getColor()));
			}
		}

		infos.add(teamEvent);
		infos.addAll(teamPlayerEvents);

		return infos;
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

	/**
	 * Checks whether any gameobject is within given bounds.
	 * 
	 * @param bounds
	 *            rectangular shape.
	 * @return true if object is in bounds.,
	 * 
	 * @author illonis
	 */
	public boolean isObjectWithin(Rectangle2D bounds) {
		for (GameObject o : objects.values()) {
			if (o.getBoundingBox().intersects(bounds))
				return true;
		}
		return false;
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
	 */
	public Vector2D getSpawnPointFor(PlayerMainFigure player)
			throws GameModeNotSupportedByMapException {

		SpawnType spawnType = spawnGroups.get(player.getTeam());

		LinkedList<SpawnPosition> availableSpawnings = new LinkedList<SpawnPosition>();

		LinkedList<SpawnPosition> spawnAreas = new LinkedList<SpawnPosition>(
				getMap().getSpawnAreas());
		for (int i = 0; i < spawnAreas.size(); i++) {
			SpawnPosition p = spawnAreas.get(i);
			if (p.getTeaming() == spawnType)
				availableSpawnings.add(p);
		}

		if (availableSpawnings.size() == 0)
			throw new GameModeNotSupportedByMapException(getGameSettings()
					.getGameMode(), getMap());

		int area = RANDOM.nextInt(availableSpawnings.size());
		SpawnPosition spawnPos = availableSpawnings.get(area);
		Rectangle2D.Double boundings = new Rectangle2D.Double();
		boundings.width = player.getBoundingBox().width;
		boundings.height = player.getBoundingBox().height;

		Vector2D newPos;
		do {
			newPos = spawnPos.getAPoint(player.getShape());
			boundings.x = newPos.getX();
			boundings.y = newPos.getY();
		} while (isObjectWithin(boundings));

		return new Vector2D(boundings.x, boundings.y);
	}
}
