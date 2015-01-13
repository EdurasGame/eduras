package de.illonis.eduras.logic;

import java.io.File;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.ServerInterface;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.NoSuchGameModeException;
import de.illonis.eduras.exceptions.NoSuchMapException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.BasicGameMode;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.persistence.InvalidDataException;

/**
 * Triggers events from console.
 * 
 * @author illonis
 * 
 */
public class ConsoleEventTriggerer {

	private final static Logger L = EduLog
			.getLoggerFor(ConsoleEventTriggerer.class.getName());

	private final ServerEventTriggerer triggerer;
	private final ServerInterface server;

	/**
	 * Creates a new ConsoleEventTriggerer that uses the information and
	 * functionality provided by serverTriggerer.
	 * 
	 * @param serverTriggerer
	 *            the servertriggerer.
	 * @param server
	 *            the server.
	 */
	public ConsoleEventTriggerer(ServerEventTriggerer serverTriggerer,
			ServerInterface server) {
		this.triggerer = serverTriggerer;
		this.server = server;
	}

	/**
	 * Returns a collection containing all online players.
	 * 
	 * @return list of players.
	 */
	public Collection<Player> getPlayers() {
		return triggerer.getGameInfo().getPlayers();
	}

	/**
	 * Kicks the player with the given id from server. In detail, this means
	 * that its related objects are removed from gameInformation and all open
	 * connections from and to the client are closed.
	 * 
	 * @param ownerId
	 *            The players ownerId.
	 * @return false, if the player couldnt be found.
	 */
	public boolean kickPlayerById(int ownerId) {
		try {
			triggerer.getGameInfo().getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			return false;
		}

		triggerer.kickPlayer(ownerId);

		return true;
	}

	/**
	 * Randomly respawns the player that has the given ownerId.
	 * 
	 * @param ownerId
	 * @return false, if the player couldnt be found.
	 */
	public boolean respawnPlayerById(int ownerId) {
		Player player;
		try {
			player = triggerer.getGameInfo().getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			return false;
		}
		triggerer.respawnPlayerAtRandomSpawnpoint(player);

		return true;
	}

	/**
	 * Changes the game mode and restarts the round afterwards.
	 * 
	 * @param gameModeName
	 *            The name of the game mode to switch to.
	 * @return false, if game mode name couldn't be found.
	 */
	public boolean changeGameMode(String gameModeName) {

		GameModeNumber gameModeNumber;
		try {
			gameModeNumber = GameModeNumber.valueOf(gameModeName);
		} catch (IllegalArgumentException e) {
			L.log(Level.WARNING, "Tried to change gamemode to " + gameModeName
					+ " which doesn't exist.");
			return false;
		}

		GameMode gameMode;
		try {
			gameMode = BasicGameMode.getGameModeByNumber(gameModeNumber,
					triggerer.getGameInfo());
		} catch (NoSuchGameModeException e) {
			L.log(Level.WARNING, "Tried to change gamemode to " + gameModeName
					+ " which doesn't exist.", e);
			return false;
		}

		triggerer.changeGameMode(gameMode);
		return true;
	}

	/**
	 * Changes map to given map. If no map with given name is found, map is not
	 * changed.
	 * 
	 * @param mapName
	 *            the name of the new map.
	 * @return true if map changed, false otherwise.
	 * 
	 * @author illonis
	 */
	public boolean changeMap(String mapName) {
		Map map;
		try {
			map = Map.getMapByName(mapName);
		} catch (NoSuchMapException e) {
			L.log(Level.INFO, "Tried to change map to " + mapName
					+ " which doesn't exist", e);
			return false;
		} catch (InvalidDataException e) {
			L.log(Level.SEVERE, "Mapfile for map  " + mapName
					+ " contains errors.", e);
			return false;
		}

		triggerer.changeMap(map);
		return true;
	}

	/**
	 * Restarts round of current map.
	 * 
	 * @return true if successful.
	 * 
	 * @author illonis
	 */
	public boolean restartRound() {
		triggerer.restartRound();
		return true;
	}

	/**
	 * Restarts the game.
	 * 
	 * @return true if successful
	 */
	public boolean restartGame() {
		triggerer.restartGame();
		return true;
	}

	/**
	 * Shuts the server, so it won't send, receive or listen to any incoming
	 * messages or clients anymore.
	 */
	public void shutDown() {
		server.stop();
	}

	/**
	 * Loads the file at the given path, parses it, sends it to the clients and
	 * calls {@link #restartRound()}.
	 * 
	 * @param path
	 *            The path to the file to load.
	 * @throws IllegalArgumentException
	 *             Thrown if there is no file at the given path.
	 */
	public void loadSettings(String path) throws IllegalArgumentException {
		File settingsFile = new File(path);
		if (!settingsFile.exists()) {
			throw new IllegalArgumentException("File at path " + path
					+ " doesnt exist.");
		}

		triggerer.loadSettings(settingsFile);
		restartRound();
	}

	/**
	 * Set a setting property to a certain value and notify the clients about
	 * it.
	 * 
	 * @param propertyName
	 * @param propertyValue
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 *             thrown if the value given doesn't apply to the property's
	 *             type
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 *             thrown if there is no such property
	 */
	public void setSetting(String propertyName, String propertyValue)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		triggerer.setSetting(propertyName, propertyValue);
	}
}
