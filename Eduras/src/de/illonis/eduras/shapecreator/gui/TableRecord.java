package de.illonis.eduras.shapecreator.gui;

import de.illonis.eduras.shapecreator.Vertice;

public class TableRecord {
	private Vertice vertice;

	public TableRecord(Vertice vertice) {
		this.vertice = vertice;
	}

	public double getX() {
		return vertice.getX();
	}

	public double getY() {
		return vertice.getY();
	}

	public void setX(double x) {
		vertice.setX(x);
	}

	public void setY(double y) {
		vertice.setY(y);
	}

	public Vertice getVertice() {
		return vertice;
	}
}
