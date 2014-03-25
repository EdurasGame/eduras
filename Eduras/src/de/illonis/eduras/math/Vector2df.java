package de.illonis.eduras.math;

import java.awt.Point;

import org.newdawn.slick.geom.Vector2f;

/**
 * A twodimensional vector.
 * 
 * @author illonis
 * 
 */
public strictfp class Vector2df extends Vector2f {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new vector.
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 */
	public Vector2df(float x, float y) {
		super(x, y);
	}

	/**
	 * Creates a copy of given vector.
	 * 
	 * @param v
	 *            the source.
	 */
	public Vector2df(Vector2f v) {
		this(v.x, v.y);
	}

	/**
	 * @param v
	 *            array with x and y coordinate.
	 */
	public Vector2df(float[] v) {
		this(v[0], v[1]);
	}

	/**
	 * Creates a new vector with (0,0).
	 */
	public Vector2df() {
		this(0, 0);
	}

	/**
	 * Creates a vector using given point's coordinates.
	 * 
	 * @param p
	 *            the point.
	 */
	public Vector2df(Point p) {
		this(p.x, p.y);
	}

	@Override
	public strictfp Vector2df copy() {
		return new Vector2df(x, y);
	}

	/**
	 * Rotates this vector by given angle.
	 * 
	 * @param degrees
	 *            angle in degrees.
	 */
	public void rotate(float degrees) {
		degrees = BasicMath.calcModulo(degrees, 360);

		if (degrees == 0)
			return;
		float oldX = this.x;
		float oldY = this.y;
		float radian = Geometry.toRadian(degrees);
		this.x = oldX * (float) Math.cos(radian) - oldY
				* (float) Math.sin(radian);
		this.y = oldX * (float) Math.sin(radian) + oldY
				* (float) Math.cos(radian);
	}

	/**
	 * Rotates this vector by given angle using given point as rotation-center.
	 * 
	 * @param degrees
	 *            the angle in degrees.
	 * @param center
	 *            the center point.
	 */
	public void rotate(float degrees, Vector2df center) {
		if (center.x == 0 && center.y == 0) {
			rotate(degrees);
			return;
		}
		degrees = BasicMath.calcModulo(degrees, 360);
		if (degrees == 0)
			return;
		float oldX = this.x;
		float oldY = this.y;
		float radian = Geometry.toRadian(degrees);
		this.x = center.x + (float) Math.cos(radian) * (oldX - center.x)
				- (float) Math.sin(radian) * (oldY - center.getY());
		this.y = center.y + (float) Math.sin(radian) * (oldX - center.x)
				+ (float) Math.cos(radian) * (oldY - center.y);
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
	public float getAngleBetween(Vector2df other)
			throws IllegalArgumentException {
		if (this.isNull() || other.isNull()) {
			throw new IllegalArgumentException(toString() + " or "
					+ other.toString() + " is a null-vector.");
		}
		float dotProduct = x * other.x + y * other.y;
		float myLength = length();
		float othersLength = other.length();
		float cosOfAngle = dotProduct / (myLength * othersLength);
		float angle = Geometry.toDegree((float) Math.acos(cosOfAngle));

		if (x * other.y + other.x * y > 0) {
			if (angle >= 180) {
				angle = 360 - angle;
			}
		} else {
			if (angle < 180) {
				angle = 360 - angle;
			}
		}

		return BasicMath.calcModulo(angle, 360);
	}

	/**
	 * @return true if both, x and y, are zero.
	 */
	public boolean isNull() {
		return (this.x == 0 && this.y == 0);
	}

	/**
	 * Calculates this vectors angle to the x-axis.
	 * 
	 * @return Returns the angle.
	 * @throws IllegalArgumentException
	 *             if this vector is a nullvector.
	 */
	public float getAngleToXAxis() throws IllegalArgumentException {
		return getAngleBetween(new Vector2df(1, 0));
	}
}
