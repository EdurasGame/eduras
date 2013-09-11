package de.illonis.eduras.shapecreator.templates;

/**
 * Indicates that a template was not found.
 * 
 * @author illonis
 * 
 */
public class TemplateNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param templateName
	 *            name of the template.
	 */
	public TemplateNotFoundException(String templateName) {
		super("Missing template: " + templateName);
	}
}
