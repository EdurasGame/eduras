package de.illonis.eduras.shapes;

import org.newdawn.slick.geom.Polygon;

/**
 * A basic house.
 * 
 * @author illonis
 * 
 */
public class House extends Polygon {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a basic house shape.
	 */
	public House() {
		// FIXME: change coordinates.
		addPoint(0, 20);
		addPoint(20, 0);
		addPoint(40, 20);
		addPoint(40, 60);
		addPoint(0, 60);
	}
}
