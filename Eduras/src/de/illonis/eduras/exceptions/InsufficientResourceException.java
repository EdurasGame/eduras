package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class InsufficientResourceException extends ActionFailedException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger L = EduLog
			.getLoggerFor(InsufficientResourceException.class.getName());

	public InsufficientResourceException(int cost) {
		super("You need at least " + cost
				+ " resources to perform this action!");
	}
}
