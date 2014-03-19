package de.illonis.eduras.events;

/**
 * Adds a player to a given team.
 * 
 * @author illonis
 * 
 */
public class AddPlayerToTeamEvent extends OwnerGameEvent {

	private final int team;

	/**
	 * Creates a new event with given data.
	 * 
	 * @param ownerId
	 *            the owner id of the player.
	 * @param targetTeam
	 *            the id of the team.
	 */
	public AddPlayerToTeamEvent(int ownerId, int targetTeam) {
		super(GameEventNumber.ADD_PLAYER_TO_TEAM, ownerId);
		this.team = targetTeam;
		putArgument(team);
	}

	/**
	 * @return the id of the team.
	 */
	public int getTeam() {
		return team;
	}
}
