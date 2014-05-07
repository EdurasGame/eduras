package de.illonis.eduras.events;

/**
 * Sets the amount of resources for a specific team.
 * 
 * @author illonis
 * 
 */
public class SetTeamResourceEvent extends GameEvent {

	private final int team;
	private final int newAmount;

	/**
	 * Create the event.
	 * 
	 * @param teamId
	 *            The id of the team to set the resource count of
	 * @param newAmount
	 *            the amount to set the resource count to
	 */
	public SetTeamResourceEvent(int teamId, int newAmount) {
		super(GameEventNumber.SET_TEAM_RESOURCE);
		this.team = teamId;
		this.newAmount = newAmount;
		putArgument(team);
		putArgument(newAmount);
	}

	/**
	 * Returns the new resource amount.
	 * 
	 * @return amount
	 */
	public int getNewAmount() {
		return newAmount;
	}

	/**
	 * Returns the team's id to set the resource of
	 * 
	 * @return id
	 */
	public int getTeamId() {
		return team;
	}

}
