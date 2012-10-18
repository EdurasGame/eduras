package de.illonis.eduras.interfaces;

import de.illonis.eduras.MoveableGameObject.Direction;

/**
 * A moveable object.
 * 
 * @author illonis
 * 
 */
public interface Moveable {

	/**
	 * indicates a movement event on watched object.
	 * 
	 * @param direction
	 *            direction of movement.
	 * @see Direction
	 */
	void onMove(Direction direction);

}
