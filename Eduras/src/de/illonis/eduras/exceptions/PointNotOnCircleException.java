package de.illonis.eduras.exceptions;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.math.Vector2df;

/**
 * Thrown when the given point is not located on the given circle.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PointNotOnCircleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Circle circle;
	private final Vector2df point;

	/**
	 * Create a PointNotOnCircleException.
	 * 
	 * @param c
	 *            Circle
	 * @param p
	 *            Point
	 */
	public PointNotOnCircleException(Circle c, Vector2df p) {
		circle = c;
		point = p;
	}

	/**
	 * Returns the involved circle.
	 * 
	 * @return The circle
	 */
	public Circle getCircle() {
		return circle;
	}

	/**
	 * Returns the involved point.
	 * 
	 * @return the point
	 */
	public Vector2df getPoint() {
		return point;
	}

}
