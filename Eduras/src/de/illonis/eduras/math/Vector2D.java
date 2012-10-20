package de.illonis.eduras.math;

/**
 * Provides a basic class for two-dimensional-vectors
 * 
 * @author illonis
 * 
 */
public class Vector2D {

	private double x;
	private double y;

	/**
	 * Creates an empty vector (0,0).
	 * 
	 * @see #Vector2D(double, double)
	 */
	public Vector2D() {
		this(0, 0);
	}

	/**
	 * Creates a new vector that is a copy of given vector.
	 * 
	 * @param vector
	 *            vector to copy
	 */
	public Vector2D(Vector2D vector) {
		this(vector.getX(), vector.getY());
	}

	/**
	 * Creates a new vector with given size.
	 * 
	 * @see #Vector2D()
	 * 
	 * @param x
	 *            x-dimension
	 * @param y
	 *            y-dimension
	 */
	public Vector2D(double x, double y) {
		update(x, y);
	}

	/**
	 * Returns X-component of vector.
	 * 
	 * @return X-component of vector.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets X-Component to a new value.
	 * 
	 * @param x
	 *            new X-value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Returns Y-component of vector.
	 * 
	 * @return Y-component of vector.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets Y-Component to a new value.
	 * 
	 * @param y
	 *            new Y-value
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets X- and Y-components to a new value. This will have the same result
	 * as if X and Y were set individually.
	 * 
	 * @param x
	 *            new X-value
	 * @param y
	 *            new Y-value
	 */
	public void update(double x, double y) {
		setX(x);
		setY(y);
	}

	/**
	 * Multiples vector with given integer factor.
	 * 
	 * @param multipler
	 *            factor
	 */
	public void mult(int multipler) {
		setX(x * multipler);
		setY(y * multipler);
	}

	/**
	 * Multiples vector with given double factor.
	 * 
	 * @param multipler
	 *            factor
	 */
	public void mult(double multipler) {
		setX(x * multipler);
		setY(y * multipler);
	}

	/**
	 * Adds a vector to this vector. Vector addition is done by adding
	 * X-compontents and Y-components individually.
	 * 
	 * @param vector
	 *            vector to add
	 */
	public void add(Vector2D vector) {
		setX(x + vector.getX());
		setY(y + vector.getY());
	}

	/**
	 * Inverts this vector bi multiplying all values with -1.
	 */
	public void invert() {
		setX(-x);
		setY(-y);
	}

	/**
	 * Returns length of vector.
	 * 
	 * @return length of vector
	 */
	public double getLength() {
		if (x == 0)
			return getY();
		if (y == 0)
			return getX();
		return Geometry.getHypotenuseLength(x, y);
	}

	/**
	 * Returns a copy of this vector with no references.
	 * 
	 * @return copy of this vector.
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	/**
	 * Returns a unitvector of this vector
	 * 
	 * @return unitvector of vector
	 */
	public Vector2D getUnitVector() {
		final double len = getLength();
		return new Vector2D(x / len, y / len);
	}

	/**
	 * Recalculates vector by scaling it to match given length keeping aspect
	 * ratio.
	 * 
	 * @param newLength
	 *            new length.
	 */
	public void setLength(double newLength) {
		if (x == 0) {
			setY(newLength);
		} else if (y == 0) {
			setX(newLength);
		} else {
			final double length = getLength();
			double factor = newLength / length;
			mult(factor);
			System.out.println(length + " " + getLength() + " " + newLength);
		}
	}

	/**
	 * Checks whether vector is a nullvector.
	 * 
	 * @return true if both, x and y, are 0.
	 */
	public boolean isNull() {
		return (x == 0 && y == 0);
	}
}