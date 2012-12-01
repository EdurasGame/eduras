/**
 * 
 */
package de.illonis.eduras.math;

import java.util.LinkedList;

/**
 * A wrapper class for collision points which stores not only the point of
 * collision but also the distance to that collision.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class CollisionPoint {

	private final Vector2D interceptPoint;
	private final Vector2D distanceVector;

	/**
	 * Creates a new point of collision.
	 * 
	 * @param interceptPoint
	 *            The point of collision.
	 * @param distanceVector
	 *            The distancevector to this collision
	 */
	public CollisionPoint(Vector2D interceptPoint, Vector2D distanceVector) {
		this.interceptPoint = interceptPoint;
		this.distanceVector = distanceVector;
	}

	/**
	 * Returns the point of collision
	 * 
	 * @return
	 */
	public Vector2D getInterceptPoint() {
		return interceptPoint;
	}

	/**
	 * Returns the distance to the collision.
	 * 
	 * @return
	 */
	public double getDistance() {
		return distanceVector.getLength();
	}

	/**
	 * Returns the distancevector.
	 * 
	 * @return The distanceVector.
	 */
	public Vector2D getDistanceVector() {
		return distanceVector;
	}

	/**
	 * Returns the collision with the shortest distance.
	 * 
	 * @param collisions
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
}