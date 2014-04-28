package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class NotWithinBaseException extends ActionFailedException {
	private final static Logger L = EduLog
			.getLoggerFor(NotWithinBaseException.class.getName());

	public NotWithinBaseException() {
		super(
				"Cannot switch to interact mode because the player is not located within a neutral base of his own.");
	}
}
