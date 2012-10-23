package de.illonis.eduras.math;

/**
 * A math class that provides useful functions that are used in two-dimensional
 * space.
 * 
 * @author illonis
 * 
 */
public class Geometry {

	/**
	 * Returns square of given double number.
	 * 
	 * @see #square(int)
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
	 * @see #square(double)
	 * 
	 * @param x
	 *            number
	 * @return square of x
	 */
	public static int square(int x) {
		return x * x;
	}

	/**
	 * Return length of a hypotenuse in a right-angled triangle.
	 * 
	 * @param a
	 *            leg a
	 * @param b
	 *            leg b
	 * @return length of hypotenuse
	 */
	public static double getHypotenuseLength(double a, double b) {
		return Math.sqrt(square(a) + square(b));
	}

}
