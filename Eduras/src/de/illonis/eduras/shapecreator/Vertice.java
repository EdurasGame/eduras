package de.illonis.eduras.shapecreator;

import de.illonis.eduras.math.Vector2D;

/**
 * Represents a single edge of a polygon.
 * 
 * @author illonis
 * 
 */
public class Vertice extends Vector2D {

	/**
	 * Creates a new vertice with initial coordinates.
	 * 
	 * @param x
	 *            the x-coordinate.
	 * @param y
	 *            the y-coordinate.
	 */
	public Vertice(double x, double y) {
		super(x, y);
	}

	protected void moveTo(double newX, double newY) {
		update(newX, newY);
		DataHolder.getInstance().notifyVerticesChanged();
	}

	@Override
	public void mult(double multipler) {
		super.mult(multipler);
		DataHolder.getInstance().notifyVerticesChanged();
	}
}
