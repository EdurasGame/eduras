package de.illonis.eduras.shapes;

import de.illonis.eduras.math.Vector2D;

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
		Vector2D vertices[] = { new Vector2D(0, 20), new Vector2D(20, 0),
				new Vector2D(40, 20), new Vector2D(40, 60),
				new Vector2D(0, 60) };
		setVertices(vertices);
	}
}
