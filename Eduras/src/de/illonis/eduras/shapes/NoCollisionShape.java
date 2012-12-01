package de.illonis.eduras.shapes;

import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * A simple shape that is empty and has no collision.
 * 
 * @author illonis
 * 
 */
public class NoCollisionShape extends ObjectShape {

	@Override
	public Vector2D checkCollision(GameInformation game, GameObject thisObject,
			Vector2D target) {
		return target;
	}

	@Override
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {
		return null;
	}

}
