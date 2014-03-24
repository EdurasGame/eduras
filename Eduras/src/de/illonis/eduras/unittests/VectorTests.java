package de.illonis.eduras.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.illonis.eduras.math.Vector2df;

/**
 * Unit test class for Vector2df.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class VectorTests {

	Vector2df vector, lengthVector;

	/**
	 * Executed before each test to set up some vectors.
	 */
	@Before
	public void setUp() {
		vector = new Vector2df(100, 100);
		lengthVector = new Vector2df(4, 3);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2df#rotate(double) rotate()}
	 * method of a Vector2df.
	 */
	@Test
	public void rotations() {
		Vector2df copy = vector.copy();
		copy.rotate(0);
		assertEquals(vector, copy);
		copy.rotate(360);
		assertEquals(vector, copy);
		copy.rotate(90);
		assertEquals(new Vector2df(-100, 100), copy);
		copy.rotate(90);
		assertEquals(new Vector2df(-100, -100), copy);
		copy.rotate(90);
		Vector2df check = new Vector2df(100, -100);
		assertEquals(check, copy);
		copy.rotate(90);
		assertEquals(vector, copy);
		copy.rotate(-90);
		assertEquals(check, copy);
	}

	@Test
	public void angles() {
		Vector2df posYAxis = new Vector2df(0, 1);
		Vector2df negYAxis = new Vector2df(0.1, 1);

		assertTrue(Math.abs(posYAxis.getAngleToXAxis() - 90) < 0.0001);

		assertTrue(Math.abs(posYAxis.getAngleBetween(negYAxis)) - 180 < 1);

		assertTrue(Math.abs(new Vector2df(-1, 0).getAngleToXAxis() - 180) < 0.00001);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2df#mult(double) mult()}
	 * method of a Vector2df.
	 */
	@Test
	public void mult() {
		vector.mult(2);
		assertTrue(vector.getX() == 200);
		assertTrue(vector.getY() == 200);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2df#getLength() getLength()}
	 * and {@link de.illonis.eduras.math.Vector2df#setLength(double) setLength()}
	 * methods of a Vector2df.
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

		Vector2df second = lengthVector.copy();
		second.invert();
		lengthVector.mult(-1);

		assertEquals("Negative vector is same as vector *-1.", second,
				lengthVector);
	}

	/**
	 * Tests the {@link de.illonis.eduras.math.Vector2df#getUnitVector()
	 * getUnitVector()} method of a Vector2df.
	 */
	@Test
	public void unitVectorTests() {
		Vector2df second = lengthVector.copy();
		second.mult(17.34);
		assertEquals(
				"Unitvectors of two linear dependend vectors should be the same.",
				lengthVector.getUnitVector(), second.getUnitVector());
	}

	/**
	 * Tests the
	 * {@link de.illonis.eduras.math.Vector2df#findShortestDistance(LinkedList, Vector2df)
	 * findShortestDistance()} method of a Vector2df.
	 */
	@Test
	public void findClosestPoint() {

		LinkedList<Vector2df> otherVectors = new LinkedList<Vector2df>();

		otherVectors.add(vector);
		assertTrue(Vector2df.findShortestDistance(otherVectors, vector) == vector);
		assertTrue(Vector2df.findShortestDistance(otherVectors, new Vector2df(0,
				0)) == vector);

		Vector2df closeVector = new Vector2df(101, 100);
		Vector2df anotherCloseVector = new Vector2df(100, 101);

		otherVectors.clear();
		otherVectors.add(closeVector);
		otherVectors.add(anotherCloseVector);

		assertTrue(Vector2df.findShortestDistance(otherVectors, vector) == closeVector);

	}

	/**
	 * Executed after each test method.
	 */
	@After
	public void deSetUp() {
		vector = null;
	}
}
