package de.illonis.eduras;

import de.illonis.eduras.gamemodes.Deathmatch;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.NoGameMode;

/**
 * Holds all current game settings and statistics.
 * 
 * @author illonis
 * 
 */
public class GameSettings {

	private GameMode gameMode;
	private long roundTime;
	private long remainingTime;
	private Statistic stats;

	/**
	 * Creates standard gamesettings for the server with {@link NoGameMode}
	 * enabled.
	 * 
	 * @param gameInfo
	 *            The gameinformation to work on.
	 */
	public GameSettings(GameInformation gameInfo) {

		roundTime = 300000;
		gameMode = new Deathmatch(gameInfo);
		remainingTime = roundTime;
		stats = new Statistic();
	}

	/**
	 * Changes the remaining time.
	 * 
	 * @param time
	 *            new remaining time in ms.
	 * 
	 * @author illonis
	 */
	public void changeTime(long time) {
		remainingTime = time;
	}

	/**
	 * Sets remaining time to roundtime.
	 */
	public void resetRemainingTime() {
		remainingTime = roundTime;
	}

	/**
	 * Changes the game mode.
	 * 
	 * @param newMode
	 *            the new game mode.
	 * 
	 * @author illonis
	 */
	public void changeGameMode(GameMode newMode) {
		gameMode = newMode;
	}

	/**
	 * Returns remaining time.
	 * 
	 * @return the remaining time in ms.
	 * 
	 * @author illonis
	 */
	public long getRemainingTime() {
		return remainingTime;
	}

	/**
	 * Returns the statistics.
	 * 
	 * @return the statistics object.
	 * 
	 * @author illonis
	 */
	public Statistic getStats() {
		return stats;
	}

	/**
	 * Returns the current game mode.
	 * 
	 * @return the current game mode.
	 * 
	 * @author illonis
	 */
	public GameMode getGameMode() {
		return gameMode;
	}
}
