package de.illonis.eduras.logic;

import java.util.Collection;

import de.illonis.eduras.units.Player;

/**
 * Triggers events from console.
 * 
 * @author illonis
 * 
 */
public class ConsoleEventTriggerer {

	private ServerEventTriggerer triggerer;

	public ConsoleEventTriggerer(ServerEventTriggerer serverTriggerer) {
		this.triggerer = serverTriggerer;
	}

	public Collection<Player> getPlayers() {
		// TODO: Implement
		return null;
	}

	public boolean kickPlayerById(int id) {
		// TODO: Implement
		return false;
	}

	public boolean respawnPlayerById(int id) {
		// TODO: Implement
		return false;
	}

	public boolean changeGameMode(String gameModeName) {
		// TODO: Implement
		return false;
	}

	public boolean changeMap(String mapName) {
		// TODO: Implement
		return false;
	}

	public boolean restartRound() {
		// TODO: Implement
		return false;
	}
}
