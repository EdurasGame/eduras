package de.illonis.eduras;

import java.util.LinkedList;

import org.newdawn.slick.Color;

import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.utils.Randomizer;

/**
 * Represents a team of players.
 * 
 * @author illonis
 * 
 */
public class Team {

	private static int nextTeamId = 0;

	private final int teamId;
	private Color color;
	private final String name;
	private final LinkedList<PlayerMainFigure> players;
	private int resourceCount;

	/**
	 * @return the next free id for a new team.
	 */
	public static synchronized int getNextTeamId() {
		return nextTeamId++;
	}

	/**
	 * Creates a new team with random color.
	 * 
	 * @param name
	 *            the name of the team.
	 * 
	 * @param teamId
	 *            the id of the team.
	 */
	public Team(String name, int teamId) {
		this(name, teamId, Randomizer.getRandomColor());
	}

	/**
	 * @param name
	 *            the name of the team.
	 * @param teamId
	 *            the id of the team.
	 * @param color
	 *            the team color.
	 */
	public Team(String name, int teamId, Color color) {
		this.teamId = teamId;
		this.name = name;
		players = new LinkedList<PlayerMainFigure>();
		this.color = color;
	}

	/**
	 * @return the id of this team.
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @param color
	 *            the color of this team.
	 */
	public void setColor(Color color) {
		this.color = color;
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
		if (players.contains(newPlayer))
			return;
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
	public Color getColor() {
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
			return getTeamId() == other.getTeamId();
		}
		return super.equals(obj);
	}

	/**
	 * Returns the amount of resources that this team has available.
	 * 
	 * @return resources
	 */
	public int getResourceCount() {
		return resourceCount;
	}

	/**
	 * Sets the amount of resources this team has available.
	 * 
	 * @param resourceCount
	 */
	public void setResourceCount(int resourceCount) {
		this.resourceCount = resourceCount;
	}
}
