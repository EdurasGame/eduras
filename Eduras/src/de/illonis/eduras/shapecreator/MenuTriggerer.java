package de.illonis.eduras.shapecreator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.PanelInteractor.InteractMode;
import de.illonis.eduras.shapecreator.ShapeCreator.FrameListener;
import de.illonis.eduras.shapecreator.gui.ToolPanel;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

/**
 * Triggers actions from menu clicks.
 * 
 * @author illonis
 * 
 */
public class MenuTriggerer implements MenuActionReactor {

	private final static Logger L = EduLog.getLoggerFor(MenuTriggerer.class
			.getName());

	private final FrameListener frameListener;
	private final PanelModifier panel;
	private final ToolPanel toolPanel;

	MenuTriggerer(ToolPanel toolPanel, FrameListener listener,
			PanelModifier panel) {
		this.frameListener = listener;
		this.panel = panel;
		this.toolPanel = toolPanel;
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
			L.log(Level.WARNING, "File contains invalid data.", e);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					null,
					"An error occured while reading file "
							+ f.getAbsolutePath() + ": " + e.getMessage());
			L.log(Level.SEVERE, "Error reading the file.", e);
		}
	}

	@Override
	public void saveShape(File f) {
		try {
			ShapeFiler.saveShape(panel.getShape(), f);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Could not write to file: " + e.getMessage());
			L.log(Level.SEVERE, "Error writing to file.", e);
		}
	}

	@Override
	public void rotateShape(float angle) {
		if (angle % 360 == 0) {
			return;
		}
		System.out.println("rotating shape by " + angle + " degree.");
		// TODO: implement
	}

	@Override
	public void mirrorShape(Axis axis) {
		for (Vector2df v : panel.getShape().getVector2dfs()) {
			if (axis == Axis.VERTICAL) {
				v.x = -v.x;
			} else {
				v.y = -v.y;
			}
		}
		DataHolder.getInstance().notifyVector2dfsChanged();
	}

	@Override
	public void setMode(InteractMode mode) {
		toolPanel.onModeSet(mode);
	}

	@Override
	public void modZoom(float modifier) {
		panel.modZoom(modifier);
	}

	@Override
	public void loadBackgroundImage(File f) {
		try {
			BufferedImage image = ImageIO.read(f);
			panel.setBackgroundImage(image);
		} catch (IOException e) {
			L.log(Level.WARNING, "Could not load file", e);
		}		
	}
}
