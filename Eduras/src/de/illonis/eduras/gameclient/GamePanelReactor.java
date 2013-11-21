package de.illonis.eduras.gameclient;

import java.awt.geom.Rectangle2D;

import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Handles interactions from graphical user interface that should be passed
 * either to logic or server.
 * 
 * @author illonis
 * 
 */
public interface GamePanelReactor {

	/**
	 * Triggers an item use.
	 * 
	 * @param slotId
	 *            the slot that's item should be used.
	 * @param target
	 *            the target location.
	 */
	void onItemUse(int slotId, Vector2D target);

	/**
	 * Triggers start of movement of clients {@link PlayerMainFigure}.
	 * 
	 * @param direction
	 *            the movement direction.
	 */
	void onStartMovement(Direction direction);

	/**
	 * Triggers stop of movement of clients {@link PlayerMainFigure}.
	 * 
	 * @param direction
	 *            the movement direction.
	 */
	void onStopMovement(Direction direction);

	/**
	 * Triggers that player dragged a rectangle to select multiple units.
	 * 
	 * @param area
	 *            the rectangle that has been drawn.
	 */
	void onUnitsSelected(Rectangle2D.Double area);

	/**
	 * Triggers users wish of quitting the game.
	 */
	void onGameQuit();

	/**
	 * Indicates a mode switch.
	 */
	void onModeSwitch();

	/**
	 * Indicates a simple click at given position. This should either select a
	 * single unit (if any is at that point) or remove selection.
	 * 
	 * @param point
	 *            the point where user clicked.
	 */
	void selectOrDeselectAt(Vector2D point);

	/**
	 * Indicates to trigger a send units event to server with currently selected
	 * units and given target.
	 * 
	 * @param target
	 *            the target location.
	 */
	void sendSelectedUnits(Vector2D target);

}
