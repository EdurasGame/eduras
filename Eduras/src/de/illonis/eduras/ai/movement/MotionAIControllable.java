package de.illonis.eduras.ai.movement;

import de.illonis.eduras.ai.AIControllable;
import de.illonis.eduras.math.Vector2df;

/**
 * A motion controllable entity.
 * 
 * @author illonis
 * 
 */
public interface MotionAIControllable extends AIControllable {

	/**
	 * Starts moving this entity into given direction (respectively sets the
	 * speedvector).
	 * 
	 * @param direction
	 *            target direction.
	 */
	void startMovingTo(Vector2df direction);

	/**
	 * Stops moving this entity.
	 */
	void stopMoving();

	/**
	 * @return current position of this entity.
	 */
	Vector2df getPosition();

	/**
	 * @return the motion type of this entity.
	 */
	MotionType getMotionType();

}
