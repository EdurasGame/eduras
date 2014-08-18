package de.illonis.eduras.logicabstraction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.NoSpawnAvailableException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.InfoInterface;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.units.Observer;

/**
 * This class provides a connection between GUI and logic. GUI developers can
 * use the methods provided here to gain information about the current state of
 * the game to use these in your GUI.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class InformationProvider implements InfoInterface {
	private ClientData clientData;
	private EdurasInitializer edurasInitializer;
	private long timeTillRespawn;

	/**
	 * Creates a new InformationProvider that gains information with the given
	 * logic.
	 * 
	 * @param logic
	 *            The logic to gain information from.
	 */
	InformationProvider(EdurasInitializer edurasInitializer) {
		this.edurasInitializer = edurasInitializer;
		this.clientData = new ClientData();
	}

	@Override
	public Rectangle getMapBounds() {
		return edurasInitializer.logic.getGame().getMap().getBounds();
	}

	/**
	 * Returns owner id.
	 * 
	 * @return owner id.
	 */
	public int getOwnerID() {
		return edurasInitializer.networkManager.getClient().getClientId();
	}

	/**
	 * Returns map's NodeData.
	 * 
	 * @return node data
	 */
	public Collection<NodeData> getNodes() {
		de.illonis.eduras.maps.Map map = edurasInitializer.logic.getGame()
				.getMap();
		return map.getNodes();
	}

	/**
	 * Returns the name of the current map.
	 * 
	 * @return current's map name.
	 * 
	 * @author illonis
	 */
	public String getMapName() {
		return edurasInitializer.logic.getGame().getMap().getName();
	}

	@Override
	public Player getPlayer() throws ObjectNotFoundException {
		return edurasInitializer.logic.getGame().getPlayerByOwnerId(
				getOwnerID());
	}

	@Override
	public Map<Integer, GameObject> getGameObjects() {
		return edurasInitializer.logic.getGame().getObjects();
	}

	/**
	 * Adds an eventlistener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void setGameEventListener(GameEventListener listener) {
		edurasInitializer.logic.setGameEventListener(listener);
	}

	@Override
	public Statistic getStatistics() {
		return edurasInitializer.logic.getGame().getGameSettings().getStats();
	}

	@Override
	public GameMode getGameMode() {
		return edurasInitializer.logic.getGame().getGameSettings()
				.getGameMode();
	}

	@Override
	public Collection<Player> getPlayers() {
		return edurasInitializer.logic.getGame().getPlayers();
	}

	@Override
	public GameObject findObjectById(int id) throws ObjectNotFoundException {
		return edurasInitializer.logic.getGame().findObjectById(id);
	}

	@Override
	public Player getPlayerByOwnerId(int ownerId)
			throws ObjectNotFoundException {
		return edurasInitializer.logic.getGame().getPlayerByOwnerId(ownerId);
	}

	@Override
	public long getRemainingTime() {
		return edurasInitializer.logic.getGame().getGameSettings()
				.getRemainingTime();
	}

	@Override
	public Collection<Team> getTeams() {
		return edurasInitializer.logic.getGame().getTeams();
	}

	@Override
	public ClientData getClientData() {
		return clientData;
	}

	@Override
	public Collection<GameObject> findObjectsByType(ObjectType type) {
		return edurasInitializer.logic.getGame().findObjectsByType(type);
	}

	@Override
	public Collection<GameObject> findObjectsAt(Vector2f point) {
		return edurasInitializer.logic.getGame().findObjectsAt(point);
	}

	@Override
	public Team findTeamById(int teamId) {
		return edurasInitializer.logic.getGame().findTeamById(teamId);
	}

	@Override
	public long getRespawnTime() {
		return timeTillRespawn;
	}

	public void setRespawnTime(long time) {
		timeTillRespawn = time;
	}

	@Override
	public boolean fitsObjectInBase(ObjectType type, Base base) {
		GameObject unitToSpawn;
		switch (type) {
		case OBSERVER:
			unitToSpawn = new Observer(null, null, -1, -1);
			break;
		default:
			return false;
		}

		SpawnPosition spawnPosition = new SpawnPosition(
				(Rectangle) base.getShape(), SpawnPosition.SpawnType.ANY);
		Collection<GameObject> allObjectsExceptBase = new LinkedList<GameObject>(
				edurasInitializer.getLogic().getGame().getObjects().values());
		allObjectsExceptBase.remove(base);
		try {
			GameInformation
					.findFreePointWithinSpawnPositionForShape(spawnPosition,
							unitToSpawn.getShape(), allObjectsExceptBase);
			return true;
		} catch (NoSpawnAvailableException e) {
			return false;
		}

	}
}
