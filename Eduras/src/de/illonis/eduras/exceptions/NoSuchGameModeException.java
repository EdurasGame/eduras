package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;

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

	private final GameModeNumber gameModeNumber;

	/**
	 * Create a new NoSuchGameModeException.
	 * 
	 * @param gameModeThatWasntFound
	 *            The {@link GameModeNumber} of the {@link GameMode} that wasn't
	 *            found.
	 */
	public NoSuchGameModeException(GameModeNumber gameModeThatWasntFound) {
		gameModeNumber = gameModeThatWasntFound;
	}

	public NoSuchGameModeException() {
		gameModeNumber = GameModeNumber.NO_GAMEMODE;
	}

	/**
	 * Returns the {@link GameModeNumber} for which the corresponding
	 * {@link GameMode} couldn't be found.
	 * 
	 * @return the name of the game mode
	 */
	public GameModeNumber getGameModeName() {
		return gameModeNumber;
	}
}
