package de.illonis.eduras;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Holds all statistics data.
 * 
 * @author illonis
 * 
 */
public class Statistic {

	/**
	 * This enum represents the different kinds of statistics.
	 * 
	 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
	 * 
	 */
	public enum StatsProperty {
		/**
		 * Number of kills
		 */
		KILLS,
		/**
		 * Number of deaths
		 */
		DEATHS
	}

	private final HashMap<Integer, Integer> teamScore;

	public static class PlayerStatEntry implements Comparable<PlayerStatEntry> {
		private final Player player;
		private final HashMap<StatsProperty, Integer> stats;

		PlayerStatEntry(Player player) {
			stats = new HashMap<StatsProperty, Integer>();
			this.player = player;
			for (StatsProperty prop : StatsProperty.values()) {
				stats.put(prop, 0);
			}
		}

		@Override
		public int compareTo(PlayerStatEntry o) {
			int kills = this.stats.get(StatsProperty.KILLS);
			int deaths = this.stats.get(StatsProperty.DEATHS);
			int otherKills = o.stats.get(StatsProperty.KILLS);
			int otherDeaths = o.stats.get(StatsProperty.DEATHS);
			if (kills > otherKills)
				return -1;
			if (kills < otherKills)
				return 1;
			if (deaths < otherDeaths)
				return -1;
			if (deaths > otherDeaths)
				return 1;
			return 0;
		}

		public int getProperty(StatsProperty property) {
			return stats.get(property);
		}

		public void setProperty(StatsProperty property, int value) {
			stats.put(property, value);
		}

		public Player getPlayer() {
			return player;
		}

		@Override
		public int hashCode() {
			return player.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PlayerStatEntry) {
				return ((PlayerStatEntry) obj).player.equals(this.player);
			}
			return false;
		}

		public void reset() {
			for (StatsProperty property : StatsProperty.values()) {
				setProperty(property, 0);
			}
		}
	}

	/**
	 * Returns a teams score.
	 * 
	 * @param team
	 *            team to get score of
	 * @return score as integer
	 */
	public int getScoreOfTeam(Team team) {
		if (!teamScore.containsKey(team.getTeamId())) {
			teamScore.put(team.getTeamId(), 0);
		}
		return teamScore.get(team.getTeamId());
	}

	/**
	 * Sets a team's score.
	 * 
	 * @param team
	 * @param newScore
	 */
	public void setScoreOfTeam(Team team, int newScore) {
		teamScore.put(team.getTeamId(), newScore);
	}

	public ArrayList<PlayerStatEntry> getStatList() {
		return stats;
	}

	private ArrayList<PlayerStatEntry> stats;

	/**
	 * Creates a new empty statistic.
	 * 
	 */
	public Statistic() {
		stats = new ArrayList<PlayerStatEntry>();
		teamScore = new HashMap<Integer, Integer>();
	}

	private PlayerStatEntry findForOwner(int ownerId) {
		for (int i = 0; i < stats.size(); i++) {
			PlayerStatEntry entry = stats.get(i);
			if (entry.player.getPlayerId() == ownerId)
				return entry;
		}
		return null;
	}

	private PlayerStatEntry findForPlayer(Player player) {
		for (int i = 0; i < stats.size(); i++) {
			PlayerStatEntry entry = stats.get(i);
			if (entry.player.equals(player))
				return entry;
		}
		PlayerStatEntry entry = new PlayerStatEntry(player);
		stats.add(entry);
		return entry;
	}

	/**
	 * Returns the number of kills of the given player.
	 * 
	 * @param player
	 *            The player
	 * @return The number of kills.
	 */
	public int getKillsOfPlayer(Player player) {
		PlayerStatEntry entry = findForPlayer(player);
		return entry.getProperty(StatsProperty.KILLS);
	}

	/**
	 * Set a {@link StatsProperty} of a player identified by the given id to the
	 * given value.
	 * 
	 * @param prop
	 * @param player
	 * @param newVal
	 */
	public void setStatsProperty(StatsProperty prop, Player player, int newVal) {
		findForPlayer(player).setProperty(prop, newVal);
		Collections.sort(stats);
	}

	/**
	 * Get a {@link StatsProperty} of a player identified by the given id.
	 * 
	 * @param prop
	 * @param player
	 * 
	 * @return Returns the value
	 */
	public int getStatsProperty(StatsProperty prop, Player player) {
		return findForPlayer(player).getProperty(prop);
	}

	/**
	 * Returns the number of deaths of the given player.
	 * 
	 * @param player
	 *            The player
	 * @return The number of deaths.
	 */
	public int getDeathsOfPlayer(Player player) {
		return getStatsProperty(StatsProperty.DEATHS, player);
	}

	/**
	 * Increments the death count of the given player.
	 * 
	 * @param player
	 */
	public void addDeathForPlayer(Player player) {
		incrementProperty(player, StatsProperty.DEATHS);
	}

	public void incrementProperty(Player player, StatsProperty property) {
		int oldValue = getStatsProperty(property, player);
		setStatsProperty(property, player, oldValue + 1);
	}

	/**
	 * Increments the kill count of the given player.
	 * 
	 * @param player
	 */
	public void addKillForPlayer(Player player) {
		incrementProperty(player, StatsProperty.KILLS);
	}

	/**
	 * Returns the a collection of those players with the most frags.
	 * 
	 * @param amongPlayers
	 *            the players to consider
	 * 
	 * @return list of best players
	 */
	public Collection<Player> findPlayersWithMostFrags(
			Collection<Player> amongPlayers) {
		LinkedList<Player> playersWithMostFrags = new LinkedList<Player>();
		Collections.sort(stats);
		if (stats.size() == 0)
			return playersWithMostFrags;
		int maxKills = stats.get(0).getProperty(StatsProperty.KILLS);
		int minDeaths = stats.get(0).getProperty(StatsProperty.DEATHS);
		playersWithMostFrags.add(stats.get(0).player);
		for (int j = 1; j < stats.size(); j++) {
			int kills = stats.get(j).getProperty(StatsProperty.KILLS);
			int deaths = stats.get(j).getProperty(StatsProperty.DEATHS);
			if (kills == maxKills && deaths == minDeaths) {
				playersWithMostFrags.add(stats.get(j).player);
			} else {
				break;
			}
		}
		return playersWithMostFrags;
	}

	/**
	 * Adds a player to the frag count and initializes his count with 0.
	 * 
	 * @param player
	 *            The player.
	 */
	public void addPlayerToStats(Player player) {
		findForPlayer(player);
	}

	/**
	 * Resets all statistics, which means that for all players every count is
	 * set to zero.
	 */
	public void reset() {
		for (int i = 0; i < stats.size(); i++) {
			stats.get(i).reset();
		}
	}

	/**
	 * Sets the kill count of a player.
	 * 
	 * @param player
	 * @param newCount
	 */
	public void setKills(Player player, int newCount) {
		setStatsProperty(StatsProperty.KILLS, player, newCount);
	}

	/**
	 * Sets the death count of a player.
	 * 
	 * @param player
	 * @param newCount
	 */
	public void setDeaths(Player player, int newCount) {
		setStatsProperty(StatsProperty.DEATHS, player, newCount);
	}

	/**
	 * Removes a player from the frag and death count.
	 * 
	 * @param ownerId
	 *            The ownerId of the player to be removed from the stats.
	 */
	public void removePlayerFromStats(int ownerId) {
		stats.remove(findForOwner(ownerId));
	}

	/**
	 * Returns number of kills of a team.
	 * 
	 * @param team
	 *            the team.
	 * @return kills of all players of given team summarized.
	 */
	public int getKillsByTeam(Team team) {
		int kills = 0;
		for (Player player : team.getPlayers()) {
			kills += getKillsOfPlayer(player);
		}
		return kills;
	}

	public void resetStatsFor(Player player) {
		findForPlayer(player).reset();
	}

	/**
	 * Creates a static deep copy that is not updated anymore.
	 * 
	 * @return a copy of current stat data.
	 */
	public Statistic copy() {
		Statistic copy = new Statistic();
		for (int i = 0; i < stats.size(); i++) {
			PlayerStatEntry entry = stats.get(i);
			PlayerStatEntry copyEntry = new PlayerStatEntry(entry.getPlayer());
			for (StatsProperty statType : StatsProperty.values()) {
				copyEntry.setProperty(statType, entry.getProperty(statType));
			}
			copy.stats.add(copyEntry);
		}
		return copy;
	}
}
