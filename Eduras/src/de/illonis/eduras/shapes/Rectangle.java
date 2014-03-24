package de.illonis.eduras.shapes;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.math.Vector2df;

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
	 *             Thrown if any point in the rectangle is negative on either x-
	 *             or y-value.
	 */
	public Rectangle(Vector2df topLeft, Vector2df bottomRight)
			throws ShapeVerticesNotApplicableException {
		if (topLeft.getX() < 0 || topLeft.getY() < 0 || bottomRight.getX() < 0
				|| bottomRight.getY() < 0) {
			throw new ShapeVerticesNotApplicableException();
		} else {
			// whether topLeft and bottomRight are switches does not matter
			Vector2df[] vertices = new Vector2df[4];
			vertices[0] = topLeft;
			vertices[1] = new Vector2df(topLeft.getX(), bottomRight.getY());
			vertices[2] = bottomRight;
			vertices[3] = new Vector2df(bottomRight.getX(), topLeft.getY());
			setVector2dfs(vertices);
		}
	}

	/**
	 * Creates a new rectangle with origin (0,0) and the given size.
	 * 
	 * @param size
	 *            the dimension of the rectangle.
	 * @throws ShapeVerticesNotApplicableException
	 *             if any of the dimension parameters is negative.
	 */
	public Rectangle(Vector2df size) throws ShapeVerticesNotApplicableException {
		this(new Vector2df(), size);
	}
}
