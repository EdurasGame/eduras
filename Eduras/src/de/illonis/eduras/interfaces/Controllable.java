package de.illonis.eduras.interfaces;

import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

/**
 * Provides methods for controlling movement of this object.
 * 
 * @author illonis
 * 
 */
public interface Controllable {

	/**
	 * Starts moving this object into given direction. Movement in other
	 * directions is not affected.
	 * 
	 * @param direction
	 *            the moving direction.
	 * 
	 * @author illonis
	 */
	public void startMoving(Direction direction);

	/**
	 * Stops moving this object into given direction. Movement in other
	 * directions is not affected.
	 * 
	 * @param direction
	 *            the direction.
	 * 
	 * @author illonis
	 */
	public void stopMoving(Direction direction);

	/**
	 * Stops movement in all directions.
	 * 
	 * @author illonis
	 */
	public void stopMoving();

}
