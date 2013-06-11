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

	/**
	 * Returns minimum of any number of values.
	 * 
	 * @param values
	 *            input values.
	 * @return minimum.
	 */
	public static double min(Double... values) {
		if (values.length == 0)
			throw new NullPointerException(
					"Passed no value to min(double...) function. Must be at least one.");
		if (values.length == 1)
			return values[0];
		double min = 0;
		for (int i = 1; i < values.length; i++) {
			min = Math.min(values[i], values[i - 1]);
		}
		return min;
	}

	/**
	 * Returns maximum of given values.
	 * 
	 * @param values
	 *            input values.
	 * @return maximum.
	 */
	public static double max(Double... values) {
		if (values.length == 0)
			throw new NullPointerException(
					"Passed no value to max(double...) function. Must be at least one.");
		if (values.length == 1)
			return values[0];
		double max = 0;
		for (int i = 1; i < values.length; i++) {
			max = Math.max(values[i], values[i - 1]);
		}
		return max;
	}

	/**
	 * Calculates arithmetic average of two given numbers.
	 * 
	 * @param x
	 *            first value.
	 * @param y
	 *            second value.
	 * @return the average.
	 */
	public static double avg(double x, double y) {
		return (x + y) / 2;
	}
}
