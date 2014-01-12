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

	/**
	 * Tests {@link BasicMath}.isInBetweenModulo().
	 */
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

	/**
	 * Tests {@link BasicMath}.findClosestNumberModulo.
	 */
	@Test
	public void testClosestNumberModulo() {
		assertTrue(BasicMath.findClosestNumberModuloArray(6.,
				new double[] { 1. }, 2) == 1);
		assertTrue(BasicMath.findClosestNumberModuloArray(6., new double[] {
				0.9, 4 }, 7) == 0.9);
		assertTrue(BasicMath.findClosestNumberModuloArray(0, new double[] { 0,
				0, 42 }, 42) == 0);
		assertTrue(BasicMath.findClosestNumberModuloArray(3.5, new double[] {
				3.4, 3.61 }, 2) == 1.4);
	}
}
