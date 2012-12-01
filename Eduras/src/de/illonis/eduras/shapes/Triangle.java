/**
 * 
 */
package de.illonis.eduras.shapes;

import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Triangle extends ObjectShape {

	Vector2D[] vertices;

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
			vertices = new Vector2D[3];
			vertices[0] = first;
			vertices[1] = second;
			vertices[2] = third;
		} else {
			throw new ShapeVerticesNotApplicableException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.ObjectShape#checkCollision(de.illonis.eduras.Game,
	 * de.illonis.eduras.GameObject, java.awt.geom.Point2D.Double)
	 */
	@Override
	public Vector2D checkCollision(GameInformation game, GameObject thisObject,
			Vector2D target) {

		return checkPolygonCollision(game, thisObject, target);
	}

	/**
	 * Returns the vertices of the triangle.
	 * 
	 * @return The vertices.
	 */
	@Override
	public LinkedList<Vector2D> getVertices() {
		LinkedList<Vector2D> res = new LinkedList<Vector2D>();

		res.add(vertices[0]);
		res.add(vertices[1]);
		res.add(vertices[2]);

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.shapes.ObjectShape#getBorderLines()
	 */
	@Override
	public LinkedList<Line> getBorderLines(GameObject object) {
		return Geometry.getRelativeBorderLines(getAbsoluteVertices(object)
				.toArray(new Vector2D[getAbsoluteVertices(object).size()]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.shapes.ObjectShape#getVerticesAsArray()
	 */
	@Override
	public Vector2D[] getVerticesAsArray() {
		return vertices;
	}

}
