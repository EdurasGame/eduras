/**
 * 
 */
package de.illonis.eduras.math;

import java.util.LinkedList;

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
		return a * point.getX() + b * point.getY() + c == 0;
	}

	/**
	 * Returns all the lines between a shape's corner points and its counterpart
	 * of a second shape, that is a shifted copy of the first shape.
	 * 
	 * @param vertices
	 *            The relative vertices of the shape.
	 * @param source
	 *            The middle point of the shape.
	 * @param destination
	 *            The middle point of the shapes copy.
	 * @return Returns a list containing the resulting lines.
	 */
	public static LinkedList<Line> getLinesBetweenShapePositions(
			Vector2D[] vertices, Vector2D source, Vector2D destination) {

		LinkedList<Line> lines = new LinkedList<Line>();

		for (int i = 0; i < vertices.length; i++) {

			Vector2D sourcePoint = new Vector2D(source);
			sourcePoint.add(vertices[i]);

			Vector2D destPoint = new Vector2D(destination);
			destPoint.add(vertices[i]);

			lines.add(new Line(sourcePoint, destPoint));
		}

		return lines;
	}

	/**
	 * Returns a position vector to the first point the line was deduced from.
	 * @return A position vector to the first point.
	 */
	public Vector2D getU() {
		return u;
	}

	/**
	 * Returns a position vector to the first point the line was deduced from.
	 * @return A position vector to the second point.
	 */
	public Vector2D getV() {
		return v;
	}
	
	

}
