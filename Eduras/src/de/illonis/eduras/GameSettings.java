package de.illonis.eduras;

import java.util.LinkedList;

public class GameSettings {

	private GameInformation gameInfo;
	private GameMode gameMode;
	private long remainingTime;
	private NumberOfTeams numberOfTeams;
	private LinkedList<Team> teams;
	private Statistic stats;

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

		this.gameInfo = gameInfo;

		gameMode = new Deathmatch(gameInfo);
		remainingTime = 30000;
		numberOfTeams = NumberOfTeams.FFA;
		stats = new Statistic();
		teams = new LinkedList<Team>();
	}

	public void changeTime(long time) {
		remainingTime = time;
	}

	public void changeGameMode(GameMode newMode) {
		gameMode = newMode;
	}

	public void changeNumOfTeams(NumberOfTeams numOfTeams) {
		numberOfTeams = numOfTeams;
	}

	public String getGameModeName() {
		return gameMode.getName();
	}

	public NumberOfTeams getNumberOfTeams() {
		return numberOfTeams;
	}

	public long getRemainingTime() {
		return remainingTime;
	}

	public Statistic getStats() {
		return stats;
	}

	public LinkedList<Team> getTeams() {
		return teams;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

}
