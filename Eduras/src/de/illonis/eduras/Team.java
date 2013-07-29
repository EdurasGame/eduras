package de.illonis.eduras;

import java.util.LinkedList;

import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Represents a team of players.
 * 
 * @author illonis
 * 
 */
public class Team {

	private LinkedList<PlayerMainFigure> players;

	/**
	 * Creates a new team.
	 * 
	 * @param players
	 *            initial player list.
	 */
	public Team(LinkedList<PlayerMainFigure> players) {
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
	public void addPlayer(PlayerMainFigure newPlayer) {
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
	public void removePlayer(PlayerMainFigure playerToRemove) {
		players.remove(playerToRemove);
	}

}
