package de.illonis.eduras.math;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.exceptions.ShapeNotSupportedException;
import de.illonis.eduras.settings.S;

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
	 * Returns true if first rectangle completely contains second rectangle.<br>
	 * This method is required as slicks inbuild method does not work for
	 * rectangles.
	 * 
	 * @param a
	 *            first rectangle.
	 * @param b
	 *            second rectangle.
	 * @return true if a contains b.
	 */
	public static boolean rectangleContains(Rectangle a, Rectangle b) {
		float ax = a.getX();
		float ay = a.getY();
		float aw = a.getWidth();
		float ah = a.getHeight();

		float bx = b.getX();
		float by = b.getY();
		float bw = b.getWidth();
		float bh = b.getHeight();

		return ax < bx && (bx + bw) < (ax + aw) && ay < by
				&& (by + bh) < (ay + ah);
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
	 * Converts an array of floats to an array of vectors where vector[i] =
	 * (float[2 * i], float[2 * i + 1]).
	 * 
	 * @param floats
	 * @return vectors
	 */
	public static Vector2f[] floatsToVectors(float[] floats) {
		Vector2f result[] = new Vector2f[floats.length / 2];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Vector2f(floats[2 * i], floats[2 * i + 1]);
		}
		return result;
	}

	/**
	 * Returns all the lines between a shape and its counterpart of a second
	 * shape, that is a shifted copy of the first shape.
	 * 
	 * @param shape
	 *            the shape.
	 * @param destination
	 *            The middle point of the shapes copy.
	 * @return Returns a list containing the resulting lines.
	 */
	public static LinkedList<Line> getLinesBetweenShapePositions(Shape shape,
			Vector2f destination) {

		LinkedList<Line> lines = new LinkedList<Line>();

		Vector2f originalPosition = new Vector2f(shape.getX(), shape.getY());
		Vector2f[] pointsOfOriginalPosition = floatsToVectors(shape.getPoints());

		shape.setLocation(destination);
		Vector2f[] pointOfDestinationPosition = floatsToVectors(shape
				.getPoints());
		shape.setLocation(originalPosition);

		for (int i = 0; i < pointsOfOriginalPosition.length; i++) {
			lines.add(new Line(pointsOfOriginalPosition[i],
					pointOfDestinationPosition[i]));
		}

		return lines;
	}

	/**
	 * Returns 'numpoints' points on the circle with the origin as its
	 * centerpoint such that two neighboured points have the same distance.
	 * 
	 * @param circle
	 *            the circle to return the points of
	 * @param numPoints
	 *            The number of points you want to receive.
	 * @return Returns a list containing the points on the circle.
	 */
	public static LinkedList<Vector2f> getPointsOnCirlce(Ellipse circle,
			int numPoints) {

		LinkedList<Vector2f> result = new LinkedList<Vector2f>();
		float angle = (float) (2 * Math.PI) / numPoints;

		for (int i = 0; i < numPoints; i++) {
			result.add(getPointOnCircleAtAngle(circle, i * angle));
		}
		return result;
	}

	/**
	 * Returns the point on the circle at the given angle.
	 * 
	 * @param circle
	 * @param angle
	 *            The angle on the circle where to find the point.
	 * @return The point.
	 */
	public static Vector2f getPointOnCircleAtAngle(Ellipse circle, float angle) {

		// assuming this is a circle, so the radii are identical
		float pointXPos = (float) (Math.cos(angle) * circle.getRadius1());
		float pointYPos = (float) (Math.sin(angle) * circle.getRadius1());

		return new Vector2f(pointXPos, pointYPos);
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
	public static LinkedList<Line> getBorderLines(Vector2f[] vertices) {

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

	/**
	 * Calculates all {@link CollisionPoint}s that occur when checking if the
	 * given shape is intersected by the given lines.
	 * 
	 * @param lines
	 * @param shape
	 * @return a list of {@link CollisionPoint}.
	 * @throws ShapeNotSupportedException
	 */
	public static LinkedList<CollisionPoint> isShapeIntersectedByLines(
			LinkedList<Line> lines, Shape shape)
			throws ShapeNotSupportedException {

		if (shape instanceof Polygon) {
			return isPolygonIntersectedByLines(lines, (Polygon) shape);
		}

		if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			return isPolygonIntersectedByLines(lines,
					new Polygon(rectangle.getPoints()));
		}

		if (shape instanceof Circle) {

			return isCircleIntersectedByLines(lines, (Circle) shape);
		}

		throw new ShapeNotSupportedException(shape);
	}

	private static LinkedList<CollisionPoint> isCircleIntersectedByLines(
			LinkedList<Line> lines, Circle circle) {

		LinkedList<CollisionPoint> result = new LinkedList<CollisionPoint>();

		for (Line singleLine : lines) {

			// check wether given line is valid
			// if (singleLine.getDirectionalVector().isNull()) {
			// continue;
			// }

			Vector2f[] interceptionPoints = Geometry
					.getCircleLineSegmentInterceptPoints(circle, new Vector2f(
							circle.getCenterX(), circle.getCenterY()),
							singleLine);

			if (interceptionPoints[0] == null && interceptionPoints[1] == null) {
				continue;
			}

			for (int i = 0; i < 2; i++) {

				if (interceptionPoints[i] != null) {

					CollisionPoint collisionPoint = CollisionPoint
							.createCollisionPointByInterceptPoint(
									interceptionPoints[i], singleLine, 0);
					result.add(collisionPoint);
				}
			}

		}

		return result;
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
	public static Vector2f[] getCircleLineSegmentInterceptPoints(Circle circle,
			Vector2f centerPoint, Line singleLine) {

		Vector2f[] result = new Vector2f[2];

		// the calculation of the intercept point is derived from the
		// mathematical approach of simply having equations for the circle and
		// the line containing the x and y values of the intercept point.
		//
		// line: let (u1,u2) be the line's supportvector and (v1,v2) the line's
		// directional vector. For any point (x,y) on the line the following
		// equations hold:
		// 1. x = u1 + v1 * lambda
		// 2. y = u2 + v2 * lambda

		float u1 = singleLine.getX1();
		float u2 = singleLine.getY1();

		float v1 = singleLine.getDX();
		float v2 = singleLine.getDY();

		// circle: let (m1,m2) the circle's center point and r its radius. For
		// any point on the circle
		// the following equation holds:
		// 3. r^2 = (x - m1)^2 + (y - m2)^2

		float r = circle.getRadius();
		float m1 = centerPoint.getX();
		float m2 = centerPoint.getY();

		// Applying equations one and two for the third and solving for lambda
		// gives a square equation (in pq formula) having the following p and q:

		float p = (2 * v1 * u1 - 2 * v1 * m1 + 2 * v2 * u2 - 2 * m2 * v2)
				/ (BasicMath.square(v1) + BasicMath.square(v2));

		float q = (BasicMath.square(u1) + BasicMath.square(u2)
				+ BasicMath.square(m1) + BasicMath.square(m2) - 2 * m2 * u2 - 2
				* m1 * u1 - BasicMath.square(r))
				/ (BasicMath.square(v1) + BasicMath.square(v2));

		// there is a solution if the pq formula's radiant is >= 0.
		// check whether there is a solution:

		float pqRadian = BasicMath.square(p / 2) - q;

		if (pqRadian < 0) {
			return result;
		}

		// if there is a solution, there is two:

		float lambdaOne = -(p / 2) + (float) Math.sqrt(pqRadian);
		float lambdaTwo = -(p / 2) - (float) Math.sqrt(pqRadian);

		// check if the points are on the line seqment.

		Vector2f firstInterceptPoint = null;
		Vector2f secondInterceptPoint = null;

		if (lambdaOne <= 1 && lambdaOne >= 0) {
			firstInterceptPoint = getPointOnLineAt(singleLine, lambdaOne);
		}

		if (lambdaTwo <= 1 && lambdaTwo >= 0) {
			secondInterceptPoint = getPointOnLineAt(singleLine, lambdaTwo);
		}

		result[0] = firstInterceptPoint;
		result[1] = secondInterceptPoint;

		return result;
	}

	private static LinkedList<CollisionPoint> isPolygonIntersectedByLines(
			LinkedList<Line> lines, Polygon polygon) {
		LinkedList<CollisionPoint> interceptPoints = new LinkedList<CollisionPoint>();

		for (Line line : lines) {
			for (Line borderLine : getBorderLines(polygon)) {
				Vector2f interceptPoint = Geometry
						.getSegmentLinesInterceptPoint(borderLine, line);

				if (interceptPoint == null) {
					continue;
				} else {
					float angle = getAngleBetweenLines(borderLine, line);
					angle = BasicMath.calcModulo(angle, 180);

					CollisionPoint interception = CollisionPoint
							.createCollisionPointByInterceptPoint(
									interceptPoint, line, angle);
					interceptPoints.add(interception);
				}

			}
		}
		return interceptPoints;
	}

	/**
	 * Returns the angle between two lines, that is, the angle between their
	 * directional vectors.
	 * 
	 * @param first
	 * @param second
	 * @return angle between lines
	 */
	public static float getAngleBetweenLines(Line first, Line second) {
		Vector2df directionalVectorOfFirst = new Vector2df(
				getDirectionalVectorOfLine(first));
		Vector2df directionalVectorOfSecond = new Vector2df(
				getDirectionalVectorOfLine(second));
		return directionalVectorOfFirst
				.getAngleBetween(directionalVectorOfSecond);
	}

	/**
	 * Calculates the borderlines spanned by the given shape's vertices assuming
	 * that a borderline i is given by a line between vertex no i and vertex no
	 * (i + 1).
	 * 
	 * @param shape
	 * @return see {@link #getBorderLines(Vector2f[])}
	 */
	public static LinkedList<Line> getBorderLines(Shape shape) {
		return Geometry.getBorderLines(Geometry.floatsToVectors(shape
				.getPoints()));
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
	public static Vector2f getSegmentLinesInterceptPoint(Line first, Line second) {
		Vector2f result = new Vector2f(0, 0);
		Boolean limitToExtend = true;
		boolean doesIntersect = first.intersect(second, limitToExtend, result);

		// do this switch because not sure what intersect does with
		// "result"-vector if there is no intersection
		if (doesIntersect) {
			return result;
		} else {
			return null;
		}
	}

	/**
	 * Calculates whether the first vector is linearly depending on the second
	 * vector.
	 * 
	 * @param firstVector
	 * @param secondVector
	 *            second vector.
	 * 
	 * @return true if vectors are linearly depending.
	 */
	public static boolean isVectorLinearTo(Vector2f firstVector,
			Vector2f secondVector) {

		if (firstVector.getX() == 0) {
			return secondVector.getX() == 0;
		}

		if (firstVector.getY() == 0) {
			return secondVector.getY() == 0;
		}

		// reaching this case X and Y of this vector cannot be 0 anymore.
		if (secondVector.getX() == 0 || secondVector.getY() == 0) {
			return false;
		}

		// so now there no component of any vector can be zero.

		double r = secondVector.getX() / firstVector.getX();

		return firstVector.getY() * r == secondVector.getY();
	}

	/**
	 * This function returns the point you will get by multiplying the given //
	 * * lambda with the directional vector of the line (considered to be
	 * endpoint - startpoint) and add it to the support vector 'start point'.
	 * 
	 * @param line
	 *            the line
	 * @param lambda
	 *            * The multiplier.
	 * @return Returns u + uv * lambda
	 */
	public static Vector2f getPointOnLineAt(Line line, float lambda) {
		Vector2f startPoint = new Vector2f(line.getX1(), line.getY1());
		Vector2f directionalVector = getDirectionalVectorOfLine(line);
		return startPoint.add(directionalVector.scale(lambda));
	}

	/**
	 * Returns the directional vector of a line.
	 * 
	 * @param line
	 * @return the directional vector
	 */
	public static Vector2f getDirectionalVectorOfLine(Line line) {
		Vector2f startPoint = new Vector2f(line.getX1(), line.getY1());
		Vector2f endPoint = new Vector2f(line.getX2(), line.getY2());
		endPoint.sub(startPoint);
		return endPoint;
	}

	/**
	 * Returns an array of vectors which approximate the borders of the given
	 * shape.
	 * 
	 * @param shape
	 *            the shape to return the border points of
	 * @return points
	 * @throws ShapeNotSupportedException
	 *             Thrown if the given shape type is not supported
	 */
	public Vector2f[] getBorderPointsForShape(Shape shape)
			throws ShapeNotSupportedException {
		if (shape instanceof Polygon) {
			return getPolygonBorderPoints((Polygon) shape);
		}

		if (shape instanceof Ellipse) {
			Vector2f[] result = new Vector2f[0];
			int numberOfPoints = getNumberOfPointsOnCircleForDistance(
					(Circle) shape, S.Server.sv_performance_collision_accuracy);
			result = Geometry
					.getPointsOnCirlce((Ellipse) shape, numberOfPoints)
					.toArray(result);
			return result;
		}

		throw new ShapeNotSupportedException(shape);
	}

	private int getNumberOfPointsOnCircleForDistance(Circle circle,
			float distance) {
		// calculates how many points fit onto a circle if the point have the
		// given distance to each other
		float circumference = calculateCircumference(circle);
		return Math.round(circumference / distance);
	}

	/**
	 * Calculates the circumference of a circle.
	 * 
	 * @param circle
	 * @return circumference
	 */
	public static float calculateCircumference(Circle circle) {
		return (float) (circle.radius * 2f * Math.PI);
	}

	private Vector2f[] getPolygonBorderPoints(Polygon shape) {
		Vector2f[] vertices = Geometry.floatsToVectors(shape.getPoints());

		LinkedList<Line> borderLines = Geometry.getBorderLines(vertices);

		ArrayList<Vector2f> movementPoints = new ArrayList<Vector2f>();

		for (Line singleBorderLine : borderLines) {
			int numberOfPoints = numberOfPointsOnLineForDistance(
					singleBorderLine,
					S.Server.sv_performance_collision_accuracy);

			for (int i = 0; i < numberOfPoints; i++) {
				movementPoints.add(Geometry.getPointOnLineAt(singleBorderLine,
						(1f / numberOfPoints) * i));
			}
		}

		return movementPoints.toArray(new Vector2f[1]);
	}

	private int numberOfPointsOnLineForDistance(Line singleBorderLine,
			float distance) {
		return Math.round(singleBorderLine.length() / distance);
	}

	/**
	 * Returns an inverted copy of the given vector.
	 * 
	 * @param vectorToInvert
	 * @return inverted copy
	 */
	public static Vector2f invert(Vector2f vectorToInvert) {
		return new Vector2f(vectorToInvert.x * -1, vectorToInvert.y * -1);
	}

	public static Vector2f calculateDistanceVector(Vector2f first,
			Vector2f second) {
		Vector2f copy = second.copy();
		return copy.sub(first);
	}
}
