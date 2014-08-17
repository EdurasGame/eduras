package de.illonis.eduras.shapecreator.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;

import javax.swing.JPanel;

import org.newdawn.slick.geom.Line;

import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.DataHolder;

/**
 * A drawing panel where user creates and edits polygon on.
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
	private Vector2df hoverVector2df, selectedVector2df;

	/**
	 * Creates an new drawpanel.
	 */
	public DrawPanel() {
		this.data = DataHolder.getInstance();
		setPreferredSize(DEFAULT_SIZE);
		coordinateSystem = new CoordinateSystem();
		addComponentListener(new ResizeMonitor());
	}

	/**
	 * Retrieves the coordinate system that is currently active on this panel.
	 * 
	 * @return the used coordinate system.
	 */
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
		if (data.getBackgroundImage() != null) {
			int iw = (int) (data.getBackgroundImage().getWidth(null) * coordinateSystem
					.getZoom());
			int ih = (int) (data.getBackgroundImage().getHeight(null) * coordinateSystem
					.getZoom());
			Image image = data.getBackgroundImage();
			if (coordinateSystem.getZoom() != 1f) {
				image = image.getScaledInstance(iw, ih, Image.SCALE_FAST);
			}
			g2d.drawImage(image, (getWidth() - iw) / 2, (getHeight() - ih) / 2,
					null);
		}
		coordinateSystem.draw(g2d);
		drawTempLines(g2d);
		paintVector2dfs(g2d);
	}

	private void drawLine(Line line, Graphics2D g2d) {
		GuiPoint start = coordinateSystem.coordinateToGui(line.getStart());
		GuiPoint end = coordinateSystem.coordinateToGui(line.getEnd());
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}

	private void drawTempLines(Graphics2D g2d) {
		g2d.setColor(Color.green);
		Line a = data.getTempLineA();
		Line b = data.getTempLineB();
		if (a != null) {
			drawLine(a, g2d);
		}
		if (b != null) {
			drawLine(b, g2d);
		}
	}

	private void paintVector2dfs(Graphics2D g2d) {
		Color shapeLineColor = data.getSettings().getShapeLineColor();
		Color shapeDotColor = data.getSettings().getShapeDotColor();
		Color shapeLastLineColor = data.getSettings().getShapeLastLineColor();

		Collection<Vector2df> vertices = data.getPolygon().getVector2dfs();
		boolean first = true;
		Vector2df firstVector2df = null;
		Vector2df last = null;
		GuiPoint p = null;
		for (Vector2df vertice : vertices) {

			p = coordinateSystem.coordinateToGui(vertice);
			if (vertice == hoverVector2df)
				g2d.setColor(data.getSettings().getHoverShapeDotColor());
			else if (vertice == selectedVector2df)
				g2d.setColor(data.getSettings().getSelectedShapeDotColor());
			else
				g2d.setColor(shapeDotColor);
			p.draw(g2d);
			if (first) {
				firstVector2df = vertice;
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
					.coordinateToGui(firstVector2df);
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

	/**
	 * Triggers that user's mouse is over a vertice.
	 * 
	 * @param vert
	 *            the hovered vertice.
	 */
	public void onVector2dfHover(Vector2df vert) {
		setCursor(MOVE_CURSOR);
		hoverVector2df = vert;
	}

	/**
	 * Triggers that user's mouse is not above any vertice.
	 */
	public void onVector2dfLeft() {
		setCursor(Cursor.getDefaultCursor());
		hoverVector2df = null;
	}

	/**
	 * Marks given vertice as selected.
	 * 
	 * @param v
	 *            the selected vertice.
	 */
	public void selectVector2df(Vector2df v) {
		data.verticeSelectedOnGui(v);
		onVector2dfSelected(v);
	}

	/**
	 * Selects given vertice.
	 * 
	 * @param vertice
	 *            the selected vertice.
	 */
	public void onVector2dfSelected(Vector2df vertice) {
		this.selectedVector2df = vertice;
	}

}
