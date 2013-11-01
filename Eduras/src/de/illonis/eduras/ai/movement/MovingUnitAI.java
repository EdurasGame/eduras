package de.illonis.eduras.ai.movement;

import de.illonis.eduras.ai.AIControllable;
import de.illonis.eduras.ai.UnitAI;
import de.illonis.eduras.math.Vector2D;

/**
 * A basic unit ai that supports moving and shooting.
 * 
 * @author illonis
 * 
 */
public class MovingUnitAI implements UnitAI {
	private final UnitMover mover;
	private final MotionAIControllable unit;

	public MovingUnitAI(MotionAIControllable unit) {
		mover = new UnitMover(unit);
		this.unit = unit;
	}

	public void stopMoving() {
		mover.stopMovement();
	}

	public void moveTo(Vector2D direction) {
		mover.startMoving(direction);
	}

	public void shootAt(Vector2D direction) {
		// TODO: implement
	}

	@Override
	public AIControllable getUnit() {
		return unit;
	}
}
