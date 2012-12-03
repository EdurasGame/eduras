/**
 * 
 */
package de.illonis.eduras.shapes;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * Represents the shape of a circle.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Circle extends ObjectShape {

	private static final int COLLISION_ACCURACY = 20;
	private double radius = 0;

	/**
	 * A circle shape is determined by its radius.
	 * 
	 * @param radius
	 *            The circle's radius.
	 */
	public Circle(double radius) {
		this.radius = radius;
	}

	/**
	 * Returns 'numPoints' points on the circle such that two neighboured points
	 * have the same distance.
	 * 
	 * @param centerPoint
	 *            The center point of the circle.
	 * @param numPoints
	 *            The number of points you want to receive.
	 * @return Returns a list containing the points on the circle.
	 */
	public LinkedList<Vector2D> getAbsolutePoints(Vector2D centerPoint,
			int numPoints) {

		double centerPointX = centerPoint.getX();
		double centerPointY = centerPoint.getY();

		LinkedList<Vector2D> result;

		result = getRelativePoints(numPoints);

		for (int i = 0; i < numPoints; i++) {
			Vector2D vec = result.get(i);
			vec.setX(vec.getX() + centerPointX);
			vec.setY(vec.getY() + centerPointY);
		}
		return result;
	}

	/**
	 * Returns 'numpoints' points on the circle with the origin as its
	 * centerpoint such that two neighboured points have the same distance.
	 * 
	 * @param numPoints
	 *            The number of points you want to receive.
	 * @return Returns a list containing the points on the circle.
	 */
	public LinkedList<Vector2D> getRelativePoints(int numPoints) {

		LinkedList<Vector2D> result = new LinkedList<Vector2D>();
		double angle = 2 * Math.PI / numPoints;

		for (int i = 0; i < numPoints; i++) {
			result.add(getPointAt(i * angle));
		}
		return result;
	}

	/**
	 * Returns the point on the circle at the given angle.
	 * 
	 * @param angle
	 *            The angle on the circle where to find the point.
	 * @return The point.
	 */
	private Vector2D getPointAt(double angle) {

		double pointXPos = (Math.cos(angle) * this.radius);
		double pointYPos = (Math.sin(angle) * this.radius);

		return new Vector2D(pointXPos, pointYPos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.shapes.ObjectShape#checkCollision(de.illonis.eduras
	 * .GameInformation, de.illonis.eduras.GameObject,
	 * de.illonis.eduras.math.Vector2D)
	 */
	@Override
	public Vector2D checkCollision(GameInformation game, GameObject thisObject,
			Vector2D target) {

		Vector2D result = target;
		HashMap<Integer, GameObject> gameObjects = game.getObjects();

		LinkedList<Line> lines = new LinkedList<Line>();
		LinkedList<Vector2D> circlePoints = getRelativePoints(COLLISION_ACCURACY);
		Geometry.getLinesBetweenShapePositions(
				circlePoints.toArray(new Vector2D[COLLISION_ACCURACY]),
				thisObject.getPositionVector(), target);

		LinkedList<CollisionPoint> collisions = new LinkedList<CollisionPoint>();

		GameObject collidingObject = null;

		for (GameObject singleGameObject : gameObjects.values()) {

			if (singleGameObject.equals(thisObject)) {
				continue;
			}

			LinkedList<CollisionPoint> collisionPoints = singleGameObject
					.getShape().isIntersected(lines, singleGameObject);

			CollisionPoint nearestCollisionPoint = CollisionPoint
					.findNearestCollision(collisionPoints);

			if (nearestCollisionPoint == null) {
				continue;
			}

			collidingObject = singleGameObject;

			collisions.add(nearestCollisionPoint);

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
		if (collidingObject != null) {
			thisObject.onCollision(collidingObject);
			collidingObject.onCollision(thisObject);

			Vector2D targetResult = new Vector2D(
					collidingObject.getPositionVector());
			resultingCollisionPoint.getDistanceVector().invert();
			targetResult.add(resultingCollisionPoint.getDistanceVector());
			result = targetResult;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.shapes.ObjectShape#isIntersected(java.util.LinkedList,
	 * de.illonis.eduras.GameObject)
	 */
	@Override
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {

		Vector2D positionVector = thisObject.getPositionVector();

		LinkedList<CollisionPoint> result = new LinkedList<CollisionPoint>();

		for (Line singleLine : lines) {

			// check wether given line is valid
			if (singleLine.getDirectionalVector().isNull()) {
				continue;
			}

			Vector2D[] interceptionPoints = Geometry
					.getCircleLineSegmentInterceptPoints(this, positionVector,
							singleLine);

			if (interceptionPoints[0] == null && interceptionPoints[1] == null) {
				continue;
			}

			for (int i = 0; i < 2; i++) {

				if (interceptionPoints[i] != null) {

					CollisionPoint collisionPoint = CollisionPoint
							.createCollisionPointByInterceptPoint(
									interceptionPoints[i], singleLine);
					result.add(collisionPoint);
				}
			}

		}

		return result;
	}

	/**
	 * Returns the circle's radius.
	 * 
	 * @return The radius.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the circle.
	 * 
	 * @param radius
	 *            The new value of the radius.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

}
