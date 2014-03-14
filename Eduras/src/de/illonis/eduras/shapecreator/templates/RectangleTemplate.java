package de.illonis.eduras.shapecreator.templates;

/**
 * A template for a square.
 * 
 * @author illonis
 * 
 */
public class RectangleTemplate extends ShapeTemplate {

	@Override
	public String getName() {
		return "Rectangle";
	}

	/**
	 * Creates a new rectangle template.
	 */
	public RectangleTemplate() {
		super();
		addVertice(-30, -30);
		addVertice(-30, 30);
		addVertice(30, 30);
		addVertice(30, -30);
	}

}
