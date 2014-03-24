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
		addVector2df(-20, -15);
		addVector2df(0, -35);
		addVector2df(20, -15);
		addVector2df(20, 25);
		addVector2df(-20, 25);
	}
}
