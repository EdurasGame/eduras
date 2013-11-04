package de.illonis.eduras.shapes;

import de.illonis.eduras.math.Vector2D;

/**
 * A sword for close combat.
 * 
 * @author jme
 * 
 */
public class Sword extends Polygon {

	/**
	 * Creates a new sword.
	 */
	public Sword() {
		super();
		Vector2D vertices[] = { new Vector2D(-1, -11), new Vector2D(0, -14),
				new Vector2D(1, -11), new Vector2D(1, -3), new Vector2D(3, -3),
				new Vector2D(3, -2), new Vector2D(1, -2), new Vector2D(1, 0),
				new Vector2D(-1, 0), new Vector2D(-1, -2),
				new Vector2D(-3, -2), new Vector2D(-3, -3),
				new Vector2D(-1, -3) };
		setVertices(vertices);
	}
}
