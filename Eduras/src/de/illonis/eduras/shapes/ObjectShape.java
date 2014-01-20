/**
 * 
 */
package de.illonis.eduras.shapes;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
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
	 * All types of shapes.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum ShapeType {
		BIRD, HOUSE, SWORD, TRIANGLE, ROCKET, STAR;
	}

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
	public Vector2D checkCollisionOnMove(GameInformation game,
			GameObject thisObject, Vector2D target) {

		Vector2D result = new Vector2D(target);
		if (!thisObject.isCollidable())
			return result;
		ConcurrentHashMap<Integer, GameObject> gameObjects = game.getObjects();

		GameObject collisionObject = null;

		Vector2D positionVector = thisObject.getPositionVector();

		// calculate border points to use for collision calculation

		Vector2D[] movementPoints = getBorderPoints();

		Vector2D.rotateAll(movementPoints, thisObject.getRotation());

		LinkedList<Line> lines = Geometry.getLinesBetweenShapePositions(
				movementPoints, positionVector, target);

		LinkedList<CollisionPoint> collisions = new LinkedList<CollisionPoint>();

		// Check for collides with objects
		for (GameObject singleObject : gameObjects.values()) {

			// skip comparing the object with itself
			if (singleObject.equals(thisObject) || !singleObject.isCollidable())
				continue;

			CollisionPoint nearestCollision = CollisionPoint
					.findNearestCollision(singleObject.isIntersected(lines));

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
	 * Checks if there will be a collision if the given object tries to rotate
	 * to the given absolute angle.
	 * 
	 * @param gameInfo
	 *            The game info.
	 * @param thisObject
	 *            The game object that tries to rotate to the given angle.
	 * @param rotationAngle
	 *            The angle the game object tries to rotate to.
	 * @return Returns the absolute rotation angle of the game object after the
	 *         rotation.
	 */
	public abstract double checkCollisionOnRotation(GameInformation gameInfo,
			GameObject thisObject, double rotationAngle);

	/**
	 * Returns border points of this shape.
	 * 
	 * @return border points.
	 */
	public abstract Vector2D[] getBorderPoints();

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
	public abstract LinkedList<CollisionPoint> isIntersected(
			LinkedList<Line> lines, GameObject thisObject);

	/**
	 * Returns a rectangular bounding box containing the complete shape.
	 * 
	 * @return the bounding box.
	 */
	public abstract Rectangle2D.Double getBoundingBox();

	/**
	 * Returns a scaled version of this shape. The returned shape is a copy of
	 * this shape.
	 * 
	 * @param scale
	 *            new scale.
	 * @return scaled copy of this shape.
	 */
	public abstract ObjectShape getScaled(double scale);
}
