package de.illonis.eduras.shapes;

import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2df;

/**
 * A simple shape that is empty and has no collision.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public final class NoCollisionShape extends ObjectShape {

	@Override
	public Vector2df checkCollisionOnMove(GameInformation game,
			GameObject thisObject, Vector2df target) {
		return target;
	}

	@Override
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {
		return null;
	}

	@Override
	public Vector2df[] getBorderPoints() {
		return null;
	}

	@Override
	public ObjectShape getScaled(double scale) {
		return this;
	}

	@Override
	public double checkCollisionOnRotation(GameInformation gameInfo,
			GameObject thisObject, double rotationAngle) {
		return rotationAngle;
	}
}
