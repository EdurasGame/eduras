package de.illonis.eduras.ai.movement;

import org.newdawn.slick.geom.Vector2f;

/**
 * Allows commanding of a unit and keeps track of pathfinder and motion of a
 * unit.
 * 
 * @author illonis
 * 
 */
public final class UnitMover {

	private final PathFinder pathFinder;
	private final MotionAIControllable motionUnit;
	private boolean active;

	UnitMover(MotionAIControllable motionTarget) {
		active = false;
		pathFinder = PathFinderFactory.getFinderFor(motionTarget
				.getMotionType());
		this.motionUnit = motionTarget;
	}

	void startMoving(Vector2f target) {
		pathFinder.setTarget(target);
		active = true;
	}

	void stopMovement() {
		motionUnit.startMovingTo(new Vector2f());
		pathFinder.setTarget(motionUnit.getPosition());
		active = false;
	}

	/**
	 * @return true if movement AI is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Checks for new targets.
	 */
	public void check() {
		pathFinder.setLocation(motionUnit.getPosition());
		if (pathFinder.hasReachedTarget()) {
			motionUnit.stopMoving();
			return;
		}
		motionUnit.startMovingTo(pathFinder.getMovingDirection());
	}
}
