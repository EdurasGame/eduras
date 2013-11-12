package de.illonis.eduras.gameclient;

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
	 * Triggers users wish of quitting the game.
	 */
	void onGameQuit();

	/**
	 * Indicates a mode switch.
	 */
	void onModeSwitch();
}
