/**
 * 
 */
package de.illonis.eduras.math;

/**
 * Represents a two-dimensional line. See {@link http
 * ://www.java-forum.org/spiele
 * -multimedia-programmierung/6588-einiges-geometrie-
 * punkte-vektoren-geraden.html} for detailed information about the mathematics
 * behind the methods.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Line {

	public static final double RANGE = 0.00001;

	private final double a;
	private final double b;
	private final double c;

	private final Vector2D u;
	private final Vector2D v;

	/**
	 * Creates a line that goes through u and v.
	 * 
	 * @param u
	 * @param v
	 */
	public Line(Vector2D u, Vector2D v) {

		this.u = u;
		this.v = v;

		double dx = u.getX() - v.getX();
		double dy = u.getY() - v.getY();

		a = dy;
		b = -dx;

		c = -(a * u.getX() + b * u.getY());

	}

	/**
	 * Checks if a the line contains the given point.
	 * 
	 * @param point
	 *            The point to check.
	 * @return True if line contains point, false otherwise.
	 */
	public boolean containsPoint(Vector2D point) {
		System.out.println(a * point.getX() + b * point.getY() + c);
		return Math.abs(a * point.getX() + b * point.getY() + c) < RANGE;
	}

	/**
	 * Returns a position vector to the first point the line was deduced from.
	 * 
	 * @return A position vector to the first point.
	 */
	public Vector2D getU() {
		return u;
	}

	/**
	 * Returns a position vector to the first point the line was deduced from.
	 * 
	 * @return A position vector to the second point.
	 */
	public Vector2D getV() {
		return v;
	}

	/**
	 * Returns a directional vector of the line.
	 * 
	 * @return The directional vector.
	 * 
	 *         In detail, it returns the difference vector between the two
	 *         vectors of which this line was made up. So if you call getU() on
	 *         this line and add the directional vector to u, you will gain v.
	 */
	public Vector2D getDirectionalVector() {
		Vector2D vec = new Vector2D(v);
		vec.subtract(u);
		return vec;
	}

	/**
	 * Returns the support vector of this line.
	 * 
	 * @return You will get a copy of the vector you gain calling getU().
	 */
	public Vector2D getSupportVector() {
		Vector2D vec = new Vector2D(u);
		return vec;
	}

	/**
	 * Checks whether the given point is inside of the shape that is given by
	 * the lines. Note that in this context, a line is assumed to be a only a
	 * line segment but not an infinite line. The line segment is given from the
	 * u to v point, out of which the line was created
	 * 
	 * @return
	 */
	/*
	 * public static boolean checkInsideLines(LinkedList<Line> lines, Vector2D
	 * point) {
	 * 
	 * boolean result = false;
	 * 
	 * 
	 * 
	 * return result; }
	 */

	/**
	 * This function returns the point you will get by multiplying the given
	 * lambda with the directional vector 'uv' (see {@link
	 * getDirectionalVector()}) and add it to the support vector 'u'.
	 * 
	 * @param lambda
	 *            The multiplier.
	 * @return Returns u + uv * lambda
	 */
	public Vector2D getPointAt(double lambda) {

		Vector2D resultPoint = new Vector2D(getU());

		Vector2D temp = new Vector2D(getDirectionalVector());
		temp.mult(lambda);

		resultPoint.add(temp);

		return resultPoint;
	}

	/**
	 * Compares the line with another for equality.
	 * 
	 * @param otherLine
	 *            The line to compare with this line.
	 * @return Returns true, if the two vectors the lines are made of are equal
	 *         pairwisely.
	 */
	public boolean equals(Line otherLine) {
		return u.equals(otherLine.getU()) && v.equals(otherLine.getV());
	}

}
