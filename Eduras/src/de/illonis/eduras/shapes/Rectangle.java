/**
 * 
 */
package de.illonis.eduras.shapes;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.math.Vector2D;

/**
 * Represents a rectangle.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Rectangle extends Polygon {

	/**
	 * Creates a rectangle made of the given point at the top left and the point
	 * at bottom right.
	 * 
	 * @param topLeft
	 *            The point at top left.
	 * @param bottomRight
	 *            The point at bottom right.
	 * @throws ShapeVerticesNotApplicableException
	 *             Thrown if the topleft point is not the topleft point or the
	 *             bottom right point is not the bottom right point or if the
	 *             origin (0,0) does not lie inside of the rectangle.
	 */
	public Rectangle(Vector2D topLeft, Vector2D bottomRight)
			throws ShapeVerticesNotApplicableException {
		if (topLeft.getX() > 0 || topLeft.getY() < 0 || bottomRight.getX() < 0
				|| bottomRight.getY() > 0) {
			throw new ShapeVerticesNotApplicableException();
		} else {
			Vector2D[] vertices = new Vector2D[4];
			vertices[0] = topLeft;
			vertices[1] = new Vector2D(bottomRight.getX(), topLeft.getY());
			vertices[3] = new Vector2D(topLeft.getX(), bottomRight.getY());
			vertices[2] = bottomRight;
			setVertices(vertices);
		}
	}

}