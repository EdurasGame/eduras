package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.shapes.Polygon;

/**
 * Test the {@link DynamicPolygonObject} behaviour.
 * 
 * @author illonis
 * 
 */
public class PolygonTests {

	private DynamicPolygonObject polyBlock;

	/**
	 * Init process
	 * 
	 * @author illonis
	 */
	@Before
	public void init() {
		polyBlock = new DynamicPolygonObject(ObjectType.DYNAMIC_POLYGON_BLOCK,
				null, null, 0);
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
		Vector2df[] vertices = new Vector2df[] { new Vector2df(3, 4),
				new Vector2df(6, 8.5) };
		polyBlock.setPolygonVector2dfs(vertices);

		assertTrue(vertices.equals(((Polygon) polyBlock.getShape())
				.getVector2dfsAsArray()));

	}
}
