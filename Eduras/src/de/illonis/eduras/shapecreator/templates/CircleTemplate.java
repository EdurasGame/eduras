package de.illonis.eduras.shapecreator.templates;

/**
 * A template for a circle.
 * 
 * @author illonis
 * 
 */
public class CircleTemplate extends ShapeTemplate {

	@Override
	public String getName() {
		return "Circle";
	}

	/**
	 * Creates a new template scheme.
	 */
	public CircleTemplate() {
		int max = 15;
		int radius = 30;
		double r = 0;
		double c = Math.PI * 2;
		while (r < c) {
			double x = Math.sin(r) * radius;
			double y = Math.cos(r) * radius;
			addVertice(x, y);
			r += (double) c / max;
		}
	}
}
