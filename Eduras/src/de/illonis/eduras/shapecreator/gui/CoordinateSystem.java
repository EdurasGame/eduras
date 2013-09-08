package de.illonis.eduras.shapecreator.gui;

import java.awt.Graphics2D;

import de.illonis.eduras.interfaces.Drawable;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapecreator.DataHolder;

/**
 * A two-dimensional coordinate system that supports translation between gui
 * positions and coordinate-system's positions.
 * 
 * @author illonis
 * 
 */
public class CoordinateSystem implements Drawable {

	private final GuiPoint origin;

	/**
	 * Creates a new coordinate system.
	 */
	public CoordinateSystem() {
		origin = new GuiPoint(0, 0);
	}

	/**
	 * Sets the location of the origin relative to gui.
	 * 
	 * @param x
	 *            x-position of origin.
	 * @param y
	 *            y-position of origin.
	 * 
	 * @author illonis
	 */
	public void setOrigin(int x, int y) {
		origin.setLocation(x, y);
	}

	/**
	 * @return current origin.
	 * 
	 * @author illonis
	 */
	public GuiPoint getOrigin() {
		return origin;
	}

	/**
	 * Converts given point of gui into a coordinate position.
	 * 
	 * @param point
	 *            the gui location.
	 * @return the coordinate position that represents this gui location.
	 * 
	 * @author illonis
	 */
	public Vector2D guiToCoordinate(GuiPoint point) {
		float zoomFactor = DataHolder.getInstance().getZoom();
		int guiX = point.x;
		int guiY = point.y;
		float zoom = 1.0f / zoomFactor;
		int coordX = Math.round((guiX - origin.x) * zoom);
		int coordY = Math.round((guiY - origin.y) * zoom);
		Vector2D pos = new Vector2D(coordX, coordY);
		pos.mult(1.0f / zoomFactor);
		return pos;
	}

	/**
	 * Converts given coordinate point to a graphical location in the gui.
	 * 
	 * @param point
	 *            the coordinate point.
	 * @return the location of the coordinate point in the gui.
	 * 
	 * @author illonis
	 */
	public GuiPoint coordinateToGui(Vector2D point) {
		float zoomFactor = DataHolder.getInstance().getZoom();
		int coordX = (int) Math.round(point.getX() * zoomFactor);
		int coordY = (int) Math.round(point.getY() * zoomFactor);

		int guiX = coordX + origin.x;
		int guiY = coordY + origin.y;
		GuiPoint pos = new GuiPoint(guiX, guiY);

		return pos;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(DataHolder.getInstance().getSettings().getGridColor());
		float zoomFactor = DataHolder.getInstance().getZoom();

		// horizontal line
		g.drawLine(0, origin.y, g.getClipBounds().width, origin.y);

		for (int i = origin.x; i < g.getClipBounds().width; i += 10 * zoomFactor) {
			g.drawLine(i, origin.y - GuiPoint.SIZE / 3, i, origin.y
					+ GuiPoint.SIZE / 3);
		}
		for (int i = origin.x; i >= 0; i -= 10 * zoomFactor) {
			g.drawLine(i, origin.y - GuiPoint.SIZE / 3, i, origin.y
					+ GuiPoint.SIZE / 3);
		}

		// vertical line
		g.drawLine(origin.x, 0, origin.x, g.getClipBounds().height);
		for (int i = origin.y; i < g.getClipBounds().height; i += 10 * zoomFactor) {
			g.drawLine(origin.x - GuiPoint.SIZE / 3, i, origin.x
					+ GuiPoint.SIZE / 3, i);
		}
		for (int i = origin.y; i >= 0; i -= 10 * zoomFactor) {
			g.drawLine(origin.x - GuiPoint.SIZE / 3, i, origin.x
					+ GuiPoint.SIZE / 3, i);
		}

		// origin
		g.drawOval(origin.x - GuiPoint.SIZE / 2, origin.y - GuiPoint.SIZE / 2,
				GuiPoint.SIZE, GuiPoint.SIZE);
	}
}