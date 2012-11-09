package de.illonis.eduras.math;

import java.util.LinkedList;

/**
 * A math class that provides useful functions that are used in two-dimensional
 * space.
 * 
 * @author illonis
 * 
 */
public class Geometry {

	/**
	 * Returns square of given double number.
	 * 
	 * @see #square(int)
	 * 
	 * @param x
	 *            number
	 * @return square of x
	 */
	public static double square(double x) {
		return x * x;
	}

	/**
	 * Returns square of given integer number.
	 * 
	 * @see #square(double)
	 * 
	 * @param x
	 *            number
	 * @return square of x
	 */
	public static int square(int x) {
		return x * x;
	}

	/**
	 * Return length of a hypotenuse in a right-angled triangle.
	 * 
	 * @param a
	 *            leg a
	 * @param b
	 *            leg b
	 * @return length of hypotenuse
	 */
	public static double getHypotenuseLength(double a, double b) {
		return Math.sqrt(square(a) + square(b));
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
			Vector2D[] vertices, Vector2D source, Vector2D destination) {

		LinkedList<Line> lines = new LinkedList<Line>();

		for (int i = 0; i < vertices.length; i++) {

			Vector2D sourcePoint = new Vector2D(source);
			sourcePoint.add(vertices[i]);

			Vector2D destPoint = new Vector2D(destination);
			destPoint.add(vertices[i]);

			lines.add(new Line(sourcePoint, destPoint));
		}

		return lines;
	}

	/**
	 * Returns the intercept point of two line segments.
	 * 
	 * @param first
	 *            The first line segment.
	 * @param second
	 *            The second line segment.
	 * @return Returns null if there was no intercept point found and returns
	 *         the intercept point as a vector otherwise.
	 */
	public static Vector2D getSegmentLinesInterceptPoint(Line first, Line second) {

		// use parameter equotation of lines and equalize them. this results in
		// the following calculation

		Vector2D firstSupportVector = first.getSupportVector();
		Vector2D secondSupportVector = second.getSupportVector();
		Vector2D firstDirectionalVector = first.getDirectionalVector();
		Vector2D secondDirectionalVector = second.getDirectionalVector();

		double firstSupportX = firstSupportVector.getX();
		double firstSupportY = firstSupportVector.getY();
		double secondSupportX = secondSupportVector.getX();
		double secondSupportY = secondSupportVector.getY();

		double firstDirectionX = firstDirectionalVector.getX();
		double firstDirectionY = firstDirectionalVector.getY();
		double secondDirectionX = secondDirectionalVector.getX();
		double secondDirectionY = secondDirectionalVector.getY();

		double s = 0;
		s = (firstSupportY - secondSupportY + ((secondSupportX - firstSupportX) * firstDirectionY)
				/ firstDirectionX)
				/ (secondDirectionY - ((secondDirectionX * firstDirectionY) / firstDirectionX));

		double r = (secondSupportX + s * secondDirectionX - firstSupportX)
				/ firstDirectionX;
		// the first line segment ends for s = 1, the second ends for r = 1 so
		// if s > 1 || r > 1 there is no intersection
		// points

		if (s > 1 || r > 1 || Double.isNaN(s) || Double.isNaN(r)) {
			return null;
		}

		Vector2D intersectPoint = second.getPointAt(s);

		return intersectPoint;

	}
}
