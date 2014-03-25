package de.illonis.eduras.shapes;

import org.newdawn.slick.geom.Polygon;

/**
 * A sword for close combat.
 * 
 * @author jme
 * 
 */
public class Sword extends Polygon {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new sword.
	 */
	public Sword() {
		addPoint(-1, -11);
		addPoint(0, -14);
		addPoint(1, -11);
		addPoint(1, -3);
		addPoint(3, -3);
		addPoint(-1, 0);
		addPoint(-1, -2);
		addPoint(-3, -2);
		addPoint(-3, -3);
		addPoint(-1, -3);
	}
}
