package de.illonis.eduras.shapecreator.templates;

public class TemplateNotFoundException extends Exception {

	public TemplateNotFoundException(String templateName) {
		super("Missing template: " + templateName);
	}
}
