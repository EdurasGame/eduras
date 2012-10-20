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
	 * Creates an empty vector (0,0)
	 */
	public Vector2D() {
		this(0, 0);
	}

	/**
	 * Creates a new vector that is a copy of given vector
	 * 
	 * @param vector
	 *            vector to copy
	 */
	public Vector2D(Vector2D vector) {
		this(vector.getX(), vector.getY());
	}

	/**
	 * Creates a new vector with given size
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
	 * Returns X-component of vector
	 * 
	 * @return X-component of vector
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
	 * Returns Y-component of vector
	 * 
	 * @return Y-component of vector
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
	 * as if X and Y are set individually
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
	 * Multiples vector with given integer factor
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
	 * Returns length of vector
	 * 
	 * @return length of vector
	 */
	public double getLength() {
		return Geometry.getHypotenuseLength(x, y);
	}

	/**
	 * Returns a unitvector of this vector
	 * 
	 * @return unitvector of vector
	 */
	public Vector2D getUnitVector() {
		return new Vector2D(x / getLength(), y / getLength());
	}
}
