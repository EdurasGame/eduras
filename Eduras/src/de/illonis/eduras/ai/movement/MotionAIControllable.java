package de.illonis.eduras.ai.movement;

import de.illonis.eduras.ai.AIControllable;
import de.illonis.eduras.math.Vector2D;

public interface MotionAIControllable extends AIControllable {

	void moveToDirection(Vector2D direction);

	void stopMoving();

	MotionType getMotionType();

}
