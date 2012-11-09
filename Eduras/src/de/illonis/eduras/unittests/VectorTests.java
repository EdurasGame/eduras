/**
 * 
 */
package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

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

	@After
	public void deSetUp() {
		vector = null;
	}
}
