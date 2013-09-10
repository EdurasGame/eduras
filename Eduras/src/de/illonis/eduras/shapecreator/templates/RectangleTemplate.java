package de.illonis.eduras.shapecreator.templates;

public class RectangleTemplate extends ShapeTemplate {

	@Override
	public String getName() {
		return "Rectangle";
	}

	public RectangleTemplate() {
		super();
		addVertice(-30, -30);
		addVertice(-30, 30);
		addVertice(30, 30);
		addVertice(30, -30);
	}

}
