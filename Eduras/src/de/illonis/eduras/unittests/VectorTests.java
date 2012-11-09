/**
 * 
 */
package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.illonis.eduras.math.Vector2D;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class VectorTests {

	Vector2D vector;

	@Before
	public void setUp() {
		vector = new Vector2D(100, 100);
	}

	@Test
	public void mult() {
		vector.mult(2);
		assertTrue(vector.getX() == 200);
		assertTrue(vector.getY() == 200);
	}

	@Test
	public void findClosestPoint() {

		LinkedList<Vector2D> otherVectors = new LinkedList<Vector2D>();

		otherVectors.add(vector);
		assertTrue(Vector2D.findShortestDistance(otherVectors, vector) == vector);
		assertTrue(Vector2D.findShortestDistance(otherVectors, new Vector2D(0,
				0)) == vector);

		Vector2D closeVector = new Vector2D(101, 100);
		Vector2D anotherCloseVector = new Vector2D(100, 101);

		otherVectors.clear();
		otherVectors.add(closeVector);
		otherVectors.add(anotherCloseVector);

		assertTrue(Vector2D.findShortestDistance(otherVectors, vector) == closeVector);

	}

	@After
	public void deSetUp() {
		vector = null;
	}
}
