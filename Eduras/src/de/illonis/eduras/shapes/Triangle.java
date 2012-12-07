/**
 * 
 */
package de.illonis.eduras.shapes;

import java.awt.geom.Rectangle2D;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.math.Vector2D;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Triangle extends Polygon {

	/**
	 * Creates a triangle with the 3 given vertices. The vertices are given as
	 * vectors relative to the center point of the object behind.
	 * 
	 * @param first
	 *            The first vertex' relative position.
	 * @param second
	 *            The second vertex relative position.
	 * @param third
	 *            The third vertex relative position.
	 * @throws ShapeVerticesNotApplicableException
	 *             Is thrown if the given vertices do not enclose the objects
	 *             centre point.
	 */
	public Triangle(Vector2D first, Vector2D second, Vector2D third)
			throws ShapeVerticesNotApplicableException {

		boolean isPositiveX = first.getX() > 0 || second.getX() > 0
				|| third.getX() > 0;
		boolean isNegativeX = first.getX() < 0 || second.getX() < 0
				|| third.getX() < 0;
		boolean isPositiveY = first.getY() > 0 || second.getY() > 0
				|| third.getY() > 0;
		boolean isNegativeY = first.getY() < 0 || second.getY() < 0
				|| third.getY() < 0;

		if (isPositiveX && isNegativeX && isPositiveY && isNegativeY) {
			Vector2D[] vertices = { first, second, third };
			setVertices(vertices);
		} else {
			throw new ShapeVerticesNotApplicableException();
		}
	}

	@Override
	public Rectangle2D.Double getBoundingBox() {
		// TODO: test
		Vector2D[] v = getVerticesAsArray();
		double x = BasicMath.min(v[0].getX(), v[1].getX(), v[2].getX());
		double y = BasicMath.min(v[0].getY(), v[1].getY(), v[2].getY());

		double xMax = BasicMath.max(v[0].getX(), v[1].getX(), v[2].getX());
		double yMax = BasicMath.max(v[0].getY(), v[1].getY(), v[2].getY());

		Rectangle2D.Double r = new Rectangle2D.Double(0, 0, Math.abs(x - xMax),
				Math.abs(y + yMax));
		return r;
	}
}
