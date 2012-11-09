/**
 * 
 */
package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * Tests for several methods in Geometry-class.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class GeometryUtilsTests {

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
	}

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
}
