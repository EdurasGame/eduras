/**
 * 
 */
package de.illonis.eduras.shapes;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Triangle extends ObjectShape {

	Vector2D[] vertices;

	/**
	 * Creates a triangle with the 3 given vertices. The vertices are given as
	 * vectors relative to the center point of the object behind.
	 * 
	 * @param first
	 *            The first vertex' relative position.
	 * @param second
	 *            The second vertex relative position.
	 * @param third
	 *            The third vertex relative position.
	 * @throws ShapeVerticesNotApplicableException
	 *             Is thrown if the given vertices do not enclose the objects
	 *             centre point.
	 */
	public Triangle(Vector2D first, Vector2D second, Vector2D third)
			throws ShapeVerticesNotApplicableException {

		boolean isPositiveX = first.getX() > 0 || second.getX() > 0
				|| third.getX() > 0;
		boolean isNegativeX = first.getX() < 0 || second.getX() < 0
				|| third.getX() < 0;
		boolean isPositiveY = first.getY() > 0 || second.getY() > 0
				|| third.getY() > 0;
		boolean isNegativeY = first.getY() < 0 || second.getY() < 0
				|| third.getY() < 0;

		if (isPositiveX && isNegativeX && isPositiveY && isNegativeY) {
			vertices = new Vector2D[3];
			vertices[0] = first;
			vertices[1] = second;
			vertices[2] = third;
		} else {
			throw new ShapeVerticesNotApplicableException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.ObjectShape#checkCollision(de.illonis.eduras.Game,
	 * de.illonis.eduras.GameObject, java.awt.geom.Point2D.Double)
	 */
	@Override
	public Vector2D checkCollision(GameInformation game, GameObject thisObject,
			Vector2D target) {

		Vector2D result = target;

		HashMap<Integer, GameObject> gameObjects = game.getObjects();

		GameObject collisionObject = null;

		Vector2D positionVector = thisObject.toPositionVector();
		LinkedList<Line> lines = Geometry.getLinesBetweenShapePositions(
				vertices, positionVector, target);

		LinkedList<CollisionPoint> collisions = new LinkedList<CollisionPoint>();

		// Check for collides with objects
		for (GameObject singleObject : gameObjects.values()) {

			// skip comparing the object with itself
			if (singleObject.equals(thisObject))
				continue;

			ObjectShape otherObjectShape = singleObject.getShape();

			CollisionPoint nearestCollision = CollisionPoint
					.findNearestCollision(otherObjectShape.isIntersected(lines,
							singleObject));

			// skip if there was no collision
			if (nearestCollision == null) {
				continue;
			}

			// remember the gameObject that had a collision
			collisionObject = singleObject;

			collisions.add(nearestCollision);
		}

		// Figure out which collision is the nearest
		CollisionPoint resultingCollisionPoint = null;
		if (collisions.size() > 1) {
			resultingCollisionPoint = CollisionPoint
					.findNearestCollision(collisions);
		} else {
			if (collisions.size() > 0) {
				resultingCollisionPoint = collisions.getFirst();
			}
		}

		// if there was a collision, notify the involved objects and calculate
		// the new position
		if (collisionObject != null) {
			thisObject.onCollision(collisionObject);
			collisionObject.onCollision(thisObject);

			Vector2D targetResult = new Vector2D(positionVector);
			targetResult.add(resultingCollisionPoint.getDistanceVector());
			result = targetResult;
		}

		// calculate the new position after a collision

		return result;
	}

	/**
	 * Returns the vertices of the triangle.
	 * 
	 * @return The vertices.
	 */
	@Override
	public LinkedList<Vector2D> getVertices() {
		LinkedList<Vector2D> res = new LinkedList<Vector2D>();

		res.add(vertices[0]);
		res.add(vertices[1]);
		res.add(vertices[2]);

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.ObjectShape#isIntersected(java.util.LinkedList,
	 * de.illonis.eduras.GameObject)
	 */
	@Override
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {

		LinkedList<CollisionPoint> interceptPoints = new LinkedList<CollisionPoint>();

		for (Line line : lines) {
			for (Line borderLine : getBorderLines(thisObject)) {
				Vector2D interceptPoint = Geometry
						.getSegmentLinesInterceptPoint(borderLine, line);

				if (interceptPoint == null) {
					continue;
				} else {

					double distanceVectorX = interceptPoint.getX()
							- line.getU().getX();
					double distanceVectorY = interceptPoint.getX()
							- line.getU().getY();
					Vector2D distanceVector = new Vector2D(distanceVectorX,
							distanceVectorY);

					EduLog.info("[LOGIC][TRIANGLE] Collision at "
							+ interceptPoint.getX() + " , "
							+ interceptPoint.getY());

					CollisionPoint interception = new CollisionPoint(
							interceptPoint, distanceVector);
					interceptPoints.add(interception);
				}

			}
		}
		return interceptPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.shapes.ObjectShape#getBorderLines()
	 */
	@Override
	public LinkedList<Line> getBorderLines(GameObject object) {

		LinkedList<Line> borderLines = new LinkedList<Line>();

		LinkedList<Vector2D> absoluteVertices = getAbsoluteVertices(object);

		for (int i = 0; i < 3; i++) {
			Line borderLine = new Line(absoluteVertices.get(i),
					absoluteVertices.get((i + 1) % 3));
			borderLines.add(borderLine);
		}
		return borderLines;
	}

}
