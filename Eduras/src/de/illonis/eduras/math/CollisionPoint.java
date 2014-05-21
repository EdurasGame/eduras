/**
 * 
 */
package de.illonis.eduras.math;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;

/**
 * A wrapper class for collision points which stores not only the point of
 * collision but also the distance to that collision.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class CollisionPoint {

	private final static Logger L = EduLog.getLoggerFor(CollisionPoint.class
			.getName());

	private final Vector2f interceptPoint;
	private final Vector2f distanceVector;

	/**
	 * Creates a new point of collision.
	 * 
	 * @param interceptPoint
	 *            The point of collision.
	 * @param distanceVector
	 *            The distancevector to this collision
	 */
	public CollisionPoint(Vector2f interceptPoint, Vector2f distanceVector) {
		this.interceptPoint = interceptPoint;
		this.distanceVector = distanceVector;
	}

	/**
	 * Returns the point of collision.
	 * 
	 * @return point of collision.
	 */
	public Vector2f getInterceptPoint() {
		return interceptPoint;
	}

	/**
	 * Returns the distance to the collision.
	 * 
	 * @return distance to the collision.
	 */
	public double getDistance() {
		return distanceVector.length();
	}

	/**
	 * Returns the distancevector.
	 * 
	 * @return The distanceVector.
	 */
	public Vector2f getDistanceVector() {
		return distanceVector;
	}

	/**
	 * Returns the collision with the shortest distance.
	 * 
	 * @param collisionPoints
	 *            The collisions to calculate the shortest distance of.
	 * @return Returns the collision point with the shortest distance. If the
	 *         given list is null or empty, null will be returned.
	 */
	public static CollisionPoint findNearestCollision(
			LinkedList<CollisionPoint> collisionPoints) {

		if (collisionPoints == null || collisionPoints.size() == 0) {
			return null;
		}

		CollisionPoint result = collisionPoints.getFirst();
		double shortestDistance = collisionPoints.getFirst().getDistance();

		for (CollisionPoint singleCollision : collisionPoints) {
			if (singleCollision.getDistance() < shortestDistance) {
				shortestDistance = singleCollision.getDistance();
				result = singleCollision;
			}
		}

		return result;
	}

	/**
	 * Creates a CollisionPoint by a given intercept point and the intercepting
	 * line.
	 * 
	 * @param interceptPoint
	 *            The point where the interception took place.
	 * @param line
	 *            The line that was involved in the interception.
	 * @return Returns the correlating CollisionPoint.
	 */
	public static CollisionPoint createCollisionPointByInterceptPoint(
			Vector2f interceptPoint, Line line) {
		float distanceVectorX = interceptPoint.getX() - line.getX1();
		float distanceVectorY = interceptPoint.getY() - line.getY1();
		Vector2f distanceVector = new Vector2df(distanceVectorX,
				distanceVectorY);

		L.finest("[LOGIC][TRIANGLE] Collision at " + interceptPoint.getX()
				+ " , " + interceptPoint.getY());

		CollisionPoint interception = new CollisionPoint(interceptPoint,
				distanceVector);
		return interception;
	}
}
