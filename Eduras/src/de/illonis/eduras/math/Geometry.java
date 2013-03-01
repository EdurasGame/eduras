package de.illonis.eduras.math;

import java.util.LinkedList;

import de.illonis.eduras.shapes.Circle;

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
	 * Converts given radian value to degree.
	 * 
	 * @param radian
	 *            value in radian.
	 * @return value in degree.
	 */
	public static double toDegree(double radian) {
		return radian * (180 / Math.PI);
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
	 *         the intercept point as a vector otherwise. If there's an infinite
	 *         number of intercept points, one of them will be returned (but you
	 *         cant say which).
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

	/**
	 * Calculates the interception points of a circle and a line segment. This
	 * method assumes that the given line is a valid one, means that it's
	 * directional vector must not be the nullvector.
	 * 
	 * @param circle
	 *            The circle.
	 * @param centerPoint
	 *            The circle's center point.
	 * @param singleLine
	 *            The line.
	 * @return The intercept points as an array of two, if there are any. If
	 *         there is only one intercept point, one of the array entrys is
	 *         null. If there is no intercept points, both entrys are left null.
	 */
	public static Vector2D[] getCircleLineSegmentInterceptPoints(Circle circle,
			Vector2D centerPoint, Line singleLine) {

		Vector2D[] result = new Vector2D[2];

		// the calculation of the intercept point is derived from the
		// mathematical approach of simply having equations for the circle and
		// the line containing the x and y values of the intercept point.
		//
		// line: let (u1,u2) be the line's supportvector and (v1,v2) the line's
		// directional vector. For any point (x,y) on the line the following
		// equations hold:
		// 1. x = u1 + v1 * lambda
		// 2. y = u2 + v2 * lambda

		double u1 = singleLine.getSupportVector().getX();
		double u2 = singleLine.getSupportVector().getY();

		double v1 = singleLine.getDirectionalVector().getX();
		double v2 = singleLine.getDirectionalVector().getY();

		// circle: let (m1,m2) the circle's center point and r its radius. For
		// any point on the circle
		// the following equation holds:
		// 3. r^2 = (x - m1)^2 + (y - m2)^2

		double r = circle.getRadius();
		double m1 = centerPoint.getX();
		double m2 = centerPoint.getY();

		// Applying equations one and two for the third and solving for lambda
		// gives a square equation (in pq formula) having the following p and q:

		double p = (2 * v1 * u1 - 2 * v1 * m1 + 2 * v2 * u2 - 2 * m2 * v2)
				/ (BasicMath.square(v1) + BasicMath.square(v2));

		double q = (BasicMath.square(u1) + BasicMath.square(u2)
				+ BasicMath.square(m1) + BasicMath.square(m2) - 2 * m2 * u2 - 2
				* m1 * u1 - BasicMath.square(r))
				/ (BasicMath.square(v1) + BasicMath.square(v2));

		// there is a solution if the pq formula's radiant is >= 0.
		// check wether there is a solution:

		double pqRadian = BasicMath.square(p / 2) - q;

		if (pqRadian < 0) {
			return result;
		}

		// if there is a solution, there is two:

		double lambdaOne = -(p / 2) + Math.sqrt(pqRadian);
		double lambdaTwo = -(p / 2) - Math.sqrt(pqRadian);

		// check if the points are on the line seqment.

		Vector2D firstInterceptPoint = null;
		Vector2D secondInterceptPoint = null;

		if (lambdaOne <= 1 && lambdaOne >= 0) {
			firstInterceptPoint = singleLine.getPointAt(lambdaOne);
		}

		if (lambdaTwo <= 1 && lambdaTwo >= 0) {
			secondInterceptPoint = singleLine.getPointAt(lambdaTwo);
		}

		result[0] = firstInterceptPoint;
		result[1] = secondInterceptPoint;

		return result;
	}
}
