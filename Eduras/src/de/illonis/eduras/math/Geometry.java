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
	 * Return length of a hypotenuse in a right-angled triangle.
	 * 
	 * @param a
	 *            leg a
	 * @param b
	 *            leg b
	 * @return length of hypotenuse
	 */
	public static double getHypotenuseLength(double a, double b) {
		return Math.sqrt(BasicMath.square(a) + BasicMath.square(b));
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

		if (s > 1 || r > 1 || s < 0 || r < 0 || Double.isNaN(s)
				|| Double.isNaN(r) || Double.isInfinite(r)
				|| Double.isInfinite(s)) {
			return null;
		}

		Vector2D intersectPoint = second.getPointAt(s);

		return intersectPoint;

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
	public static LinkedList<Line> getRelativeBorderLines(Vector2D[] vertices) {

		LinkedList<Line> borderLines = new LinkedList<Line>();

		for (int i = 0; i < vertices.length; i++) {
			Line borderLine = new Line(vertices[i], vertices[(i + 1)
					% vertices.length]);
			borderLines.add(borderLine);
		}

		return borderLines;

	}
}
