package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Team;

public class SetScoreEvent extends GameEvent {

	private final static Logger L = EduLog.getLoggerFor(SetScoreEvent.class
			.getName());

	private final Team team;
	private final int score;

	public SetScoreEvent(Team team, int score) {
		super(GameEventNumber.SET_SCORE);

		this.team = team;
		this.score = score;
		putArgument(team.getTeamId());
		putArgument(score);
	}

	public int getScore() {
		return score;
	}

	public Team getTeam() {
		return team;
	}
}
