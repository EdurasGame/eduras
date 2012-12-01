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
		double r = 0;

		// We must distinguish between cases when one or more of the direction
		// vectors are zero.
		// The cases firstDirectionX == firstDirectionY == 0 and
		// secondDirectionX == secondDirectionY == 0 cannot (or should not)
		// occur because then one of the lines is not really a line.
		// If the directionVectors are linearly depending on each other, the
		// lines might be the same, so we must
		// check for that.
		// In any other case we can give a calculation for s and r.

		if ((firstDirectionX == 0 && firstDirectionY == 0)
				|| (secondDirectionX == 0 && secondDirectionY == 0)) {
			try {
				throw new Exception(
						"There was a line given that was not correct.");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		if (firstDirectionalVector.isLinearTo(secondDirectionalVector)) {

			boolean firstIn = false;
			boolean secondIn = false;

			if (second.containsPoint(firstSupportVector)) {
				firstIn = firstSupportVector.calculateDistance(first.getV()) > firstSupportVector
						.calculateDistance(secondSupportVector);
				secondIn = secondSupportVector.calculateDistance(second.getV()) > secondSupportVector
						.calculateDistance(firstSupportVector);
				if (firstIn && secondIn) {
					return secondSupportVector;
				}
			} else {
				return null;
			}
		}

		if (firstDirectionX != 0 && firstDirectionY != 0
				&& secondDirectionX != 0 && secondDirectionY != 0) {

			s = (firstSupportY - secondSupportY + ((secondSupportX - firstSupportX) * firstDirectionY)
					/ firstDirectionX)
					/ (secondDirectionY - ((secondDirectionX * firstDirectionY) / firstDirectionX));

			r = (secondSupportX + s * secondDirectionX - firstSupportX)
					/ firstDirectionX;
			// the first line segment ends for s = 1, the second ends for r = 1
			// so
			// if s > 1 || r > 1 there is no intersection
			// points

			if (checkNotWithin(s, r)) {
				return null;
			}

			return second.getPointAt(s);
		}

		if (firstDirectionX == 0 && secondDirectionY == 0) {
			s = (firstSupportX - secondSupportX) / secondDirectionX;
			r = (secondSupportY - firstSupportY) / firstDirectionY;
			if (!checkNotWithin(s, r)) {
				return second.getPointAt(s);
			}
			return null;
		}

		if (firstDirectionX == 0) {
			s = (firstSupportX - secondSupportX) / secondDirectionX;
			r = (secondSupportY - firstSupportY + ((firstSupportX - secondSupportX) * secondDirectionY)
					/ secondDirectionX)
					/ firstDirectionY;
			if (!checkNotWithin(s, r)) {
				return second.getPointAt(s);
			}
			return null;
		}

		if (firstDirectionY == 0 && secondDirectionX == 0) {
			s = (firstSupportY - secondSupportY) / secondDirectionY;
			r = (secondSupportX - firstSupportX) / firstDirectionX;
			if (!checkNotWithin(s, r)) {
				return second.getPointAt(s);
			}
			return null;
		}

		if (firstDirectionY == 0) {
			s = (firstSupportY - secondSupportY) / secondDirectionY;
			r = (secondSupportX - firstSupportX + ((firstSupportY - secondSupportY)
					* secondDirectionX / secondDirectionY))
					/ firstDirectionX;
			if (!checkNotWithin(s, r)) {
				return second.getPointAt(s);
			}
			return null;
		}

		if (secondDirectionX == 0) {
			r = (secondSupportX - firstSupportX) / firstDirectionX;
			s = (firstSupportY - secondSupportY + ((secondSupportX - firstSupportX)
					* firstDirectionY / firstDirectionX))
					/ secondDirectionY;
			if (!checkNotWithin(s, r)) {
				return second.getPointAt(s);
			}
			return null;
		}

		if (secondDirectionY == 0) {
			r = (secondSupportY - firstSupportY) / firstDirectionY;
			s = (firstSupportX - secondSupportX + ((secondSupportY - firstSupportY)
					* firstDirectionX / firstDirectionY))
					/ secondDirectionX;
			if (!checkNotWithin(s, r)) {
				return second.getPointAt(s);
			}
			return null;
		}

		return null;

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
