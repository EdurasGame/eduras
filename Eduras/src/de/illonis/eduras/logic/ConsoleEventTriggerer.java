package de.illonis.eduras.logic;

import java.util.Collection;

import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.Deathmatch;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.NoGameMode;
import de.illonis.eduras.maps.ManyBlocks;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.SimpleMap;
import de.illonis.eduras.networking.Server;
import de.illonis.eduras.units.Player;

/**
 * Triggers events from console.
 * 
 * @author illonis
 * 
 */
public class ConsoleEventTriggerer {

	private ServerEventTriggerer triggerer;
	private Server server;

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
			Server server) {
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

		server.handleClientDisconnect(server.getClientById(ownerId));

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
		triggerer.respawnPlayer(player);

		return true;
	}

	/**
	 * Changes the game mode and restarts the round afterwards.
	 * 
	 * @param gameModeName
	 *            The name of the game mode to switch to.
	 * @return false, if game mode name couldnt be found.
	 */
	public boolean changeGameMode(String gameModeName) {

		GameMode gameMode = null;
		switch (gameModeName.toLowerCase()) {
		case "deathmatch":
			gameMode = new Deathmatch(triggerer.getGameInfo());
			break;
		default:
			gameMode = new NoGameMode(triggerer.getGameInfo());
			break;
		}

		triggerer.changeGameMode(gameMode);
		if (gameMode instanceof NoGameMode)
			return false;
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
		Map map = null;
		switch (mapName.toLowerCase()) {
		case "simple":
			map = new SimpleMap();
			break;
		case "manyblocks":
			map = new ManyBlocks();
			break;
		default:
		}

		if (map == null) {
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
	 * Shuts the server, so it won't send, receive or listen to any incoming
	 * messages or clients anymore.
	 */
	public void shutDown() {
		server.stopServer();
	}
}
