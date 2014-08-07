package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class SetTimeEvent extends GameEvent {

	private final static Logger L = EduLog.getLoggerFor(SetTimeEvent.class
			.getName());

	private final long remainingTime;

	public SetTimeEvent(GameEventNumber type, long remainingTime) {
		super(type);
		this.remainingTime = remainingTime;
		putArgument(remainingTime);
	}

	/**
	 * Returns the remaining time.
	 * 
	 * @return The remaining time in milliseconds.
	 */
	public long getRemainingTime() {
		return remainingTime;
	}

}
