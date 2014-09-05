package de.illonis.eduras.unittests;

import static org.junit.Assert.assertEquals;

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
	 * Tests the {@link de.illonis.eduras.math.Vector2df#rotate(float) rotate()}
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
		Vector2df expected = new Vector2df(-100, 100);
		assertEquals(expected.x, copy.x, 0.0001f);
		assertEquals(expected.y, copy.y, 0.0001f);
		copy.rotate(90);
		Vector2df expected2 = new Vector2df(-100, -100);
		assertEquals(expected2.x, copy.x, 0.0001f);
		assertEquals(expected2.y, copy.y, 0.0001f);
		copy.rotate(90);
		Vector2df check = new Vector2df(100, -100);
		assertEquals(check.x, copy.x, 0.0001f);
		assertEquals(check.y, copy.y, 0.0001f);
		copy.rotate(90);
		assertEquals(vector.x, copy.x, 0.0001f);
		assertEquals(vector.y, copy.y, 0.0001f);
		copy.rotate(-90);
		assertEquals(check.x, copy.x, 0.0001f);
		assertEquals(check.y, copy.y, 0.0001f);
	}

	/**
	 * Executed after each test method.
	 */
	@After
	public void deSetUp() {
		vector = null;
	}
}
