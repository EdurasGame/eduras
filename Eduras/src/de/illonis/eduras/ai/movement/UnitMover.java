package de.illonis.eduras.ai.movement;

import de.illonis.eduras.math.Vector2D;

/**
 * Allows commanding of a unit and keeps track of pathfinder and motion of a
 * unit.
 * 
 * @author illonis
 * 
 */
public class UnitMover {
	private final PathFinder pathFinder;
	private Runnable currentMotion;
	private final MotionAIControllable motionTarget;

	public UnitMover(MotionType motionType, MotionAIControllable motionTarget) {
		pathFinder = PathFinderFactory.getFinderFor(motionType);
		this.motionTarget = motionTarget;
	}

	public void startMoving(Vector2D target) {

	}

	public void stopMovement() {

	}

	public boolean isActive() {
		return false;
	}

}
