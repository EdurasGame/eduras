package de.illonis.eduras.ai.movement;

import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;

/**
 * Allows commanding of a unit and keeps track of pathfinder and motion of a
 * unit.
 * 
 * @author illonis
 * 
 */
public final class UnitMover {

	private final PathFinder pathFinder;
	private Thread currentMotion;
	private final MotionAIControllable motionUnit;

	UnitMover(MotionAIControllable motionTarget) {
		pathFinder = PathFinderFactory.getFinderFor(motionTarget
				.getMotionType());
		this.motionUnit = motionTarget;
	}

	void startMoving(Vector2D target) {
		pathFinder.setTarget(target);
		if (!isActive()) {
			currentMotion = new MoverThread();
			currentMotion.start();
		}
	}

	void stopMovement() {
		currentMotion.interrupt();
	}

	boolean isActive() {
		if (currentMotion == null)
			return false;
		return currentMotion.isAlive();
	}

	private class MoverThread extends Thread {

		public MoverThread() {
			super("MoverThread");
		}

		@Override
		public void run() {
			while (!pathFinder.hasReachedTarget()) {
				pathFinder.setLocation(motionUnit.getPosition());
				motionUnit.startMovingTo(pathFinder.getMovingDirection());
				try {
					Thread.sleep(S.ai_motion_update_interval);
				} catch (InterruptedException e) {
					break;
				}
			}
			motionUnit.stopMoving();
		}
	}
}
