package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.illonis.eduras.gameobjects.DynamicPolygonBlock;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.shapes.Polygon;

/**
 * Test the {@link DynamicPolygonBlock} behaviour.
 * 
 * @author illonis
 * 
 */
public class PolygonTests {

	private DynamicPolygonBlock polyBlock;

	/**
	 * Init process
	 * 
	 * @author illonis
	 */
	@Before
	public void init() {
		polyBlock = new DynamicPolygonBlock(null, 0);
	}

	/**
	 * Tests the empty polygon.
	 * 
	 * @author illonis
	 */
	@Test
	public void emptyPolygon() {
		ObjectShape shape = polyBlock.getShape();
		Polygon p = new Polygon();
		assertTrue(shape.equals(p));
	}

	/**
	 * Tests if added vertices are in polygon.
	 * 
	 * @author illonis
	 */
	@Test
	public void someVerts() {
		Vector2D[] vertices = new Vector2D[] { new Vector2D(3, 4),
				new Vector2D(6, 8.5) };
		polyBlock.setPolygonVertices(vertices);

		assertTrue(vertices.equals(((Polygon) polyBlock.getShape())
				.getVerticesAsArray()));

	}
}