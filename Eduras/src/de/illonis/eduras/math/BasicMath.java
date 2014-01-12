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
		double min = values[0];
		for (int i = 1; i < values.length; i++) {
			min = Math.min(values[i], min);
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
		double max = values[0];
		for (int i = 1; i < values.length; i++) {
			max = Math.max(values[i], max);
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

	/**
	 * Implements the REAL modulo a mod b.
	 * 
	 * @param a
	 * @param b
	 * @return a mod b
	 */
	public static double calcModulo(double a, double b) {
		double r = (a % b);
		if (r < 0) {
			r += b;
		}
		return r;
	}

	/**
	 * Tells whether b is in between a and c for a natural number n. We say a <
	 * b < c if ((b - a) mod n) + ((c - b) mod n) <= (c - a) mod n. Unlike how %
	 * is implemented in Java, for us -1 % 2 == 1 instead of -1 % 2 == -1.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param n
	 * @return Returns true if a < b < c.
	 */
	public static boolean isInBetweenModulo(double a, double b, double c, int n) {
		return calcModulo(b - a, n) + calcModulo(c - b, n) <= calcModulo(c - a,
				n);
	}

	/**
	 * Returns a where (a - x) mod n= min{ (b - x) mod n | b in angles}, that
	 * is, it returns the number in nums that is closest to x regarding mod n.
	 * 
	 * @param x
	 * @param nums
	 * @param n
	 * @return a where (a - x) mod n= min{ (b - x) mod n | b in angles}
	 */
	public static double findClosestNumberModulo(double x, double[] nums, int n) {
		double currClosest = nums[0];
		double currClosestDistance = calcModulo(nums[0] - x, n);
		for (int i = 1; i < nums.length; i++) {
			double currDistance = calcModulo(nums[i] - x, n);
			if (currDistance < currClosestDistance) {
				currClosestDistance = currDistance;
				currClosest = nums[i];
			}
		}
		return currClosest;
	}
}
