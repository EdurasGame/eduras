package de.illonis.eduras.events;

import java.util.LinkedList;

import de.illonis.eduras.Team;

/**
 * Sets the playing teams. Can hold only one team for each color.
 * 
 * @author illonis
 * 
 */
public class SetTeamsEvent extends GameEvent {

	private LinkedList<Team> teamList;

	/**
	 * Creates a new SetTeamsEvent with empty dataset.
	 */
	public SetTeamsEvent() {
		super(GameEventNumber.SET_TEAMS);
		teamList = new LinkedList<Team>();
	}

	/**
	 * Creates a new SetTeamsEvent with one initial team.
	 * 
	 * @param team
	 *            the first team.
	 */
	public SetTeamsEvent(Team team) {
		this();
		addTeam(team);
	}

	/**
	 * Adds a team to the teamlist of this event.
	 * 
	 * @param team
	 *            the new team.
	 * 
	 * @author illonis
	 */
	public void addTeam(Team team) {
		teamList.add(team);
		putArgument(team.getName());
		putArgument(team.getTeamId());
		putArgument(team.getColor().getRGB());
	}

	/**
	 * Returns all teams contained.
	 * 
	 * @return a hashmap of all teams.
	 * 
	 * @author illonis
	 */
	public LinkedList<Team> getTeamList() {
		return teamList;
	}

	@Override
	public int getNumberOfArguments() {
		return teamList.size() * 3;
	}

}
