package de.illonis.eduras.shapecreator.gui;

import de.illonis.eduras.shapecreator.Vertice;

/**
 * Represents a row in the vertice table.
 * 
 * @author illonis
 * 
 */
public class TableRecord {
	private Vertice vertice;

	/**
	 * Creates a new record that holds given vertice.
	 * 
	 * @param vertice
	 *            the vertice for this row.
	 */
	public TableRecord(Vertice vertice) {
		this.vertice = vertice;
	}

	/**
	 * Returns the x-component of the hold vertice.
	 * 
	 * @return vertice's x-coordinate.
	 */
	public double getX() {
		return vertice.getX();
	}

	/**
	 * Returns the y-component of the hold vertice.
	 * 
	 * @return vertice's y-coordinate.
	 */
	public double getY() {
		return vertice.getY();
	}

	/**
	 * Sets the x-coordinate of the vertice.
	 * 
	 * @param x
	 *            the new x-coordinate.
	 */
	public void setX(double x) {
		vertice.setX(x);
	}

	/**
	 * Sets the y-coordinate of the vertice.
	 * 
	 * @param y
	 *            the new y-coordinate.
	 */
	public void setY(double y) {
		vertice.setY(y);
	}

	/**
	 * Returns the vertice of this row.
	 * 
	 * @return the vertices of this row.
	 */
	public Vertice getVertice() {
		return vertice;
	}
}
