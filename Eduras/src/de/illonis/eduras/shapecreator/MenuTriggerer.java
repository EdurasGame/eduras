package de.illonis.eduras.shapecreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.illonis.eduras.shapecreator.ShapeCreator.FrameListener;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

/**
 * Triggers actions from menu clicks.
 * 
 * @author illonis
 * 
 */
public class MenuTriggerer implements MenuActionReactor {

	private final FrameListener frameListener;
	private final PanelModifier panel;

	MenuTriggerer(FrameListener listener, PanelModifier panel) {
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
	public void openShape(File f) {
		EditablePolygon polygon;
		try {
			polygon = ShapeFiler.loadShape(f.toURI().toURL());
			panel.setShape(polygon);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"File not found: " + f.getAbsolutePath());
		} catch (FileCorruptException e) {
			JOptionPane.showMessageDialog(null, "File contains invalid data:"
					+ f.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					null,
					"An error occured while reading file "
							+ f.getAbsolutePath() + ": " + e.getMessage());

			e.printStackTrace();
		}
	}

	@Override
	public void saveShape(File f) {
		try {
			ShapeFiler.saveShape(panel.getShape(), f);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Could not write to file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void rotateShape(float angle) {
		// TODO implement
	}

	@Override
	public void mirrorShape(Axis axis) {
		for (Vertice v : panel.getShape().getVertices()) {
			if (axis == Axis.VERTICAL) {
				v.setX(-v.getX());
			} else {
				v.setY(-v.getY());
			}
		}
		DataHolder.getInstance().notifyVerticesChanged();
	}

}
