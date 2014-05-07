package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * This exception is thrown if a team doesn't have sufficient resources to
 * perform an action.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class InsufficientResourceException extends ActionFailedException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger L = EduLog
			.getLoggerFor(InsufficientResourceException.class.getName());

	/**
	 * Creates a new InsufficienctResourceException.
	 * 
	 * @param cost
	 *            The amount of resources that would be needed to perform the
	 *            action.
	 */
	public InsufficientResourceException(int cost) {
		super("You need at least " + cost
				+ " resources to perform this action!");
	}
}
