package de.illonis.eduras.shapecreator;

import java.io.File;

import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

public interface MenuActionReactor {

	public enum Axis {
		VERTICAL, HORIZONTAL;
	}

	void exit();

	void undo();

	void redo();

	void setZoom(float factor);

	void resetPanel();

	void newShape();

	void newShape(String template) throws TemplateNotFoundException;

	void importShape(File f);

	void exportShape(File f);

	void rotateShape(float angle);

	void mirrorShape(Axis axis);

}
