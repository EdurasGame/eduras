package de.illonis.eduras.shapes;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;

import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.BasicMath;
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

	/**
	 * Determines how exactly collisions are calculated. The higher this value,
	 * the less will gameobjects of type triangle will be able to overlap.
	 */
	private static final int COLLISION_ACCURACY = 20;

	private Vector2D[] vertices;

	/**
	 * Creates a polygon with no vertices.
	 */
	public Polygon() {
		this.vertices = new Vector2D[0];
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
	public Rectangle2D.Double getBoundingBox() {
		ArrayList<Double> xValues = new ArrayList<Double>();
		ArrayList<Double> yValues = new ArrayList<Double>();
		for (Vector2D vertex : vertices) {
			xValues.add(vertex.getX());
			yValues.add(vertex.getY());
		}

		double maxX = BasicMath.max(xValues.toArray(new Double[1]));
		double maxY = BasicMath.max(yValues.toArray(new Double[1]));

		double minX = BasicMath.min(xValues.toArray(new Double[1]));
		double minY = BasicMath.min(yValues.toArray(new Double[1]));

		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

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
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Polygon) {
			Polygon p = (Polygon) obj;
			if (getVertices().isEmpty())
				return p.getVertices().isEmpty();
			return p.getVertices().equals(getVertices());
		} else
			return super.equals(obj);
	}
}
