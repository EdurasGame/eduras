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
		float r = 0;
		float c = (float) Math.PI * 2;
		while (r < c) {
			float x = (float) Math.sin(r) * radius;
			float y = (float) Math.cos(r) * radius;
			addVector2df(x, y);
			r += (float) c / max;
		}
	}
}
