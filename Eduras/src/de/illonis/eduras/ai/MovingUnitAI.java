package de.illonis.eduras.ai;

import de.illonis.eduras.ai.movement.MotionAIControllable;
import de.illonis.eduras.ai.movement.UnitMover;
import de.illonis.eduras.math.Vector2D;

public class MovingUnitAI extends UnitAI {
	private UnitMover mover;

	public MovingUnitAI(MotionAIControllable unit) {

	}

	public void stopMoving() {

	}

	public void setMoveDirection(Vector2D direction) {

	}

	public void shootAt(Vector2D direction) {

	}

	@Override
	public AIControllable getUnit() {
		return null;
	}
}
