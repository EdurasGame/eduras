package de.illonis.eduras.shapecreator.templates;

/**
 * A template for a circle.
 * 
 * @author illonis
 * 
 */
public class PacmanTemplate extends CircleTemplate {

	@Override
	public String getName() {
		return "Pacman";
	}

	/**
	 * Creates a new template scheme.
	 */
	public PacmanTemplate() {
		super();
		getDefaultVertices().get(4).update(4, 1);
	}
}
