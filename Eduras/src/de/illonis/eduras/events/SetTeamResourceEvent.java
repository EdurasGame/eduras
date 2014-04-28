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

	public SetTeamResourceEvent(int teamId, int newAmount) {
		super(GameEventNumber.SET_TEAM_RESOURCE);
		this.team = teamId;
		this.newAmount = newAmount;
		putArgument(team);
		putArgument(newAmount);
	}

	public int getNewAmount() {
		return newAmount;
	}

	public int getTeamId() {
		return team;
	}

}
