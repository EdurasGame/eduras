package de.illonis.eduras.gameclient;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ActionFailedException;

public class InsufficientChargesException extends ActionFailedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger L = EduLog
			.getLoggerFor(InsufficientChargesException.class.getName());

	public InsufficientChargesException(String skillName) {
		super("You don't have enough charges for " + skillName + " available.");
	}

}
