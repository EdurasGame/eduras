package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * A RoundEndEvent is sent when a team has won a round.
 * 
 * @author Florian 'Ren' Mai
 *
 */
public class RoundEndEvent extends GameEvent {
	private final static Logger L = EduLog.getLoggerFor(RoundEndEvent.class
			.getName());

	private final int winnerId;

	/**
	 * Create a new RoundEndEvent.
	 * 
	 * @param winnerId
	 */
	public RoundEndEvent(int winnerId) {
		super(GameEventNumber.ROUND_END);
		this.winnerId = winnerId;
		putArgument(winnerId);
	}

	/**
	 * Returns the id of the round winning team.
	 * 
	 * @return id
	 */
	public int getWinnerId() {
		return winnerId;
	}

}
