package de.illonis.eduras;

import java.util.LinkedList;

import de.illonis.eduras.units.Player;

/**
 * Represents a team of players.
 * 
 * @author illonis
 * 
 */
public class Team {

	private LinkedList<Player> players;

	/**
	 * Creates a new team.
	 * 
	 * @param players
	 *            initial player list.
	 */
	public Team(LinkedList<Player> players) {
		this.players = players;
	}

	/**
	 * Adds a player to this team.
	 * 
	 * @param newPlayer
	 *            the new player.
	 * 
	 * @author illonis
	 */
	public void addPlayer(Player newPlayer) {
		players.add(newPlayer);
	}

	/**
	 * Removes a player from this team.
	 * 
	 * @param playerToRemove
	 *            the player to remove.
	 * 
	 * @author illonis
	 */
	public void removePlayer(Player playerToRemove) {
		players.remove(playerToRemove);
	}

}
