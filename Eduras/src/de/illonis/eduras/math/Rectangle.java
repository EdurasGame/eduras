package de.illonis.eduras.math;

/**
 * A rectangle, that shall replace java.awt.Rectangle in order to support
 * android or headless environments.
 * 
 * @author illonis
 * 
 */
public class Rectangle {

	private int x, y, width, height;

	/**
	 * Creates a rectangle at (0,0) with given size.
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	public Rectangle(int w, int h) {
		this(0, 0, w, h);
	}

	/**
	 * Creates a rectangle at given position with given size.
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	public Rectangle(int x, int y, int w, int h) {
		if (w <= 0 || h <= 0)
			throw new IllegalArgumentException(
					"Width and height of a rectangle must be non-zero positive.");
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	/**
	 * Checks whether or not this <code>Rectangle</code> contains the vector
	 * point.
	 * 
	 * @param point
	 *            the specified point.
	 * @return <code>true</code> if the point is inside this
	 *         <code>Rectangle</code>; <code>false</code> otherwise.
	 */
	public boolean contains(Vector2D point) {
		int x = this.x;
		int y = this.y;
		int w = this.width;
		int h = this.height;
		if (point.getX() < x || point.getY() < y) {
			return false;
		}
		w += x;
		h += y;
		// overflow || intersect
		return ((w < x || w > point.getX()) && (h < y || h > point.getY()));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Checks whether two rectangles are equal.
	 * <p>
	 * The result is <code>true</code> if and only if the argument is not
	 * <code>null</code> and is a <code>Rectangle</code> object that has the
	 * same upper-left corner, width, and height as this <code>Rectangle</code>.
	 * 
	 * @param obj
	 *            the <code>Object</code> to compare with this
	 *            <code>Rectangle</code>
	 * @return <code>true</code> if the objects are equal; <code>false</code>
	 *         otherwise.
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle r = (Rectangle) obj;
			return ((x == r.x) && (y == r.y) && (width == r.width) && (height == r.height));
		}
		return super.equals(obj);
	}

	/**
	 * Returns a <code>String</code> representing this <code>Rectangle</code>
	 * and its values.
	 * 
	 * @return a <code>String</code> representing this <code>Rectangle</code>
	 *         object's coordinate and size values.
	 */
	public String toString() {
		return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width
				+ ",height=" + height + "]";
	}

}
