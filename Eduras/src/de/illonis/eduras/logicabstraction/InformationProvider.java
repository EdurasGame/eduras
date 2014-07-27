package de.illonis.eduras.logicabstraction;

import java.util.Collection;
import java.util.Map;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.InfoInterface;
import de.illonis.eduras.maps.EduraMap;
import de.illonis.eduras.maps.NodeData;

/**
 * This class provides a connection between GUI and logic. GUI developers can
 * use the methods provided here to gain information about the current state of
 * the game to use these in your GUI.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class InformationProvider implements InfoInterface {
	private final GameLogicInterface logic;
	private final NetworkManager networkManager;
	private ClientData clientData;

	/**
	 * Creates a new InformationProvider that gains information with the given
	 * logic.
	 * 
	 * @param logic
	 *            The logic to gain information from.
	 */
	InformationProvider(GameLogicInterface logic, NetworkManager networkManager) {
		this.logic = logic;
		this.networkManager = networkManager;
		this.clientData = new ClientData();
	}

	@Override
	public Rectangle getMapBounds() {
		return logic.getGame().getMap().getBounds();
	}

	/**
	 * Returns owner id.
	 * 
	 * @return owner id.
	 */
	public int getOwnerID() {
		return networkManager.getClient().getClientId();
	}

	/**
	 * If the running map is an Edura! map, it's NodeData are returned.
	 * 
	 * @return node data
	 * @throws IllegalArgumentException
	 *             Thrown if the running map is NOT an Edura! map.
	 */
	public Collection<NodeData> getNodes() throws IllegalArgumentException {
		de.illonis.eduras.maps.Map map = logic.getGame().getMap();
		if (!(map instanceof EduraMap)) {
			throw new IllegalArgumentException(
					"The current map is no Edura! map, so there are no nodes");
		}

		EduraMap eduraMap = (EduraMap) map;
		return eduraMap.getNodes();
	}

	/**
	 * Returns the name of the current map.
	 * 
	 * @return current's map name.
	 * 
	 * @author illonis
	 */
	public String getMapName() {
		return logic.getGame().getMap().getName();
	}

	@Override
	public Player getPlayer() throws ObjectNotFoundException {
		return logic.getGame().getPlayerByOwnerId(getOwnerID());
	}

	@Override
	public Map<Integer, GameObject> getGameObjects() {
		return logic.getGame().getObjects();
	}

	/**
	 * Adds an eventlistener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void setGameEventListener(GameEventListener listener) {
		logic.setGameEventListener(listener);
	}

	@Override
	public Statistic getStatistics() {
		return logic.getGame().getGameSettings().getStats();
	}

	@Override
	public GameMode getGameMode() {
		return logic.getGame().getGameSettings().getGameMode();
	}

	@Override
	public Collection<Player> getPlayers() {
		return logic.getGame().getPlayers();
	}

	@Override
	public GameObject findObjectById(int id) {
		return logic.getGame().findObjectById(id);
	}

	@Override
	public Player getPlayerByOwnerId(int ownerId)
			throws ObjectNotFoundException {
		return logic.getGame().getPlayerByOwnerId(ownerId);
	}

	@Override
	public long getRemainingTime() {
		return logic.getGame().getGameSettings().getRemainingTime();
	}

	@Override
	public Collection<Team> getTeams() {
		return logic.getGame().getTeams();
	}

	@Override
	public ClientData getClientData() {
		return clientData;
	}

	@Override
	public Collection<GameObject> findObjectsByType(ObjectType type) {
		return logic.getGame().findObjectsByType(type);
	}

	@Override
	public Collection<GameObject> findObjectsAt(Vector2f point) {
		return logic.getGame().findObjectsAt(point);
	}
}
