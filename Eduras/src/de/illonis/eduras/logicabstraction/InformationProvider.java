/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.Statistic;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.InfoInterface;
import de.illonis.eduras.units.Player;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.interfaces.InfoInterface#getMapBounds()
	 */
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
		return networkManager.getClient().getOwnerId();
	}

	@Override
	public Player getPlayer() throws ObjectNotFoundException {
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
	public void addEventListener(GameEventListener listener) {
		logic.addGameEventListener(listener);
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

}
