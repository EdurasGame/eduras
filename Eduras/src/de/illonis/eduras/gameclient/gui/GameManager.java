package de.illonis.eduras.gameclient.gui;

import de.illonis.eduras.gameclient.EdurasGameInterface;

/**
 * Provides access to the container holding the {@link EdurasGameInterface}.
 * 
 * @author illonis
 * 
 */
public interface GameManager {

	/**
	 * Indicates that player disconnected from the game.
	 * 
	 * @param gracefully
	 *            true if disconnect was not intended.
	 * @param message
	 *            an optional error message.
	 */
	void onDisconnect(boolean gracefully, String message);

	/**
	 * This is called when the game's initialization is completed so starting to
	 * render the game is legit
	 */
	void onGameReady();
}
