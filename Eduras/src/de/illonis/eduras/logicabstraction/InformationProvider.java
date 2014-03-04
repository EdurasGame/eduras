/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.Statistic;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.InfoInterface;
import de.illonis.eduras.units.PlayerMainFigure;

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
	}

	@Override
	public Rectangle getMapBounds() {
		return new Rectangle(logic.getGame().getMap().getBounds());
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
	public PlayerMainFigure getPlayer() throws ObjectNotFoundException {
		return logic.getGame().getPlayerByOwnerId(getOwnerID());
	}

	@Override
	public ConcurrentHashMap<Integer, GameObject> getGameObjects() {
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
	public Collection<PlayerMainFigure> getPlayers() {
		return logic.getGame().getPlayers();
	}

	@Override
	public GameObject findObjectById(int id) {
		return logic.getGame().findObjectById(id);
	}

	@Override
	public PlayerMainFigure getPlayerByOwnerId(int ownerId)
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

}
