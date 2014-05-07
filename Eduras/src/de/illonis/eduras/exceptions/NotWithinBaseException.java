package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Thrown if a player attempts to switch to strategy mode but is not located
 * within a neutral base.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NotWithinBaseException extends ActionFailedException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger L = EduLog
			.getLoggerFor(NotWithinBaseException.class.getName());

	/**
	 * Create a new {@link NotWithinBaseException}.
	 */
	public NotWithinBaseException() {
		super(
				"Cannot switch to interact mode because the player is not located within a neutral base of his own.");
	}
}
