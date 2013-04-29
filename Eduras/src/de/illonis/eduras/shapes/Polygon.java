/**
 * 
 */
package de.illonis.eduras.shapes;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;

import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * 
 * Represents a Polygon.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Polygon extends ObjectShape {

	/*
	 * Determines how exactly collisions are calculated. The higher this value,
	 * the less will gameobjects of type triangle will be able to overlap.
	 */
	private static final int COLLISION_ACCURACY = 20;

	private Vector2D[] vertices;

	public Polygon() {
	}

	/**
	 * Creates a new polygon with the given vertices.
	 * 
	 * @param vertices
	 */
	public Polygon(Vector2D[] vertices) {

		this.vertices = vertices;

	}

	/**
	 * Set the array of vertices of this polygon to the given array.
	 * 
	 * @param vertices
	 *            The array to set the vertices of this polygon to.
	 */
	public void setVertices(Vector2D[] vertices) {
		this.vertices = vertices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.shapes.ObjectShape#checkCollision(de.illonis.eduras
	 * .GameInformation, de.illonis.eduras.GameObject,
	 * de.illonis.eduras.math.Vector2D)
	 */
	/*
	 * @Override public Vector2D checkCollision(GameInformation game, GameObject
	 * thisObject, Vector2D target) {
	 * 
	 * Polygon shape;
	 * 
	 * if (!(thisObject.getShape() instanceof Polygon)) { try { throw new
	 * WrongShapeTypeException(); } catch (WrongShapeTypeException e) {
	 * e.printStackTrace(); return null; } } else { shape = (Polygon)
	 * thisObject.getShape(); }
	 * 
	 * Vector2D[] vertices = shape.getVerticesAsArray();
	 * 
	 * Vector2D result = target;
	 * 
	 * HashMap<Integer, GameObject> gameObjects = game.getObjects();
	 * 
	 * GameObject collisionObject = null;
	 * 
	 * Vector2D positionVector = thisObject.toPositionVector();
	 * 
	 * // calculate border points to use for collision calculation
	 * LinkedList<Line> borderLines = Geometry
	 * .getRelativeBorderLines(vertices);
	 * 
	 * Vector2D[] movementPoints = new Vector2D[COLLISION_ACCURACY
	 * vertices.length];
	 * 
	 * int j = 0; for (Line singleBorderLine : borderLines) { for (int i = 0; i
	 * < COLLISION_ACCURACY; i++) { movementPoints[j] = singleBorderLine
	 * .getPointAt((1. / COLLISION_ACCURACY) * i); j++; } }
	 * 
	 * LinkedList<Line> lines = Geometry.getLinesBetweenShapePositions(
	 * movementPoints, positionVector, target);
	 * 
	 * LinkedList<CollisionPoint> collisions = new LinkedList<CollisionPoint>();
	 * 
	 * // Check for collides with objects for (GameObject singleObject :
	 * gameObjects.values()) {
	 * 
	 * // skip comparing the object with itself if
	 * (singleObject.equals(thisObject)) continue;
	 * 
	 * ObjectShape otherObjectShape = singleObject.getShape();
	 * 
	 * CollisionPoint nearestCollision = CollisionPoint
	 * .findNearestCollision(otherObjectShape.isIntersected(lines,
	 * singleObject));
	 * 
	 * // skip if there was no collision if (nearestCollision == null) {
	 * continue; }
	 * 
	 * // remember the gameObject that had a collision collisionObject =
	 * singleObject;
	 * 
	 * collisions.add(nearestCollision); }
	 * 
	 * // Figure out which collision is the nearest CollisionPoint
	 * resultingCollisionPoint = null; if (collisions.size() > 1) {
	 * resultingCollisionPoint = CollisionPoint
	 * .findNearestCollision(collisions); } else { if (collisions.size() > 0) {
	 * resultingCollisionPoint = collisions.getFirst(); } }
	 * 
	 * // if there was a collision, notify the involved objects and calculate //
	 * the new position if (collisionObject != null) {
	 * thisObject.onCollision(collisionObject);
	 * collisionObject.onCollision(thisObject);
	 * 
	 * Vector2D targetResult = new Vector2D(positionVector);
	 * resultingCollisionPoint.getDistanceVector().invert();
	 * targetResult.add(resultingCollisionPoint.getDistanceVector()); result =
	 * targetResult; }
	 * 
	 * // calculate the new position after a collision
	 * 
	 * return result; }
	 */

	/**
	 * Returns the line segments which represents the borders of the shape. The
	 * borders are calculated in absolute values from the given object.
	 * 
	 * @param object
	 *            The object from which the absolute borderlines are calculated.
	 * @return The line segments representing the borders of the shape.
	 */
	public LinkedList<Line> getBorderLines(GameObject object) {
		return Geometry.getRelativeBorderLines(getAbsoluteVertices(object)
				.toArray(new Vector2D[getAbsoluteVertices(object).size()]));
	}

	/**
	 * Returns the vertices this shape is made of. Note that the relative values
	 * are returned!
	 * 
	 * @return the vertices.
	 * 
	 */
	public LinkedList<Vector2D> getVertices() {
		LinkedList<Vector2D> result = new LinkedList<Vector2D>();
		for (int i = 0; i < vertices.length; i++) {
			result.add(vertices[i]);
		}
		return result;
	}

	/**
	 * Returns the vertices as an array of vectors.
	 * 
	 * @return The vertices as an array.
	 */
	public Vector2D[] getVerticesAsArray() {
		return vertices;
	}

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
		LinkedList<CollisionPoint> interceptPoints = new LinkedList<CollisionPoint>();

		for (Line line : lines) {
			for (Line borderLine : getBorderLines(thisObject)) {
				Vector2D interceptPoint = Geometry
						.getSegmentLinesInterceptPoint(borderLine, line);

				if (interceptPoint == null) {
					continue;
				} else {

					CollisionPoint interception = CollisionPoint
							.createCollisionPointByInterceptPoint(
									interceptPoint, line);
					interceptPoints.add(interception);
				}

			}
		}
		return interceptPoints;
	}

	@Override
	public Double getBoundingBox() {
		// TODO: implement
		return new Rectangle2D.Double(0, 0, 5E100, 5E100);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.shapes.ObjectShape#getBorderPoints()
	 */
	@Override
	public Vector2D[] getBorderPoints() {
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

		return movementPoints;
	}

	@Override
	public ObjectShape getScaled(double scale) {
		// TODO: implement
		return null;
	}
}
