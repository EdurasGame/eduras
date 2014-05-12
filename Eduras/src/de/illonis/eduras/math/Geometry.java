package de.illonis.eduras.math;

import java.util.LinkedList;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * A math class that provides useful functions that are used in two-dimensional
 * space.
 * 
 * @author illonis
 * 
 */
public class Geometry {

	/**
	 * Return length of a hypotenuse in a right-angled triangle.
	 * 
	 * @param a
	 *            leg a
	 * @param b
	 *            leg b
	 * @return length of hypotenuse.
	 */
	public static double getHypotenuseLength(double a, double b) {
		return Math.sqrt(BasicMath.square(a) + BasicMath.square(b));
	}

	/**
	 * Tests whether two shapes collide.
	 * 
	 * @param a
	 *            first shape
	 * @param b
	 *            second shape
	 * @return true if given shapes collide.
	 */
	public static boolean shapeCollides(Shape a, Shape b) {
		return a.intersects(b) || a.contains(b) || b.contains(a);
	}

	/**
	 * Converts given degree value to radian.
	 * 
	 * @param degree
	 *            value in degree.
	 * @return value in radian.
	 */
	public static double toRadian(double degree) {
		return degree * (Math.PI / 180);
	}

	/**
	 * Converts given degree value to radian.
	 * 
	 * @param degree
	 *            value in degree.
	 * @return value in radian.
	 */
	public static float toRadian(float degree) {
		return (float) (degree * (Math.PI / 180));
	}

	/**
	 * Converts given radian value to degree.
	 * 
	 * @param radian
	 *            value in radian.
	 * @return value in degree.
	 */
	public static double toDegree(double radian) {
		return BasicMath.calcModulo((radian * (180 / Math.PI)), 360);
	}

	/**
	 * Converts given radian value to degree.
	 * 
	 * @param radian
	 *            value in radian.
	 * @return value in degree.
	 */
	public static float toDegree(float radian) {
		return BasicMath.calcModulo((radian * (180 / (float) Math.PI)), 360);
	}

	/**
	 * Converts an array of vectors into an array of floats such that floats[2 *
	 * i] = vectors[i].x; and floats[2 * i + 1] = vectors[i].y;
	 * 
	 * @param vectors
	 *            the vectors to convert to floats.
	 * @return array of floats
	 */
	public static float[] vectorsToFloat(Vector2f[] vectors) {
		float[] floats = new float[vectors.length * 2];
		for (int i = 0; i < vectors.length; i++) {
			floats[2 * i] = vectors[i].getX();
			floats[2 * i + 1] = vectors[i].getY();
		}

		return floats;
	}

	/**
	 * Returns all the lines between a shape's corner points and its counterpart
	 * of a second shape, that is a shifted copy of the first shape.
	 * 
	 * @param vertices
	 *            The relative vertices of the shape.
	 * @param source
	 *            The middle point of the shape.
	 * @param destination
	 *            The middle point of the shapes copy.
	 * @return Returns a list containing the resulting lines.
	 */
	public static LinkedList<Line> getLinesBetweenShapePositions(
			Vector2df[] vertices, Vector2df source, Vector2df destination) {

		LinkedList<Line> lines = new LinkedList<Line>();

		for (int i = 0; i < vertices.length; i++) {

			Vector2df sourcePoint = new Vector2df(source);
			sourcePoint.add(vertices[i]);

			Vector2df destPoint = new Vector2df(destination);
			destPoint.add(vertices[i]);

			lines.add(new Line(sourcePoint, destPoint));
		}

		return lines;
	}

	/**
	 * Checks weather the one of the given values is NaN, Infinite or not
	 * between 0 and 1.
	 * 
	 * @param s
	 *            First value
	 * @param r
	 *            Second value
	 * @return Returns true if one of the properties is valid.
	 */
	private static boolean checkNotWithin(double s, double r) {
		return s > 1 || r > 1 || s < 0 || r < 0 || Double.isNaN(s)
				|| Double.isNaN(r) || Double.isInfinite(r)
				|| Double.isInfinite(s);
	}

	/**
	 * Calculates the borderlines spanned by the given vertices assuming that a
	 * borderline i is given by a line between vertex no i and vertex no (i +
	 * 1).
	 * 
	 * @param vertices
	 *            The vertices to make the borderlines of
	 * @return The resulting borderlines.
	 */
	public static LinkedList<Line> getRelativeBorderLines(Vector2f[] vertices) {

		LinkedList<Line> borderLines = new LinkedList<Line>();

		for (int i = 0; i < vertices.length; i++) {
			Line borderLine = new Line(vertices[i], vertices[(i + 1)
					% vertices.length]);
			borderLines.add(borderLine);
		}

		return borderLines;

	}

	/**
	 * Calculates a {@link Circle} by a given center point and a point on the
	 * circle. Neither of the given points is modified.
	 * 
	 * @param centerPoint
	 *            The center point.
	 * @param pointOnCircle
	 *            The point on the circle.
	 * @return Returns the circle that was calculated with the given points.
	 */
	public static Circle getCircleByCenterAndPointOnCircle(
			Vector2f centerPoint, Vector2f pointOnCircle) {
		Vector2f diffVector = pointOnCircle.copy();
		diffVector.sub(centerPoint);
		return new Circle(centerPoint.x, centerPoint.y, diffVector.length());
	}
}
