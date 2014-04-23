package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class SetResourcesEvent extends GameEvent {

	private final static Logger L = EduLog.getLoggerFor(SetResourcesEvent.class
			.getName());

	private final int teamId;
	private final int newAmount;

	public SetResourcesEvent(int teamId, int newAmount) {
		super(GameEventNumber.SET_RESOURCES);
		this.teamId = teamId;
		this.newAmount = newAmount;

		putArgument(teamId);
		putArgument(newAmount);
	}

	/**
	 * The team this event is concerned with.
	 * 
	 * @return teamid
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * The new resourcecount of the team.
	 * 
	 * @return count
	 */
	public int getNewAmount() {
		return newAmount;
	}
}
