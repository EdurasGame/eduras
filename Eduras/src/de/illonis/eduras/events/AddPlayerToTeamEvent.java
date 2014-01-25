package de.illonis.eduras.events;

import de.illonis.eduras.Team.TeamColor;

/**
 * Adds a player to a given team.
 * 
 * @author illonis
 * 
 */
public class AddPlayerToTeamEvent extends OwnerGameEvent {

	private final TeamColor teamColor;

	/**
	 * Creates a new event with given data.
	 * 
	 * @param ownerId
	 *            the owner id of the player.
	 * @param color
	 *            the color of the team.
	 */
	public AddPlayerToTeamEvent(int ownerId, TeamColor color) {
		super(GameEventNumber.ADD_PLAYER_TO_TEAM, ownerId);
		this.teamColor = color;
		putArgument(color.toString());
	}

	/**
	 * @return the color of the team.
	 * 
	 * @author illonis
	 */
	public TeamColor getTeamColor() {
		return teamColor;
	}
}
