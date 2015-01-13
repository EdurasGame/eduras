package de.illonis.eduras.events;

import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;

/**
 * Indicates that the game mode has changed.
 * 
 * @author illonis
 * 
 */
public class SetGameModeEvent extends GameEvent {
	private final GameModeNumber newMode;

	/**
	 * Creates a new {@link SetGameModeEvent} event.
	 * 
	 * @param newMode
	 *            name of the new mode.
	 */
	public SetGameModeEvent(GameModeNumber newMode) {
		super(GameEventNumber.SET_GAMEMODE);
		this.newMode = newMode;

		putArgument(newMode);
	}

	/**
	 * Returns the name of the new mode.
	 * 
	 * @return the name of the new mode.
	 * 
	 * @author illonis
	 */
	public GameModeNumber getNewMode() {
		return newMode;
	}
}
