package de.illonis.eduras;

import java.awt.Rectangle;

import de.illonis.eduras.math.Vector2D;

/**
 * A playable map for gaming.
 * 
 * @author illonis
 * 
 */
public class Map {

	private int width;
	private int height;

	/**
	 * Creates a new map with default size.
	 */
	public Map() {
		width = 2000;
		height = 2000;
	}

	/**
	 * Returns height of map.
	 * 
	 * @return height of map.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns width of map.
	 * 
	 * @return width of map.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns bounds of map. A map's top left corner always has coordinates
	 * (0,0), so any negative coordinates are not in map area.
	 * 
	 * @return bounds of map as rectangle.
	 */
	public Rectangle getBounds() {
		return new Rectangle(0, 0, width, height);
	}

	/**
	 * Checks whether given point is within map bounds.
	 * 
	 * @param point
	 *            point to test.
	 * @return true if point is on map.
	 */
	public boolean contains(Vector2D point) {
		return getBounds().contains(point.toPoint());
	}

}