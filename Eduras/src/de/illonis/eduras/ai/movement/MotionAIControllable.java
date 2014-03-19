package de.illonis.eduras.ai.movement;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ai.AIControllable;

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
	void startMovingTo(Vector2f direction);

	/**
	 * Stops moving this entity.
	 */
	void stopMoving();

	/**
	 * @return current position of this entity.
	 */
	Vector2f getPosition();

	/**
	 * @return the motion type of this entity.
	 */
	MotionType getMotionType();

}
