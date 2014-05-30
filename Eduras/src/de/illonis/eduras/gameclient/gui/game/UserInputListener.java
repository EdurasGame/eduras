package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.geom.Vector2f;

/**
 * Listens for user input (e.g. key presses).
 * 
 * @author illonis
 * 
 */
public interface UserInputListener {

	/**
	 * Shows the statistics window.
	 * 
	 * @author illonis
	 */
	void showStatWindow();

	/**
	 * Hides the statistics window.
	 * 
	 * @author illonis
	 */
	void hideStatWindow();

	/**
	 * Triggered when ENTER is pressed in chat.
	 */
	void onChatEnter();

	/**
	 * Triggered when chat typing is aborted.
	 * 
	 * @return true if user was currently writing.
	 */
	boolean abortChat();

	/**
	 * Calculates current mouse position in gamePanel
	 * 
	 * @return mouse position computed to ingame point.
	 */
	Vector2f getCurrentMousePos();

	/**
	 * Sets the item of the given slot number as selected.
	 * 
	 * @param i
	 *            item slot.
	 */
	void selectItem(int i);

	/**
	 * Resets the camera.
	 */
	void resetCamera();

	/**
	 * Quits game.
	 */
	void onGameQuit();
	

}
