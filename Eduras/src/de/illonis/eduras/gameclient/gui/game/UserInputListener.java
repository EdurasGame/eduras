package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.DragSelectionRectangle;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;

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
	 * Sets the item in given slot as selected if that slot is not empty.
	 * 
	 * @param slot
	 *            item slot.
	 * @throws ItemSlotIsEmptyException
	 *             if item slot is empty.
	 */
	void selectItem(int slot) throws ItemSlotIsEmptyException;

	/**
	 * Sets the item of given type as selected.
	 * 
	 * @param type
	 *            item type.
	 */
	void selectItem(ObjectType type);

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
	 * Ask the user to quit the game.
	 */
	void cancel();

	/**
	 * Displays a select team dialogue.
	 */
	void showSelectTeam();

	/**
	 * Switches to next page in strategy mode.
	 */
	void nextPage();

	/**
	 * Switches to previous page in strategy mode.
	 */
	void prevPage();

	/**
	 * Switches to given page in strategy mode.
	 * 
	 * @param page
	 *            page index.
	 */
	void setPage(int page);

	/**
	 * Selects next item in inventory.
	 */
	void selectPreviousItem();

	/**
	 * Selects previous item in inventory.
	 */
	void selectNextItem();

	void startCameraMovement(Direction dir, int type);

	void stopCameraMovement(Direction dir, int type);

	void stopCameraMovement();

	void setCameraPosition(Vector2f gamePos) throws ObjectNotFoundException;

	/**
	 * @return width of game container.
	 */
	int getContainerWidth();

	/**
	 * @return height of game container.
	 */
	int getContainerHeight();

	/**
	 * @param state
	 *            the new click state.
	 */
	void setClickState(ClickState state);

	/**
	 * Computes a point that is relative to gui into game coordinates.
	 * 
	 * @param v
	 *            point to convert.
	 * @return game-coordinate point.
	 */
	Vector2f computeGuiPointToGameCoordinate(Vector2f v);

	/**
	 * @return returns drag rectangle.
	 */
	DragSelectionRectangle getDragRect();

	/**
	 * @return the current click state.
	 */
	ClickState getClickState();

	/**
	 * Returns the {@link ClientData}.
	 * 
	 * @return clientdata
	 */
	ClientData getClientData();

	/**
	 * @return the tooltip handler.
	 */
	TooltipHandler getTooltipHandler();

}
