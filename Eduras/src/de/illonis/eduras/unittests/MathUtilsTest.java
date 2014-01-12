package de.illonis.eduras.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.illonis.eduras.math.BasicMath;

/**
 * Tests some functions in {@link BasicMath}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MathUtilsTest {

	@Test
	public void testInBetweenModulo() {
		assertTrue(BasicMath.isInBetweenModulo(1, 2, 3, 4));
		assertTrue(BasicMath.isInBetweenModulo(3, 3, 2, 4));
		assertTrue(BasicMath.isInBetweenModulo(0.0001, 0.0002, 0.00021, 3));
		assertTrue(BasicMath.isInBetweenModulo(42, 23, 31, 44));
		assertTrue(BasicMath.isInBetweenModulo(0, 0, 0, 1));
		assertTrue(BasicMath.isInBetweenModulo(43, 2, 2.5, 40));

		assertFalse(BasicMath.isInBetweenModulo(0, 4, 3, 5));
		assertFalse(BasicMath.isInBetweenModulo(42.25, 2, 2.5, 40));
		assertFalse(BasicMath.isInBetweenModulo(1, 0, 1, 2));
		assertFalse(BasicMath.isInBetweenModulo(0, 3, 2.5, 4));
	}
}
