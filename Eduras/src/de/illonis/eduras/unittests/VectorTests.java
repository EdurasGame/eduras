package de.illonis.eduras.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.illonis.eduras.math.Vector2D;

/**
 * Unit test class for Vector2D.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class VectorTests {

	Vector2D vector, lengthVector;

	/**
	 * Executed before each test to set up some vectors.
	 */
	@Before
	public void setUp() {
		vector = new Vector2D(100, 100);
		lengthVector = new Vector2D(4, 3);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2D#rotate(double) rotate()}
	 * method of a Vector2D.
	 */
	@Test
	public void rotations() {
		Vector2D copy = vector.copy();
		copy.rotate(0);
		assertEquals(vector, copy);
		copy.rotate(360);
		assertEquals(vector, copy);
		copy.rotate(90);
		assertEquals(new Vector2D(-100, 100), copy);
		copy.rotate(90);
		assertEquals(new Vector2D(-100, -100), copy);
		copy.rotate(90);
		Vector2D check = new Vector2D(100, -100);
		assertEquals(check, copy);
		copy.rotate(90);
		assertEquals(vector, copy);
		copy.rotate(-90);
		assertEquals(check, copy);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2D#mult(double) mult()}
	 * method of a Vector2D.
	 */
	@Test
	public void mult() {
		vector.mult(2);
		assertTrue(vector.getX() == 200);
		assertTrue(vector.getY() == 200);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2D#getLength() getLength()}
	 * and {@link de.illonis.eduras.math.Vector2D#setLength(double) setLength()}
	 * methods of a Vector2D.
	 */
	@Test
	public void lengthTests() {
		assertEquals(5, lengthVector.getLength(), 0);
		lengthVector.setLength(10);
		assertEquals(10, lengthVector.getLength(), 0);
		assertEquals(8, lengthVector.getX(), 0);
		assertEquals(6, lengthVector.getY(), 0);
		lengthVector.mult(0.5);
		assertEquals(5, lengthVector.getLength(), 0);

		assertEquals(5, lengthVector.getLength(), 0);
		assertEquals("Length of unit vector", 1, lengthVector.getUnitVector()
				.getLength(), 0);

		Vector2D second = lengthVector.copy();
		second.invert();
		lengthVector.mult(-1);

		assertEquals("Negative vector is same as vector *-1.", second,
				lengthVector);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2D#getUnitVector()
	 * getUnitVector()} method of a Vector2D.
	 */
	@Test
	public void unitVectorTests() {
		Vector2D second = lengthVector.copy();
		second.mult(17.34);
		assertEquals(
				"Unitvectors of two linear dependend vectors should be the same.",
				lengthVector.getUnitVector(), second.getUnitVector());
	}

	/**
	 * Tests the
	 * {@link de.illonis.eduras.math.Vector2D#findShortestDistance(LinkedList, Vector2D)
	 * findShortestDistance()} method of a Vector2D.
	 */
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

	/**
	 * Executed after each test method.
	 */
	@After
	public void deSetUp() {
		vector = null;
	}
}
