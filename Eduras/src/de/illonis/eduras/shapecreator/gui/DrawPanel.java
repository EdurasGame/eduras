package de.illonis.eduras.shapecreator.gui;

import java.awt.Color;
import java.awt.Cursor;
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
 * A drawing panel where user creates polygon on.
 * 
 * @author illonis
 * 
 */
public class DrawPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final static Dimension DEFAULT_SIZE = new Dimension(500, 300);
	private final static Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);

	private CoordinateSystem coordinateSystem;
	private final DataHolder data;
	private Vertice hoverVertice, selectedVertice;

	public DrawPanel() {
		this.data = DataHolder.getInstance();
		setPreferredSize(DEFAULT_SIZE);
		coordinateSystem = new CoordinateSystem();
		addComponentListener(new ResizeMonitor());
	}

	public CoordinateSystem getCoordinateSystem() {
		return coordinateSystem;
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

		coordinateSystem.draw(g2d);
		paintVertices(g2d);
	}

	private void paintVertices(Graphics2D g2d) {
		Color shapeLineColor = data.getSettings().getShapeLineColor();
		Color shapeDotColor = data.getSettings().getShapeDotColor();

		Collection<Vertice> vertices = data.getPolygon().getVertices();
		boolean first = true;
		Vertice firstVertice = null;
		Vertice last = null;
		GuiPoint p = null;
		for (Vertice vertice : vertices) {

			p = coordinateSystem.coordinateToGui(vertice);
			if (vertice == hoverVertice)
				g2d.setColor(data.getSettings().getHoverShapeDotColor());
			else if (vertice == selectedVertice)
				g2d.setColor(data.getSettings().getSelectedShapeDotColor());
			else
				g2d.setColor(shapeDotColor);
			p.draw(g2d);
			if (first) {
				firstVertice = vertice;
				first = false;
			} else {
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
			g2d.setColor(shapeLineColor);
			g2d.drawLine(p.x, p.y, firstPoint.x, firstPoint.y);
		}
	}

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

	public void onVerticeHover(Vertice vert) {
		setCursor(MOVE_CURSOR);
		hoverVertice = vert;
	}

	public void onVerticeLeft() {
		setCursor(Cursor.getDefaultCursor());
		hoverVertice = null;
	}

	public void selectVertice(Vertice v) {
		data.verticeSelectedOnGui(v);
		onVerticeSelected(v);
	}

	public void onVerticeSelected(Vertice selectedVertice) {
		this.selectedVertice = selectedVertice;
	}

}
