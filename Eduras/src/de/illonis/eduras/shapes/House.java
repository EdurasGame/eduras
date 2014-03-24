package de.illonis.eduras.shapes;

import de.illonis.eduras.math.Vector2df;

/**
 * A basic house.
 * 
 * @author illonis
 * 
 */
public class House extends Polygon {

	/**
	 * Creates a basic house shape.
	 */
	public House() {
		// FIXME: change coordinates.
		Vector2df vertices[] = { new Vector2df(0, 20), new Vector2df(20, 0),
				new Vector2df(40, 20), new Vector2df(40, 60),
				new Vector2df(0, 60) };
		setVector2dfs(vertices);
	}
}
