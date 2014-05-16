package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.math.Geometry;
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
	 * {@link de.illonis.eduras.math.Geometry#getLinesBetweenShapePositions(Vector2f[], Vector2f, Vector2f)
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
