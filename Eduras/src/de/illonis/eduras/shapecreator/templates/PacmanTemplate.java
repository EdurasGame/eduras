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
		getDefaultVector2dfs().get(4).set(4, 1);
	}
}
