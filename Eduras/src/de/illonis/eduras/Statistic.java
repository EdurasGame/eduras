package de.illonis.eduras;

import java.util.Collection;
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

	private final HashMap<StatsProperty, HashMap<Integer, Integer>> stats;

	/**
	 * Creates a new empty statistic.
	 * 
	 */
	public Statistic() {
		stats = new HashMap<StatsProperty, HashMap<Integer, Integer>>();

		stats.put(StatsProperty.KILLS, new HashMap<Integer, Integer>());
		stats.put(StatsProperty.DEATHS, new HashMap<Integer, Integer>());
	}

	/**
	 * Returns the number of kills of the given player.
	 * 
	 * @param player
	 *            The player
	 * @return The number of kills.
	 */
	public int getKillsOfPlayer(Player player) {
		HashMap<Integer, Integer> kills = stats.get(StatsProperty.KILLS);
		if (kills.containsKey(player.getPlayerId()))
			return kills.get(player.getPlayerId());
		return 0;
	}

	/**
	 * Set a {@link StatsProperty} of a player identified by the given id to the
	 * given value.
	 * 
	 * @param prop
	 * @param ownerId
	 * @param newVal
	 */
	public void setStatsProperty(StatsProperty prop, int ownerId, int newVal) {
		stats.get(prop).put(ownerId, newVal);
	}

	/**
	 * Get a {@link StatsProperty} of a player identified by the given id.
	 * 
	 * @param prop
	 * @param ownerId
	 * 
	 * @return Returns the value
	 */
	public int getStatsProperty(StatsProperty prop, int ownerId) {
		return stats.get(prop).get(ownerId);
	}

	/**
	 * Returns the number of deaths of the given player.
	 * 
	 * @param player
	 *            The player
	 * @return The number of deaths.
	 */
	public int getDeathsOfPlayer(Player player) {
		HashMap<Integer, Integer> deaths = stats.get(StatsProperty.DEATHS);
		if (deaths.containsKey(player.getPlayerId()))
			return deaths.get(player.getPlayerId());
		return 0;
	}

	/**
	 * Increments the death count of the given player.
	 * 
	 * @param player
	 */
	public void addDeathForPlayer(Player player) {
		HashMap<Integer, Integer> deaths = stats.get(StatsProperty.DEATHS);
		deaths.put(player.getPlayerId(), getDeathsOfPlayer(player) + 1);
	}

	/**
	 * Increments the kill count of the given player.
	 * 
	 * @param player
	 */
	public void addKillForPlayer(Player player) {
		HashMap<Integer, Integer> kills = stats.get(StatsProperty.KILLS);
		kills.put(player.getPlayerId(), getKillsOfPlayer(player) + 1);
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
		int maxFrags = 0;
		HashMap<Integer, Integer> killsOfPlayer = stats
				.get(StatsProperty.KILLS);

		for (Player player : amongPlayers) {

			if (killsOfPlayer.get(player.getPlayerId()) == maxFrags) {
				playersWithMostFrags.add(player);
			}
			if (killsOfPlayer.get(player.getPlayerId()) > maxFrags) {
				playersWithMostFrags.clear();
				playersWithMostFrags.add(player);
				maxFrags = killsOfPlayer.get(player.getPlayerId());
			}
		}

		return playersWithMostFrags;
	}

	/**
	 * Adds a player to the frag count and initializes his count with 0.
	 * 
	 * @param ownerId
	 *            The id of the player.
	 */
	public void addPlayerToStats(int ownerId) {

		for (HashMap<Integer, Integer> statProp : stats.values()) {
			statProp.put(ownerId, 0);
		}
	}

	/**
	 * Resets all statistics, which means that for all players every count is
	 * set to zero.
	 */
	public void reset() {
		HashMap<Integer, Integer> kills = stats.get(StatsProperty.KILLS);

		LinkedList<Integer> players = new LinkedList<Integer>(kills.keySet());

		for (HashMap<Integer, Integer> aStat : stats.values()) {
			aStat.clear();
		}

		for (Integer player : players) {
			addPlayerToStats(player);
		}

	}

	/**
	 * Sets the kill count of a player.
	 * 
	 * @param playerId
	 * @param newCount
	 */
	public void setKills(int playerId, int newCount) {
		setStatsProperty(StatsProperty.KILLS, playerId, newCount);
	}

	/**
	 * Sets the death count of a player.
	 * 
	 * @param playerId
	 * @param newCount
	 */
	public void setDeaths(int playerId, int newCount) {
		setStatsProperty(StatsProperty.DEATHS, playerId, newCount);
	}

	/**
	 * Removes a player from the frag and death count.
	 * 
	 * @param ownerId
	 *            The id of the player to be removed from the stats.
	 */
	public void removePlayerFromStats(int ownerId) {
		for (HashMap<Integer, Integer> aStat : stats.values()) {
			aStat.remove(ownerId);
		}
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
}
