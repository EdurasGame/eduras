//package de.illonis.eduras.math;
//
//import org.newdawn.slick.geom.Vector2f;
//
///**
// * Represents a two-dimensional line. See <a href=
// * "http://www.java-forum.org/spiele-multimedia-programmierung/6588-einiges-geometrie-punkte-vektoren-geraden.html"
// * >this url</a> for detailed information about the mathematics behind the
// * methods.
// * 
// * @author Florian Mai <florian.ren.mai@googlemail.com>
// * 
// */
//public class Line {
//
//	static final double RANGE = 0.00001;
//
//	private final double a;
//	private final double b;
//	private final double c;
//
//	private final Vector2f u;
//	private final Vector2f v;
//
//	/**
//	 * Creates a line that goes through u and v.
//	 * 
//	 * @param u
//	 * @param v
//	 */
//	public Line(Vector2f u, Vector2f v) {
//
//		this.u = u;
//		this.v = v;
//
//		double dx = u.getX() - v.getX();
//		double dy = u.getY() - v.getY();
//
//		a = dy;
//		b = -dx;
//
//		c = -(a * u.getX() + b * u.getY());
//
//	}
//
//	/**
//	 * Checks if a the line contains the given point.
//	 * 
//	 * @param point
//	 *            The point to check.
//	 * @return True if line contains point, false otherwise.
//	 */
//	public boolean containsPoint(Vector2f point) {
//		return Math.abs(a * point.getX() + b * point.getY() + c) < RANGE;
//	}
//
//	/**
//	 * Returns a position vector to the first point the line was deduced from.
//	 * 
//	 * @return A position vector to the first point.
//	 */
//	public Vector2f getU() {
//		return u;
//	}
//
//	/**
//	 * Returns a position vector to the first point the line was deduced from.
//	 * 
//	 * @return A position vector to the second point.
//	 */
//	public Vector2f getV() {
//		return v;
//	}
//
//	/**
//	 * Returns a directional vector of the line.
//	 * 
//	 * @return The directional vector.
//	 * 
//	 *         In detail, it returns the difference vector between the two
//	 *         vectors of which this line was made up. So if you call getU() on
//	 *         this line and add the directional vector to u, you will gain v.
//	 */
//	public Vector2f getDirectionalVector() {
//		Vector2f vec = new Vector2f(v);
//		vec.sub(u);
//		return vec;
//	}
//
//	/**
//	 * Returns the support vector of this line.
//	 * 
//	 * @return You will get a copy of the vector you gain calling getU().
//	 */
//	public Vector2f getSupportVector() {
//		Vector2f vec = new Vector2df(u);
//		return vec;
//	}
//
//	/**
//	 * This function returns the point you will get by multiplying the given
//	 * lambda with the directional vector 'uv' (see
//	 * {@link #getDirectionalVector()}) and add it to the support vector 'u'.
//	 * 
//	 * @param lambda
//	 *            The multiplier.
//	 * @return Returns u + uv * lambda
//	 */
//	public Vector2f getPointAt(float lambda) {
//
//		Vector2f resultPoint = new Vector2f(getU());
//
//		Vector2f temp = new Vector2f(getDirectionalVector());
//		temp.scale(lambda);
//
//		resultPoint.add(temp);
//
//		return resultPoint;
//	}
//
//	/**
//	 * Compares the line with another for equality.
//	 * 
//	 * @param otherLine
//	 *            The line to compare with this line.
//	 * @return Returns true, if the two vectors the lines are made of are equal
//	 *         pairwisely.
//	 */
//	public boolean equals(Line otherLine) {
//		return u.equals(otherLine.getU()) && v.equals(otherLine.getV());
//	}
//
// }
