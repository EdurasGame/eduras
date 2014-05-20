package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class NoSpawnAvailableException extends Exception {
	private final static Logger L = EduLog
			.getLoggerFor(NoSpawnAvailableException.class.getName());

	public NoSpawnAvailableException() {
		super("Couldn't find a suitable spawnpoint.");
	}
}
