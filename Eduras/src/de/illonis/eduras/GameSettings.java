package de.illonis.eduras;

import java.util.LinkedList;

public class GameSettings {

	private GameMode gameMode;
	private long remainingTime;
	private NumberOfTeams numberOfTeams;
	private LinkedList<Team> teams;
	private Statistic stats;

	public enum NumberOfTeams {

		FFA, ONE, MULTIPLE;

	}

	public GameSettings() {
		// TODO: Implemnt!
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

}
