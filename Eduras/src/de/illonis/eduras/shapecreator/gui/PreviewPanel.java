package de.illonis.eduras.shapecreator.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;

import javax.swing.JPanel;

import de.illonis.eduras.shapecreator.DataHolder;
import de.illonis.eduras.shapecreator.Vertice;

/**
 * Provides a preview of the shape in gamesize.
 * 
 * @author illonis
 * 
 */
public class PreviewPanel extends JPanel {

	private CoordinateSystem coordinateSystem;
	private final DataHolder data;

	public PreviewPanel() {
		data = DataHolder.getInstance();
		Dimension size = new Dimension(150, 150);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		coordinateSystem = new CoordinateSystem();
		addComponentListener(new ResizeMonitor());
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setColor(data.getSettings().getBackgroundColor());
		g2d.fillRect(0, 0, getWidth(), getHeight());
		drawPreview(g2d);
	}

	private void drawPreview(Graphics2D g2d) {
		Color shapeLineColor = data.getSettings().getShapeLineColor();
		Color shapeLastLineColor = data.getSettings().getShapeLastLineColor();

		Collection<Vertice> vertices = data.getPolygon().getVertices();
		boolean first = true;
		Vertice firstVertice = null;
		Vertice last = null;
		GuiPoint p = null;
		for (Vertice vertice : vertices) {

			p = coordinateSystem.coordinateToGui(vertice);

			if (first) {
				firstVertice = vertice;
				first = false;
			} else {
				g2d.setStroke(new BasicStroke(1.5f));
				GuiPoint lastPoint = coordinateSystem.coordinateToGui(last);
				g2d.setColor(shapeLineColor);
				g2d.drawLine(lastPoint.x, lastPoint.y, p.x, p.y);
			}
			last = vertice;
		}
		if (vertices.size() > 1) {
			// connect last point with first one
			GuiPoint firstPoint = coordinateSystem
					.coordinateToGui(firstVertice);
			g2d.setColor(shapeLastLineColor);
			g2d.drawLine(p.x, p.y, firstPoint.x, firstPoint.y);
		}
	}

	/**
	 * Repositions the origin to the center of this panel.
	 */
	public void centerOrigin() {
		int w = getWidth();
		int h = getHeight();
		coordinateSystem.setOrigin(w / 2, h / 2);
	}

	private class ResizeMonitor extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			centerOrigin();
		}
	}
}