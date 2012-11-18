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

	/**
	 * Sums up given integer values.
	 * 
	 * @see #sum(double...)
	 * 
	 * @param values
	 *            values to sum up.
	 * @return sum of given values.
	 */
	public static int sum(int... values) {
		int result = 0;
		for (int i : values) {
			result += i;
		}
		return result;
	}

	/**
	 * Sums up given double values.
	 * 
	 * @see #sum(int...)
	 * 
	 * @param values
	 *            values to sum up.
	 * @return sum of given values.
	 */
	public static double sum(double... values) {
		double result = 0;
		for (double i : values) {
			result += i;
		}
		return result;
	}
}
