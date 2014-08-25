package de.illonis.eduras.shapecreator.templates;

import java.util.LinkedList;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.math.Vector2df;

/**
 * A template for editable shapes.
 * 
 * @author illonis
 * 
 */
public abstract class ShapeTemplate {
	private final LinkedList<Vector2f> vertices;

	protected ShapeTemplate() {
		vertices = new LinkedList<Vector2f>();
	}

	/**
	 * @return name of the template.
	 */
	public abstract String getName();

	protected final void addVector2df(Vector2f v) {
		vertices.add(v);
	}

	protected final void addVector2df(float x, float y) {
		vertices.add(new Vector2df(x, y));
	}

	public final LinkedList<Vector2f> getDefaultVector2dfs() {
		return vertices;
	}

	@Override
	public String toString() {
		return getName();
	}

}
