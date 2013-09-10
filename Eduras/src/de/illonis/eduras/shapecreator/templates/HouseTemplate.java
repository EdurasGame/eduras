package de.illonis.eduras.shapecreator.templates;

public class HouseTemplate extends ShapeTemplate {

	@Override
	public String getName() {
		return "House";
	}

	public HouseTemplate() {
		addVertice(-20, -40);
		addVertice(0, -60);
		addVertice(20, -40);
		addVertice(20, 0);
		addVertice(-20, 0);
	}
}
