/**
 * 
 */
package de.illonis.eduras.shapes;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * Super class of any shape a object in the game can have.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class ObjectShape {

	/**
	 * Determines how exactly collisions are calculated. The higher this value,
	 * the less will gameobjects of type triangle will be able to overlap.
	 */
	private static final int COLLISION_ACCURACY = 20;

	/**
	 * Checks if there is will be a collision if the object tries to move from
	 * its current position to the target position.
	 * 
	 * @param game
	 *            The current state of the game.
	 * @param thisObject
	 *            The object which wants to move.
	 * @param target
	 *            The target position
	 * @return Returns the position of the object after the move.
	 */
	public abstract Vector2D checkCollision(GameInformation game,
			GameObject thisObject, Vector2D target);

	/**
	 * Returns the line segments which represents the borders of the shape. The
	 * borders are calculated in absolute values from the given object.
	 * 
	 * @param object
	 *            The object from which the absolute borderlines are calculated.
	 * @return The line segments representing the borders of the shape.
	 */
	public abstract LinkedList<Line> getBorderLines(GameObject object);

	/**
	 * Returns the vertices this shape is made of. Note that the relative values
	 * are returned!
	 * 
	 */
	public abstract LinkedList<Vector2D> getVertices();

	public abstract Vector2D[] getVerticesAsArray();

	/**
	 * Returns the absolute vertices of the shape assuming the shape belongs to
	 * the given object.
	 * 
	 * @param object
	 *            The object to which the shape belongs.
	 * @return Returns a LinkedList of absolute vertices.
	 */
	public LinkedList<Vector2D> getAbsoluteVertices(GameObject object) {
		double objectXPos = object.getXPosition();
		double objectYPos = object.getYPosition();

		LinkedList<Vector2D> relativeVertices = getVertices();
		LinkedList<Vector2D> absoluteVertices = new LinkedList<Vector2D>();

		for (Vector2D singleRelativeVertice : relativeVertices) {
			double absXPos = singleRelativeVertice.getX() + objectXPos;
			double absYPos = singleRelativeVertice.getY() + objectYPos;

			Vector2D singleAbsoluteVertice = new Vector2D(absXPos, absYPos);

			absoluteVertices.add(singleAbsoluteVertice);
		}

		return absoluteVertices;
	}

	/**
	 * This method serves as a generalized calculation of collisions as
	 * described by {@link checkCollision}. It will only work for game objects
	 * whose shape is a polygon, so the shape must have implemented
	 * {@link getVerticesAsArray} properly. Otherwise you wont get a valid
	 * solution.
	 * 
	 * @param game
	 *            See {@link checkCollision}
	 * @param thisObject
	 *            See {@link checkCollision}
	 * @param target
	 *            See {@link checkCollision}
	 * @return See {@link checkCollision}
	 */
	protected Vector2D checkPolygonCollision(GameInformation game,
			GameObject thisObject, Vector2D target) {

		Vector2D[] vertices = thisObject.getShape().getVerticesAsArray();

		Vector2D result = target;

		HashMap<Integer, GameObject> gameObjects = game.getObjects();

		GameObject collisionObject = null;

		Vector2D positionVector = thisObject.toPositionVector();

		// calculate border points to use for collision calculation
		LinkedList<Line> borderLines = Geometry
				.getRelativeBorderLines(vertices);

		Vector2D[] movementPoints = new Vector2D[COLLISION_ACCURACY
				* vertices.length];

		int j = 0;
		for (Line singleBorderLine : borderLines) {
			for (int i = 0; i < COLLISION_ACCURACY; i++) {
				movementPoints[j] = singleBorderLine
						.getPointAt((1. / COLLISION_ACCURACY) * i);
				j++;
			}
		}

		LinkedList<Line> lines = Geometry.getLinesBetweenShapePositions(
				movementPoints, positionVector, target);

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
			resultingCollisionPoint.getDistanceVector().invert();
			targetResult.add(resultingCollisionPoint.getDistanceVector());
			result = targetResult;
		}

		// calculate the new position after a collision

		return result;
	}

	/**
	 * Checks if the shape related to a specific object is intersected by
	 * another moving object, which is represented by lines.
	 * 
	 * @param lines
	 *            The lines representing the moving object.
	 * @param thisObject
	 *            The object to which the shape belongs.
	 * @return Returns a linked list of collision points. The list will be empty
	 *         if there is no collision.
	 */
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
					double distanceVectorY = interceptPoint.getY()
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
}
