package de.illonis.eduras.math;

/**
 * A math class that provides useful functions relating basic math.
 * 
 * @author illonis
 * 
 */
public class BasicMath {

	/**
	 * Returns square of given double number.
	 * 
	 * @see BasicMath#square(int)
	 * 
	 * @param x
	 *            number
	 * @return square of x
	 */
	public static double square(double x) {
		return x * x;
	}

	/**
	 * Returns square of given integer number.
	 * 
	 * @see square
	 * 
	 * @param x
	 *            number
	 * @return square of x
	 */
	public static int square(int x) {
		return x * x;
	}
}
