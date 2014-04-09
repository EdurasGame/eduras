package de.illonis.eduras.shapecreator.templates;

import java.util.LinkedList;

import de.illonis.eduras.math.Vector2df;

/**
 * A template for editable shapes.
 * 
 * @author illonis
 * 
 */
public abstract class ShapeTemplate {
	private final LinkedList<Vector2df> vertices;

	protected ShapeTemplate() {
		vertices = new LinkedList<Vector2df>();
	}

	/**
	 * @return name of the template.
	 */
	public abstract String getName();

	protected final void addVector2df(Vector2df v) {
		vertices.add(v);
	}

	protected final void addVector2df(float x, float y) {
		vertices.add(new Vector2df(x, y));
	}

	final LinkedList<Vector2df> getDefaultVector2dfs() {
		return vertices;
	}

	@Override
	public String toString() {
		return getName();
	}

}
