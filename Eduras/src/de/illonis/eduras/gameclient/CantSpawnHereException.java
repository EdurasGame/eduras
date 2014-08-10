package de.illonis.eduras.gameclient;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ActionFailedException;

public class CantSpawnHereException extends ActionFailedException {

	private final static Logger L = EduLog
			.getLoggerFor(CantSpawnHereException.class.getName());

	public CantSpawnHereException(ObjectType objectType) {
		super("Can't spawn an object of type " + objectType + " here.");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
