package de.illonis.eduras.exceptions;

import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.maps.Map;

/**
 * Occurs when a game mode is tried to be applied while a map that does not
 * support this mode is active.
 * 
 * @author illonis
 * 
 */
public class GameModeNotSupportedByMapException extends Exception {

	private static final long serialVersionUID = 1L;
	private final GameMode mode;
	private final Map map;

	/**
	 * @param mode
	 *            the applied game mode.
	 * @param map
	 *            the map that does not support the applied game mode.
	 */
	public GameModeNotSupportedByMapException(GameMode mode, Map map) {
		super("Gamemode " + mode.getName() + " is not supported by "
				+ map.getName());
		this.map = map;
		this.mode = mode;
	}

	/**
	 * @return the mode that has been applied.
	 */
	public GameMode getMode() {
		return mode;
	}

	/**
	 * @return the map that does not support the mode.
	 */
	public Map getMap() {
		return map;
	}
}
