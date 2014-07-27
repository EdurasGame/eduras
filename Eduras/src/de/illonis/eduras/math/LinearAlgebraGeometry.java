package de.illonis.eduras.math;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ShapeNotSupportedException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;

/**
 * This geometry implementation identifies the cases where
 * {@link SimpleGeometry} isn't accurate enough and tries to handle them better.
 * 
 * @author Florian 'Ren' Mai
 * 
 */
public class LinearAlgebraGeometry extends SimpleGeometry {

	private final static Logger L = EduLog
			.getLoggerFor(LinearAlgebraGeometry.class.getName());

	// only for performance measuring
	private long skippedCalculations;
	private long allCalculations;

	/**
	 * Create the geometry.
	 * 
	 * @param gameObjects
	 *            The game objects to be taken into account for collision
	 *            detection.
	 */
	public LinearAlgebraGeometry(Map<Integer, GameObject> gameObjects) {
		super(gameObjects);
	}

	@Override
	public Vector2f moveTo(MoveableGameObject movingObject, Vector2f target,
			Collection<GameObject> touched, Collection<GameObject> collided) {

		Vector2f result = new Vector2f(target);
		Map<Integer, GameObject> gameObjects = movingObject.getGame()
				.getObjects();

		GameObject collisionObject = null;

		Vector2f positionVector = movingObject.getPositionVector();

		// calculate border points to use for collision calculation

		// Vector2f[] movementPoints;
		// try {
		// movementPoints = getBorderPointsForShape(thisObject.getShape());
		// } catch (ShapeNotSupportedException e) {
		// L.log(Level.SEVERE, "The shape of object #" + thisObject.getId()
		// + " is not supported! Not moving the object...", e);
		// return positionVector;
		// }

		// rotateAll(movementPoints, thisObject.getRotation());

		LinkedList<Line> lines = Geometry.getLinesBetweenShapePositions(
				movingObject.getShape(), target);
		TreeSet<GameObject> touchedObjects = new TreeSet<GameObject>(
				new GameObject.GameObjectIdComparator());

		LinkedList<CollisionPoint> collisions = new LinkedList<CollisionPoint>();

		float sizeOfThisObject = movingObject.getShape()
				.getBoundingCircleRadius();
		float maxDistanceToTarget = sizeOfThisObject
				+ target.distance(movingObject.getPositionVector());

		// Check for collides with objects
		for (GameObject singleObject : gameObjects.values()) {

			// skip comparing the object with itself
			if (singleObject.equals(movingObject)) {
				continue;
			}

			allCalculations++;

			// check if the object is in range so a collision is possible at all
			// to be sure at all times we need to assume that the position
			// vectors are at the worst possible locations within the shape such
			// that the distance is maximized.
			float sizeOfOtherObject = singleObject.getShape()
					.getBoundingCircleRadius() * 2;
			float minDistanceToOtherObject = movingObject.getPositionVector()
					.distance(singleObject.getPositionVector())
					- sizeOfThisObject - sizeOfOtherObject;
			if (maxDistanceToTarget < minDistanceToOtherObject) {
				skippedCalculations++;
				continue;
			}

			CollisionPoint nearestCollision;
			try {
				nearestCollision = CollisionPoint
						.findNearestCollision(singleObject.isIntersected(lines));
			} catch (ShapeNotSupportedException e) {
				L.log(Level.SEVERE,
						"The gameobject's shape to compare against isn't supported!",
						e);
				return movingObject.getPositionVector();
			}

			// skip if there was no collision
			if (nearestCollision == null) {
				continue;
			}

			if (GameObject.canCollideWithEachOther(movingObject, singleObject)) {
				// remember the gameObject that had a collision
				collisionObject = singleObject;
			} else {
				touchedObjects.add(singleObject);
			}

			collisions.add(nearestCollision);
		}

		touched.addAll(touchedObjects);

		if (!movingObject.isCollidable(null)) {
			return result;
		}

		// Figure out which collision is the nearest
		// CollisionPoint resultingCollisionPoint = null;
		// if (collisions.size() > 1) {
		// resultingCollisionPoint = CollisionPoint
		// .findNearestCollision(collisions);
		// } else {
		// if (collisions.size() > 0) {
		// resultingCollisionPoint = collisions.getFirst();
		// }
		// }

		// if there was a collision, notify the involved objects and calculate
		// the new position
		if (collisionObject != null) {
			collided.add(collisionObject);

			// Use the following code as an alternative. Gives more accurate
			// results, but is visually ugly and can lead to stucking at edges
			// more easily
			// Vector2D targetResult = new Vector2D(positionVector);
			// resultingCollisionPoint.getDistanceVector().invert();
			// targetResult.add(resultingCollisionPoint.getDistanceVector());
			// result = targetResult;
			result = positionVector;
		}

		L.finest("Skipped "
				+ (((float) skippedCalculations / (float) allCalculations) * 100)
				+ "% of all collision calculations so far.");
		return result;
	}

	private void rotateAll(Vector2f[] movementPoints, float rotation) {
		// TODO need to implement this once we have non circular moving objects.

	}
}
