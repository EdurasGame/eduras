package de.illonis.eduras;

import java.util.HashMap;

import de.illonis.eduras.units.Player;

/**
 * Holds all statistics data.
 * 
 * @author illonis
 * 
 */
public class Statistic {

	/**
	 * Maps the owner id of a player to his kill count.
	 */
	private HashMap<Integer, Integer> killsOfPlayer;

	/**
	 * Maps the owner id of a player to his death count.
	 */
	private HashMap<Integer, Integer> deathsOfPlayer;

	/**
	 * Creates a new empty statistic.
	 */
	public Statistic() {
		killsOfPlayer = new HashMap<Integer, Integer>();
		deathsOfPlayer = new HashMap<Integer, Integer>();
	}

	/**
	 * Returns the number of kills of the given player.
	 * 
	 * @param player
	 *            The player
	 * @return The number of kills.
	 */
	public int getKillsOfPlayer(Player player) {
		if (killsOfPlayer.containsKey(player.getOwner()))
			return killsOfPlayer.get(player.getOwner());
		return 0;
	}

	/**
	 * Returns the number of deaths of the given player.
	 * 
	 * @param player
	 *            The player
	 * @return The number of deaths.
	 */
	public int getDeathsOfPlayer(Player player) {
		if (deathsOfPlayer.containsKey(player.getOwner()))
			return deathsOfPlayer.get(player.getOwner());
		return 0;
	}

	/**
	 * Increments the death count of the given player.
	 * 
	 * @param player
	 */
	public void addDeathForPlayer(Player player) {
		deathsOfPlayer.put(player.getOwner(), getDeathsOfPlayer(player) + 1);
	}

	/**
	 * Increments the kill count of the given player.
	 * 
	 * @param player
	 */
	public void addKillForPlayer(Player player) {
		killsOfPlayer.put(player.getOwner(), getKillsOfPlayer(player) + 1);
		System.out.println(getKillsOfPlayer(player));
	}

	/**
	 * Returns the owner id of the player with the most frags.
	 * 
	 * @return the best player's owner id.
	 */
	public int findPlayerWithMostFrags() {
		int maxPlayerId = -1;
		int maxFrags = 0;

		for (Integer playerId : killsOfPlayer.keySet()) {
			if (killsOfPlayer.get(playerId) > maxFrags) {
				maxFrags = killsOfPlayer.get(playerId);
				maxPlayerId = playerId;
			}
		}

		return maxPlayerId;
	}

	/**
	 * Adds a player to the frag count and initializes his count with 0.
	 * 
	 * @param ownerId
	 *            The id of the player.
	 */
	public void addPlayerToStats(int ownerId) {
		killsOfPlayer.put(ownerId, 0);
		deathsOfPlayer.put(ownerId, 0);
	}

}
