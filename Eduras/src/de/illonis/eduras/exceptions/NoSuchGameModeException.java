package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gamemodes.GameMode;

/**
 * Thrown when there is no such {@link GameMode} for a given name.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NoSuchGameModeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger L = EduLog
			.getLoggerFor(NoSuchGameModeException.class.getName());

	private final String gameModeName;

	/**
	 * Create a new NoSuchGameModeException.
	 * 
	 * @param gameModeThatWasntFound
	 *            The name of the {@link GameMode} that wasn't found.
	 */
	public NoSuchGameModeException(String gameModeThatWasntFound) {
		gameModeName = gameModeThatWasntFound;
	}

	/**
	 * Returns the string for which the corresponding {@link GameMode} couldn't
	 * be found.
	 * 
	 * @return the name of the game mode
	 */
	public String getGameModeName() {
		return gameModeName;
	}
}
