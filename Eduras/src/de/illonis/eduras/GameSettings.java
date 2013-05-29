package de.illonis.eduras;

import java.util.LinkedList;

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
	private NumberOfTeams numberOfTeams;
	private LinkedList<Team> teams;
	private Statistic stats;

	/**
	 * Team numbers.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum NumberOfTeams {
		FFA, ONE, MULTIPLE;
	}

	/**
	 * Creates standard gamesettings for the server with {@link NoGameMode}
	 * enabled.
	 * 
	 * @param gameInfo
	 *            The gameinformation to work on.
	 */
	public GameSettings(GameInformation gameInfo) {

		roundTime = 30000;
		gameMode = new Deathmatch(gameInfo);
		remainingTime = roundTime;
		numberOfTeams = NumberOfTeams.FFA;
		stats = new Statistic();
		teams = new LinkedList<Team>();
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
	 * Changes number of teams.
	 * 
	 * @param numOfTeams
	 *            new team number.
	 * 
	 * @author illonis
	 */
	public void changeNumOfTeams(NumberOfTeams numOfTeams) {
		numberOfTeams = numOfTeams;
	}

	/**
	 * Returns the number of teams.
	 * 
	 * @return the team count.
	 * 
	 * @author illonis
	 */
	public NumberOfTeams getNumberOfTeams() {
		return numberOfTeams;
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
	 * Returns all teams.
	 * 
	 * @return a list of all teams.
	 * 
	 * @author illonis
	 */
	public LinkedList<Team> getTeams() {
		return teams;
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
