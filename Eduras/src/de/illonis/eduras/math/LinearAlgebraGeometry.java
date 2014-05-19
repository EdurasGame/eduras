package de.illonis.eduras.math;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
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

	/**
	 * Determines how exactly collisions are calculated. The higher this value,
	 * the less will gameobjects of type triangle will be able to overlap.
	 */
	private static final int COLLISION_ACCURACY = 20;

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
	public Vector2f moveTo(MoveableGameObject object, Vector2f target,
			Collection<GameObject> touched, Collection<GameObject> collided) {

		// float distanceToTarget = object.getPositionVector().distance(target);
		// if (distanceToTarget < object.getShape().getBoundingCircleRadius()) {
		// // in this case, the approach used by SimpleGeometry will always
		// // give an exact result, because if it didn't detect a collision
		// // with an object by shape intersection, it must also have not been
		// // detected in the preceding iteration, which cannot be the case
		// return super.moveTo(object, target, touched, collided);
		// } else {
		return checkCollisionOnMove(object.getGame(), object, target, touched,
				collided);
		// }
	}

	/**
	 * Checks if there is will be a collision if the object tries to move from
	 * its current position to the target position.
	 * 
	 * @param game
	 *            The current state of the game.
	 * @param movingObject
	 *            The object which wants to move.
	 * @param target
	 *            The target position
	 * @param touched
	 *            the objects that are touched moving the object to the target
	 * @param collided
	 *            the objects that this one collides with. typically at most
	 *            one.
	 * @return Returns the position of the object after the move.
	 */
	private Vector2f checkCollisionOnMove(GameInformation game,
			GameObject movingObject, Vector2f target,
			Collection<GameObject> touched, Collection<GameObject> collided) {

		Vector2f result = new Vector2f(target);
		Map<Integer, GameObject> gameObjects = game.getObjects();

		GameObject collisionObject = null;
		GameObject touchObject = null;

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

			if (movingObject.isCollidable() && singleObject.isCollidable()) {
				// remember the gameObject that had a collision
				collisionObject = singleObject;
			} else {
				touchedObjects.add(singleObject);
			}

			collisions.add(nearestCollision);
		}

		for (GameObject touchedObject : touchedObjects) {
			touchedObject.onTouch(movingObject);
			movingObject.onTouch(touchedObject);
		}

		if (!movingObject.isCollidable()) {
			return result;
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
			movingObject.onCollision(collisionObject);
			collisionObject.onCollision(movingObject);

			// Use the following code as an alternative. Gives more accurate
			// results, but is visually ugly and can lead to stucking at edges
			// more easily
			// Vector2D targetResult = new Vector2D(positionVector);
			// resultingCollisionPoint.getDistanceVector().invert();
			// targetResult.add(resultingCollisionPoint.getDistanceVector());
			// result = targetResult;
			result = positionVector;
		}

		System.out.println("Skipped "
				+ ((float) skippedCalculations / (float) allCalculations));
		return result;
	}

	private void rotateAll(Vector2f[] movementPoints, float rotation) {
		// TODO Auto-generated method stub

	}

	private Vector2f[] getBorderPointsForShape(Shape shape)
			throws ShapeNotSupportedException {
		if (shape instanceof Polygon) {
			return getPolygonBorderPoints((Polygon) shape);
		}

		if (shape instanceof Ellipse) {
			Vector2f[] result = new Vector2f[0];
			result = Geometry.getPointsOnCirlce((Ellipse) shape,
					COLLISION_ACCURACY).toArray(result);
			return result;
		}

		throw new ShapeNotSupportedException(shape);
	}

	private Vector2f[] getPolygonBorderPoints(Polygon shape) {
		Vector2f[] vertices = Geometry.floatsToVectors(shape.getPoints());

		LinkedList<Line> borderLines = Geometry.getBorderLines(vertices);

		Vector2f[] movementPoints = new Vector2f[COLLISION_ACCURACY
				* vertices.length];

		int j = 0;
		for (Line singleBorderLine : borderLines) {
			for (int i = 0; i < COLLISION_ACCURACY; i++) {
				movementPoints[j] = Geometry.getPointOnLineAt(singleBorderLine,
						(1f / COLLISION_ACCURACY) * i);
				j++;
			}
		}

		return movementPoints;
	}
}
