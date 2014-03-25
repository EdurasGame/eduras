package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2df;

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

		Line line1 = new Line(new Vector2df(1, 0), new Vector2df(-1, 0));
		Line line2 = new Line(new Vector2df(0, 1), new Vector2df(0, -1));

		Vector2df interceptPoint = Geometry.getSegmentLinesInterceptPoint(
				line1, line2);

		assertTrue(interceptPoint.isNull());

		Line line3 = new Line(new Vector2df(1, 1), new Vector2df(-1, -1));
		assertTrue(Geometry.getSegmentLinesInterceptPoint(line1, line3)
				.isNull());
		assertTrue(Geometry.getSegmentLinesInterceptPoint(line3, line2)
				.isNull());

		Line line4 = new Line(new Vector2df(-3, -5), new Vector2df(1, 3));

		interceptPoint = Geometry.getSegmentLinesInterceptPoint(line4, line1);

		assertTrue(interceptPoint.equals(new Vector2df(-0.5f, 0)));

		Line line5 = new Line(new Vector2df(2, 0), new Vector2df(-2, 0));

		interceptPoint = Geometry.getSegmentLinesInterceptPoint(line1, line5);

		assertTrue(interceptPoint != null);
	}

	/**
	 * Tests the
	 * {@link de.illonis.eduras.math.Geometry#getLinesBetweenShapePositions(Vector2df[], Vector2df, Vector2df)
	 * getLinesBetweenShapePositions} method.
	 */
	@Test
	public void getLinesBetweenShapePositions() {
		Vector2df[] vertices = new Vector2df[2];
		vertices[0] = new Vector2df(-1, 0);
		vertices[1] = new Vector2df(1, 0);

		Vector2df source = new Vector2df(0, 0);
		Vector2df dest = new Vector2df(0, 5);

		Line expectedLine1 = new Line(new Vector2df(-1, 0),
				new Vector2df(-1, 5));
		Line expectedLine2 = new Line(new Vector2df(1, 0), new Vector2df(1, 5));

		LinkedList<Line> resultLines = Geometry.getLinesBetweenShapePositions(
				vertices, source, dest);

		assertTrue(resultLines.get(0).equals(expectedLine1));
		assertTrue(resultLines.get(1).equals(expectedLine2));
	}
}
