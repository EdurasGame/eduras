package de.illonis.eduras.shapecreator.templates;

import java.util.LinkedList;

import de.illonis.eduras.shapecreator.Vertice;

public abstract class ShapeTemplate {
	private final LinkedList<Vertice> vertices;

	protected ShapeTemplate() {
		vertices = new LinkedList<Vertice>();
	}

	public abstract String getName();

	protected final void addVertice(Vertice v) {
		vertices.add(v);
	}

	protected final void addVertice(double x, double y) {
		vertices.add(new Vertice(x, y));
	}

	final LinkedList<Vertice> getDefaultVertices() {
		return vertices;
	}

	@Override
	public String toString() {
		return getName();
	}

}
