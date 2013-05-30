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
		super();
		Vector2D vertices[] = { new Vector2D(-20, -40), new Vector2D(0, -60),
				new Vector2D(20, -40), new Vector2D(20, 0),
				new Vector2D(-20, 0) };
		setVertices(vertices);
	}
}
