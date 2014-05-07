package de.illonis.eduras.gameclient.gui.hud.minimap;

/**
 * 
 * @author illonis
 * 
 */
public abstract class MiniMapObject {

	private float x;
	private float y;
	private float width;
	private float height;

	/**
	 * @param x
	 *            the x-position on screen.
	 * @param y
	 *            the y-position on screen.
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 */
	public MiniMapObject(float x, float y, float width, float height) {
		this.width = width;
		this.height = height;
		setLocation(x, y);
	}

	/**
	 * Relocates this object.
	 * 
	 * @param x
	 *            new x-location
	 * @param y
	 *            new y-location
	 */
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return x-coordinate.
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return y-coordinate.
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return width of this object.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * 
	 * @return height of this object.
	 */
	public float getHeight() {
		return height;
	}

}
