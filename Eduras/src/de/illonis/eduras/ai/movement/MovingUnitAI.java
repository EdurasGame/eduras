package de.illonis.eduras.ai.movement;

import de.illonis.eduras.ai.AIControllable;
import de.illonis.eduras.ai.UnitAI;
import de.illonis.eduras.math.Vector2D;

/**
 * A basic unit AI that supports moving and shooting.
 * 
 * @author illonis
 * 
 */
public class MovingUnitAI implements UnitAI {
	private final UnitMover mover;
	private final MotionAIControllable unit;

	/**
	 * Creates a new motion AI.
	 * 
	 * @param unit
	 *            controlled unit.
	 */
	public MovingUnitAI(MotionAIControllable unit) {
		mover = new UnitMover(unit);
		this.unit = unit;
	}

	/**
	 * Stops movement of controlled unit.
	 */
	public void stopMoving() {
		mover.stopMovement();
	}

	/**
	 * Starts moving controlled unit to given target. This method uses waypoints
	 * generated by a {@link PathFinder}.
	 * 
	 * @param direction
	 *            the target.
	 */
	public void moveTo(Vector2D direction) {
		mover.startMoving(direction);
	}

	/**
	 * Commands the controlled unit to shoot at given direction.
	 * 
	 * @param direction
	 *            shooting direction.
	 */
	public void shootAt(Vector2D direction) {
		// TODO: implement
	}

	@Override
	public AIControllable getUnit() {
		return unit;
	}

	@Override
	public void discard() {
		mover.stopMovement();
	}
}
