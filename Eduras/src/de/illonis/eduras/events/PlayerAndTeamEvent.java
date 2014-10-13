package de.illonis.eduras.events;

/**
 * An event regarding a player and a team.
 * 
 * @author illonis
 * 
 */
public class PlayerAndTeamEvent extends OwnerGameEvent {

	private final int team;

	/**
	 * Creates a new event with given data.
	 * 
	 * @param type
	 *            the type of event
	 * 
	 * @param ownerId
	 *            the owner id of the player.
	 * @param targetTeam
	 *            the id of the team.
	 */
	public PlayerAndTeamEvent(GameEventNumber type, int ownerId, int targetTeam) {
		super(type, ownerId);
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
