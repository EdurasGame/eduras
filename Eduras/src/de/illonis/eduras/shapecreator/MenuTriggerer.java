package de.illonis.eduras.shapecreator;

import java.io.File;

import de.illonis.eduras.shapecreator.ShapeCreator.FrameListener;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

public class MenuTriggerer implements MenuActionReactor {

	private final FrameListener frameListener;
	private final PanelModifier panel;

	public MenuTriggerer(FrameListener listener, PanelModifier panel) {
		this.frameListener = listener;
		this.panel = panel;
	}

	@Override
	public void exit() {
		frameListener.tryExit();
	}

	@Override
	public void undo() {
		panel.undo();
	}

	@Override
	public void redo() {
		panel.redo();
	}

	@Override
	public void setZoom(float factor) {
		panel.setZoom(factor);
	}

	@Override
	public void resetPanel() {
		panel.resetPanel();
	}

	@Override
	public void newShape() {
		panel.setShape(new EditablePolygon());
	}

	@Override
	public void newShape(String templateName) throws TemplateNotFoundException {
		EditablePolygon polygon = new EditablePolygon();
		polygon.importTemplate(templateName);
		panel.setShape(polygon);
	}

	@Override
	public void importShape(File f) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportShape(File f) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateShape(float angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mirrorShape(Axis axis) {
		// TODO Auto-generated method stub

	}

}
