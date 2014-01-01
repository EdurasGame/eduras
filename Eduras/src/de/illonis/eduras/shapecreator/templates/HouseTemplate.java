package de.illonis.eduras.shapecreator.templates;

/**
 * A template for a house.
 * 
 * @author illonis
 * 
 */
public class HouseTemplate extends ShapeTemplate {

	@Override
	public String getName() {
		return "House";
	}

	/**
	 * Creates a house scheme.
	 */
	public HouseTemplate() {
		addVertice(-20, -15);
		addVertice(0, -35);
		addVertice(20, -15);
		addVertice(20, 25);
		addVertice(-20, 25);
	}
}
