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

	/**
	 * The team color identifies a team. There should never exist two teams with
	 * the same color at once. If you compare teams, they are compared by color.
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum TeamColor {
		RED, BLUE, NEUTRAL;
	}

	private TeamColor color;
	private String name;
	private final LinkedList<PlayerMainFigure> players;

	/**
	 * Creates a new team.
	 * 
	 * @param name
	 *            the name of the team.
	 * 
	 * @param color
	 *            the color of the team.
	 */
	public Team(String name, TeamColor color) {
		this.color = color;
		this.name = name;
		players = new LinkedList<PlayerMainFigure>();
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
		newPlayer.setTeam(this);
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
		playerToRemove.setTeam(null);
		players.remove(playerToRemove);
	}

	/**
	 * @return the team color.
	 * 
	 * @author illonis
	 */
	public TeamColor getColor() {
		return color;
	}

	/**
	 * @return a list with all players in the team.
	 * 
	 * @author illonis
	 */
	public LinkedList<PlayerMainFigure> getPlayers() {
		return players;
	}

	/**
	 * @return the name of this team.
	 * 
	 * @author illonis
	 */
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Team) {
			Team other = (Team) obj;
			return getColor().equals(other.getColor());
		}
		return super.equals(obj);
	}

}
