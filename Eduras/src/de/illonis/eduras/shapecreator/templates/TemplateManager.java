package de.illonis.eduras.shapecreator.templates;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.shapecreator.Vertice;

/**
 * Manages existing templates.
 * 
 * @author illonis
 * 
 */
public class TemplateManager {

	private static TemplateManager instance;

	/**
	 * Returns the instance of {@link TemplateManager}.
	 * 
	 * @return instance.
	 */
	public static TemplateManager getInstance() {
		if (instance == null)
			instance = new TemplateManager();
		return instance;
	}

	private final HashMap<String, ShapeTemplate> templates;

	private TemplateManager() {
		templates = new HashMap<String, ShapeTemplate>();
		loadTemplates();
	}

	private void loadTemplates() {
		registerTemplate(new HouseTemplate());
		registerTemplate(new RectangleTemplate());
		registerTemplate(new CircleTemplate());
		registerTemplate(new PacmanTemplate());
	}

	void registerTemplate(ShapeTemplate template) {
		templates.put(template.getName(), template);
	}

	/**
	 * @return a list copy of all templates.
	 */
	public LinkedList<ShapeTemplate> getTemplates() {
		return new LinkedList<ShapeTemplate>(templates.values());
	}

	/**
	 * Retrieves vertices from a template.
	 * 
	 * @param templateName
	 *            the name of the used template.
	 * @return the vertices of the template.
	 * @throws TemplateNotFoundException
	 *             if template was not found.
	 */
	public LinkedList<Vertice> getVertsOfTemplate(String templateName)
			throws TemplateNotFoundException {
		ShapeTemplate t = templates.get(templateName);
		if (t == null)
			throw new TemplateNotFoundException(templateName);
		LinkedList<Vertice> vertices = new LinkedList<Vertice>();
		for (Vertice v : t.getDefaultVertices()) {
			vertices.add(new Vertice(v.getX(), v.getY()));
		}
		return vertices;
	}
}
