package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.exceptions.ActionFailedException;

/**
 * Provides key- and mouselisteners access to the user interface.
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

	/**
	 * Called when an action fails. Displays an error message on the
	 * notification panel and plays an error sound.
	 * 
	 * @param e
	 *            the related {@link ActionFailedException}.
	 */
	void onActionFailed(ActionFailedException e);

	/**
	 * Shows a notification to the user.
	 * 
	 * @param message
	 *            the message.
	 */
	void showNotification(String message);

	/**
	 * Shows a tip above action bar to the user.
	 * 
	 * @param message
	 *            the message.
	 */
	void showTip(String message);
	
	/**
	 * Shows a big prominent message.
	 * 
	 * @param message
	 *            the message.
	 */
	void showBigMessage(String message);

	/**
	 * Selectes given action button.
	 * 
	 * @param i
	 *            the button index.
	 */
	void selectActionButton(int i);

	
	/**
	 * Returns to main action bar in buildmode.
	 */
	void pageUp();

}
