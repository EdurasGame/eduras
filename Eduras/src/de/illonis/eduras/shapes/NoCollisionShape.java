package de.illonis.eduras.shapes;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * A simple shape that is empty and has no collision.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public final class NoCollisionShape extends ObjectShape {

	@Override
	public Vector2D checkCollisionOnMove(GameInformation game,
			GameObject thisObject, Vector2D target) {
		return target;
	}

	@Override
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {
		return null;
	}

	@Override
	public Double getBoundingBox() {
		return new Rectangle2D.Double();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.shapes.ObjectShape#getBorderPoints()
	 */
	@Override
	public Vector2D[] getBorderPoints() {
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
