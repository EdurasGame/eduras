/**
 * 
 */
package de.illonis.eduras.shapes;

import de.illonis.eduras.exceptions.ShapeVector2dfsNotApplicableException;
import de.illonis.eduras.math.Vector2df;

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
	 * @throws ShapeVector2dfsNotApplicableException
	 *             Is thrown if the given vertices do not enclose the objects
	 *             centre point.
	 */
	public Triangle(Vector2df first, Vector2df second, Vector2df third)
			throws ShapeVector2dfsNotApplicableException {

		boolean isPositiveX = first.getX() > 0 || second.getX() > 0
				|| third.getX() > 0;
		boolean isNegativeX = first.getX() < 0 || second.getX() < 0
				|| third.getX() < 0;
		boolean isPositiveY = first.getY() > 0 || second.getY() > 0
				|| third.getY() > 0;
		boolean isNegativeY = first.getY() < 0 || second.getY() < 0
				|| third.getY() < 0;

		if (isPositiveX && isNegativeX && isPositiveY && isNegativeY) {
			Vector2df[] vertices = { first, second, third };
			setVector2dfs(vertices);
		} else {
			throw new ShapeVector2dfsNotApplicableException();
		}
	}
}
