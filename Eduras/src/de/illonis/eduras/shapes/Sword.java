package de.illonis.eduras.shapes;

import de.illonis.eduras.math.Vector2D;

public class Sword extends Polygon {
	public Sword() {
		super();
		Vector2D vertices[] = { new Vector2D(-4, -14), new Vector2D(0, -18),
				new Vector2D(4, -14), new Vector2D(4, 0), new Vector2D(14, 0) };
		setVertices(vertices);
	}
}
