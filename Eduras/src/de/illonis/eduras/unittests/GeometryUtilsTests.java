/**
 * 
 */
package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import de.illonis.eduras.exceptions.PointNotOnCircleException;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;

/**
 * Tests for several methods in Geometry-class.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class GeometryUtilsTests {

	/**
	 * Tests the
	 * {@link de.illonis.eduras.math.Geometry#getSegmentLinesInterceptPoint(Line, Line)
	 * getSegmentLineInterceptPoint} method.
	 */
	@Test
	public void getSegmentLineInterceptPoint() {

		Line line1 = new Line(new Vector2D(1, 0), new Vector2D(-1, 0));
		Line line2 = new Line(new Vector2D(0, 1), new Vector2D(0, -1));

		Vector2D interceptPoint = Geometry.getSegmentLinesInterceptPoint(line1,
				line2);

		assertTrue(interceptPoint.isNull());

		Line line3 = new Line(new Vector2D(1, 1), new Vector2D(-1, -1));
		assertTrue(Geometry.getSegmentLinesInterceptPoint(line1, line3)
				.isNull());
		assertTrue(Geometry.getSegmentLinesInterceptPoint(line3, line2)
				.isNull());

		Line line4 = new Line(new Vector2D(-3, -5), new Vector2D(1, 3));

		interceptPoint = Geometry.getSegmentLinesInterceptPoint(line4, line1);

		assertTrue(interceptPoint.equals(new Vector2D(-0.5, 0)));

		Line line5 = new Line(new Vector2D(2, 0), new Vector2D(-2, 0));

		interceptPoint = Geometry.getSegmentLinesInterceptPoint(line1, line5);

		assertTrue(interceptPoint != null);
	}

	/**
	 * Tests the
	 * {@link de.illonis.eduras.math.Geometry#getLinesBetweenShapePositions(Vector2D[], Vector2D, Vector2D)
	 * getLinesBetweenShapePositions} method.
	 */
	@Test
	public void getLinesBetweenShapePositions() {
		Vector2D[] vertices = new Vector2D[2];
		vertices[0] = new Vector2D(-1, 0);
		vertices[1] = new Vector2D(1, 0);

		Vector2D source = new Vector2D(0, 0);
		Vector2D dest = new Vector2D(0, 5);

		Line expectedLine1 = new Line(new Vector2D(-1, 0), new Vector2D(-1, 5));
		Line expectedLine2 = new Line(new Vector2D(1, 0), new Vector2D(1, 5));

		LinkedList<Line> resultLines = Geometry.getLinesBetweenShapePositions(
				vertices, source, dest);

		assertTrue(resultLines.get(0).equals(expectedLine1));
		assertTrue(resultLines.get(1).equals(expectedLine2));
	}

	/**
	 * Tests the
	 * {@link de.illonis.eduras.math.Geometry#getCircleLineSegmentInterceptPoints(Circle, Vector2D, Line)
	 * getCircleLineSegmentInterceptPoints} method.
	 */
	@Test
	public void getCircleLineSegmentInterceptPoints() {
		Circle circle = new Circle(1);

		Vector2D nullVector = new Vector2D();
		Line xLine = new Line(new Vector2D(-2, 0), new Vector2D(2, 0));

		Vector2D[] interceptPoints = Geometry
				.getCircleLineSegmentInterceptPoints(circle, nullVector, xLine);

		Vector2D expectedResult1 = new Vector2D(-1, 0);
		Vector2D expectedResult2 = new Vector2D(1, 0);

		assertTrue(interceptPoints[0].equals(expectedResult1)
				|| interceptPoints[1].equals(expectedResult1));
		assertTrue(interceptPoints[0].equals(expectedResult2)
				|| interceptPoints[1].equals(expectedResult2));
	}

	/**
	 * Tests the
	 * {@link de.illonis.eduras.math.Geometry#getAngleForPointOnCircle(Circle, Vector2D, Vector2D)}
	 * method.
	 */
	@Test
	public void testGetAngleForPointOnCirlce() {
		Vector2D nullVector = new Vector2D(0, 0);

		Circle circle1 = new Circle(5);
		Vector2D point1onCircle1 = new Vector2D(5, 0);
		Vector2D point2onCircle1 = new Vector2D(0, 5);
		Vector2D point3onCircle1 = new Vector2D(-5, 0);
		Vector2D point4onCircle1 = new Vector2D(0, -5);

		try {
			assertTrue(0. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, point1onCircle1));
			assertTrue(90. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, point2onCircle1));
			assertTrue(180. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, point3onCircle1));
			assertTrue(270. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, point4onCircle1));
			assertTrue(45. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, new Vector2D(Math.sqrt(12), Math.sqrt(12))));
			assertTrue(135. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, new Vector2D(-Math.sqrt(12), Math.sqrt(12))));
			assertTrue(225. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, new Vector2D(-Math.sqrt(12), -Math.sqrt(12))));
			assertTrue(315. == Geometry.getAngleForPointOnCircle(circle1,
					nullVector, new Vector2D(Math.sqrt(12), -Math.sqrt(12))));
		} catch (PointNotOnCircleException e) {
			assertTrue(false);
		}

		try {
			Geometry.getAngleForPointOnCircle(circle1, nullVector,
					new Vector2D(6, 1));
			assertTrue(false);
		} catch (PointNotOnCircleException e) {

		}

		try {
			Geometry.getAngleForPointOnCircle(circle1, nullVector,
					new Vector2D(0, 0));
			assertTrue(false);
		} catch (PointNotOnCircleException e) {

		}
	}
}
