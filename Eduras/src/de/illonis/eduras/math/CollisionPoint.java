/**
 * 
 */
package de.illonis.eduras.math;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;

/**
 * A wrapper class for collision points which stores not only the point of
 * collision but also the distance to that collision and the angle of the
 * collision.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class CollisionPoint {

	private final static Logger L = EduLog.getLoggerFor(CollisionPoint.class
			.getName());

	private final Vector2f interceptPoint;
	private final Vector2f distanceVector;
	private final Vector2f interceptPointOnShape;
	private final float angle;

	/**
	 * Creates a new point of collision.
	 * 
	 * @param interceptPoint
	 *            The point of collision.
	 * @param distanceVector
	 *            The distancevector to this collision
	 * @param interceptPointOnShape
	 * @param angle
	 *            angle of the collision
	 */
	public CollisionPoint(Vector2f interceptPoint, Vector2f distanceVector,
			Vector2f interceptPointOnShape, float angle) {
		this.interceptPoint = interceptPoint;
		this.distanceVector = distanceVector;
		this.interceptPointOnShape = interceptPointOnShape;
		this.angle = angle;
	}

	/**
	 * Returns the point on the original shape that is involved in the
	 * collision.
	 * 
	 * @return the point
	 */
	public Vector2f getInterceptPointOnShape() {
		return interceptPointOnShape;
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
	 * @param collisions
	 *            The collisions to calculate the shortest distance of.
	 * @return Returns the collision point with the shortest distance. If the
	 *         given list is null or empty, null will be returned.
	 */
	public static CollisionPoint findNearestCollision(
			Collection<CollisionPoint> collisions) {

		LinkedList<CollisionPoint> collisionPoints = new LinkedList<CollisionPoint>(
				collisions);
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
	 * @param angle
	 * @return Returns the correlating CollisionPoint.
	 */
	public static CollisionPoint createCollisionPointByInterceptPoint(
			Vector2f interceptPoint, Line line, float angle) {
		float distanceVectorX = interceptPoint.getX() - line.getX1();
		float distanceVectorY = interceptPoint.getY() - line.getY1();
		Vector2f distanceVector = new Vector2df(distanceVectorX,
				distanceVectorY);
		Vector2f interceptingPointOnShape = new Vector2f(line.getX1(),
				line.getY1());

		L.finest("[LOGIC][TRIANGLE] Collision at " + interceptPoint.getX()
				+ " , " + interceptPoint.getY());

		CollisionPoint interception = new CollisionPoint(interceptPoint,
				distanceVector, interceptingPointOnShape, angle);
		return interception;
	}

	/**
	 * Returns the angle of the collision
	 * 
	 * @return angle
	 */
	public float getAngle() {
		return angle;
	}
}
