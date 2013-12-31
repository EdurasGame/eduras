package de.illonis.eduras.math;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * Provides a basic class for two-dimensional-vectors that supports several
 * mathematical and geometric operations.
 * 
 * @author illonis
 * 
 */
public class Vector2D {

	private double x;
	private double y;

	/**
	 * Creates an empty vector (0,0) ("Nullvector").
	 * 
	 * @see #Vector2D(double, double)
	 * @see #isNull()
	 */
	public Vector2D() {
		this(0, 0);
	}

	/**
	 * Creates a new vector that is an independend copy of given vector.
	 * 
	 * @param vector
	 *            vector to copy.
	 */
	public Vector2D(Vector2D vector) {
		this(vector.getX(), vector.getY());
	}

	/**
	 * Creates a new vector based on given point's coordinates. Vector will not
	 * change when point is changed later on.
	 * 
	 * @param p
	 *            point that's coordinates should be used.
	 */
	public Vector2D(Point p) {
		this(p.x, p.y);
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
	 * Modifies X-Component by given value.
	 * 
	 * @param xDiff
	 *            modifier.
	 */
	public void modifyX(double xDiff) {
		this.x += xDiff;
	}

	/**
	 * Modifies Y-Component by given value.
	 * 
	 * @param yDiff
	 *            modifier.
	 */
	public void modifyY(double yDiff) {
		this.y += yDiff;
	}

	/**
	 * Sets X- and Y-components to a new value. This will have the same result
	 * as if X and Y were set individually.
	 * 
	 * @param newX
	 *            new X-value
	 * @param newY
	 *            new Y-value
	 */
	public void update(double newX, double newY) {
		setX(newX);
		setY(newY);
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
	 * Inverts this vector by multiplying all values with -1.
	 */
	public void invert() {
		setX(-x);
		setY(-y);
	}

	/**
	 * Rotates this vector by given angle.
	 * 
	 * @param degrees
	 *            angle in degrees.
	 */
	public void rotate(double degrees) {
		degrees = degrees % 360;

		if (degrees == 0)
			return;
		double oldX = this.x;
		double oldY = this.y;
		double radian = Geometry.toRadian(degrees);
		this.x = oldX * Math.cos(radian) - oldY * Math.sin(radian);
		this.y = oldX * Math.sin(radian) + oldY * Math.cos(radian);
	}

	/**
	 * Returns length of vector.
	 * 
	 * @return length of vector.
	 */
	public double getLength() {
		if (x == 0)
			return Math.abs(getY());
		if (y == 0)
			return Math.abs(getX());
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
	 * Returns a unitvector of this vector. Unit vectors have the same direction
	 * as their original, but lenght 1.
	 * 
	 * @return unitvector of vector
	 */
	public Vector2D getUnitVector() {
		Vector2D unit = this.copy();
		unit.setLength(1);
		return unit;
	}

	/**
	 * Recalculates vector by scaling it to match given length keeping aspect
	 * ratio. This does not have any effect when this is a nullvector.
	 * 
	 * @param newLength
	 *            new length.
	 */
	public void setLength(double newLength) {
		if (isNull())
			return;
		if (x == 0) {
			setY((y > 0) ? newLength : -newLength);
		} else if (y == 0) {
			setX((x > 0) ? newLength : -newLength);
		} else {
			double factor = newLength / getLength();
			mult(factor);
		}
	}

	/**
	 * Modifies length by given value.
	 * 
	 * @see #setLength(double)
	 * 
	 * @param diff
	 *            difference.
	 */
	public void modifyLength(double diff) {
		setLength(getLength() + diff);
	}

	/**
	 * Checks whether vector is a nullvector.
	 * 
	 * @return true if both, x and y, are 0.
	 */
	public boolean isNull() {
		return (x == 0 && y == 0);
	}

	@Override
	public String toString() {
		return "Vector2D(" + x + ", " + y + ")";
	}

	/**
	 * Assumes the calling and the passed vector to be position vectors and
	 * calculates the distance between the points behind.
	 * 
	 * @param vec
	 *            target vector.
	 * 
	 * @return Returns the distance between the points represented by the
	 *         vectors.
	 */
	public double calculateDistance(Vector2D vec) {
		Vector2D copyThis = new Vector2D(this);

		copyThis.subtract(vec);

		return copyThis.getLength();
	}

	/**
	 * Subtracts the given vector from this vector. This is done by adding the
	 * inverse of given vector.
	 * 
	 * @see #add(Vector2D)
	 * 
	 * @param vec
	 *            The vector to subtract.
	 */
	public void subtract(Vector2D vec) {
		Vector2D copyVec = new Vector2D(vec);
		copyVec.invert();
		this.add(copyVec);
	}

	/**
	 * Calculates which of the points given in the list is closest to the given
	 * position vector.
	 * 
	 * @param points
	 *            The list of points.
	 * @param positionVector
	 *            The position vector.
	 * @return Returns the vector in the list that is closest to the given
	 *         position vector. If there are multiple closest vectors, the first
	 *         in the list is chosen.
	 */
	public static Vector2D findShortestDistance(LinkedList<Vector2D> points,
			Vector2D positionVector) {

		if (points.size() > 0) {
			Vector2D nearestPoint = points.getFirst();
			double shortestDistance = nearestPoint
					.calculateDistance(positionVector);

			for (Vector2D point : points) {
				double distance = point.calculateDistance(positionVector);
				if (distance < shortestDistance) {
					nearestPoint = point;
					shortestDistance = distance;
				}
			}

			return nearestPoint;
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2D) {
			return equals((Vector2D) obj);
		}
		return super.equals(obj);
	}

	/**
	 * Checks if this vector equals another vector. Two vectors are identical if
	 * they have the same x and y values.
	 * 
	 * @param vec
	 *            vector to compare to.
	 * @return true if vectors are equal, false otherwise.
	 */
	public boolean equals(Vector2D vec) {
		return (Math.abs(this.getX() - vec.getX()) < Line.RANGE && Math
				.abs(this.getY() - vec.getY()) < Line.RANGE);
	}

	/**
	 * Returns a point with same x and y values as vector. This can be used to
	 * solve geometric problems with Java's builtin methods.
	 * 
	 * @see Rectangle#contains(Point2D)
	 * @return copy of vector as point.
	 */
	public Point2D.Double toPoint() {
		return new Point2D.Double(x, y);
	}

	/**
	 * Calculates whether this Vector is linearly depending on the given vector.
	 * 
	 * @param vec
	 *            second vector.
	 * 
	 * @return true if vectors are linearly depending.
	 */
	public boolean isLinearTo(Vector2D vec) {

		if (getX() == 0) {
			return vec.getX() == 0;
		}

		if (getY() == 0) {
			return vec.getY() == 0;
		}

		// reaching this case X and Y of this vector cannot be 0 anymore.
		if (vec.getX() == 0 || vec.getY() == 0) {
			return false;
		}

		// so now there no component of any vector can be zero.

		double r = vec.getX() / this.getX();

		return this.getY() * r == vec.getY();
	}

	/**
	 * Calculates the angle between this vector and the given other vector.
	 * 
	 * @param other
	 *            The other vector.
	 * @return The angle in degrees.
	 * @throws IllegalArgumentException
	 *             Thrown if either of the two vectors is a null-vector.
	 */
	public double getAngleBetween(Vector2D other)
			throws IllegalArgumentException {
		if (this.isNull() || other.isNull()) {
			throw new IllegalArgumentException(toString() + " or "
					+ other.toString() + " is a null-vector.");
		}
		double dotProduct = x * other.x + y * other.y;
		double myLength = getLength();
		double othersLength = other.getLength();
		double cosOfAngle = dotProduct / (myLength * othersLength);
		return Math.cosh(cosOfAngle);
	}

	/**
	 * Calculates this vectors angle to the x-axis.
	 * 
	 * @return Returns the angle.
	 * @throws IllegalArgumentException
	 *             if this vector is a nullvector.
	 */
	public double getAngleToXAxis() throws IllegalArgumentException {
		return getAngleBetween(new Vector2D(1, 0));
	}
}
