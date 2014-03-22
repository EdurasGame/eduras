package de.illonis.eduras.shapes;

/**
 * A sword for close combat.
 * 
 * @author jme
 * 
 */
public class Sword extends org.newdawn.slick.geom.Polygon {

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
		trianglesDirty = true;
	}
}
