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
	 * Creates a new empty statistic.
	 */
	public Statistic() {
		killsOfPlayer = new HashMap<Integer, Integer>();
	}

	/**
	 * Returns the number of kills of the given player.
	 * 
	 * @param player
	 *            The player
	 * @return The number of kills.
	 */
	public int getKillsOfPlayer(Player player) {
		return killsOfPlayer.get(player);
	}

	/**
	 * Increments the kill count of the given player.
	 * 
	 * @param player
	 */
	public void addKillForPlayer(Player player) {

		killsOfPlayer.put(player.getOwner(),
				killsOfPlayer.get(player.getOwner()) + 1);

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
	}

}
