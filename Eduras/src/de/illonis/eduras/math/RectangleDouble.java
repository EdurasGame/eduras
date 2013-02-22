package de.illonis.eduras.math;

public class RectangleDouble extends Rectangle {

	public double x, y, width, height;

	/**
	 * Creates a rectangle at (0,0) with given size.
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	public RectangleDouble(double w, double h) {
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
	public RectangleDouble(double x, double y, double w, double h) {
		super((int) x, (int) y, (int) w, (int) h);
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public RectangleDouble() {
		this(0, 0);
	}

	@Override
	public boolean contains(Vector2D point) {
		double x = this.x;
		double y = this.y;
		double w = this.width;
		double h = this.height;
		if (point.getX() < x || point.getY() < y) {
			return false;
		}
		w += x;
		h += y;
		// overflow || intersect
		return ((w < x || w > point.getX()) && (h < y || h > point.getY()));
	}

	/**
	 * Sets size of rectangle to given size.
	 * 
	 * @param width
	 *            new width.
	 * @param height
	 *            new height.
	 */
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean intersects(Rectangle r) {
		double tw = this.width;
		double th = this.height;
		double rw = r.width;
		double rh = r.height;
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		double tx = this.x;
		double ty = this.y;
		double rx = r.x;
		double ry = r.y;
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		// overflow || intersect
		return ((rw < rx || rw > tx) && (rh < ry || rh > ty)
				&& (tw < tx || tw > rx) && (th < ty || th > ry));
	}
}
