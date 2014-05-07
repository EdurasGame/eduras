package de.illonis.eduras.exceptions;

import de.illonis.eduras.actions.RTSAction;

/**
 * This exception is thrown if the execution of an {@link RTSAction} fails for
 * some reason.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ActionFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new ActionFailedException with the given message explaining what
	 * failed.
	 * 
	 * @param message
	 *            The message
	 */
	public ActionFailedException(String message) {
		super(message);
	}

}
