package de.illonis.eduras.events;

import java.util.HashMap;

import de.illonis.eduras.Team.TeamColor;

/**
 * Sets the playing teams. Can hold only one team for each color.
 * 
 * @author illonis
 * 
 */
public class SetTeamsEvent extends GameEvent {

	private final HashMap<TeamColor, String> teamList;

	/**
	 * Creates a new SetTeamsEvent with empty dataset.
	 */
	public SetTeamsEvent() {
		super(GameEventNumber.SET_TEAMS);
		teamList = new HashMap<TeamColor, String>();
	}

	/**
	 * Creates a new SetTeamsEvent with one initial team.
	 * 
	 * @param color
	 *            the color of the first team.
	 * @param name
	 *            the name of the first team.
	 */
	public SetTeamsEvent(TeamColor color, String name) {
		this();
		addTeam(color, name);
	}

	/**
	 * Adds a team to the teamlist of this event. An existing team with the same
	 * color will be overwritten.
	 * 
	 * @param color
	 *            color of the new team.
	 * @param name
	 *            name of the new team.
	 * 
	 * @author illonis
	 */
	public void addTeam(TeamColor color, String name) {
		teamList.put(color, name);
	}

	/**
	 * Returns all teams contained.
	 * 
	 * @return a hashmap of all teams.
	 * 
	 * @author illonis
	 */
	public HashMap<TeamColor, String> getTeamList() {
		return teamList;
	}

}
