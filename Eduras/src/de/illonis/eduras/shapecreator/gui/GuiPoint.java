package de.illonis.eduras.shapecreator.gui;

import java.awt.Graphics2D;
import java.awt.Point;

import de.illonis.eduras.interfaces.Drawable;

/**
 * A point in the graphical interface.
 * 
 * @author illonis
 * 
 */
public class GuiPoint extends Point implements Drawable {

	private static final long serialVersionUID = 1L;

	protected static int SIZE = 7;

	/**
	 * Creates a new point.
	 * 
	 * @param x
	 *            x-coordinate of the point.
	 * @param y
	 *            y-coordinate of the point.
	 */
	public GuiPoint(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics2D g) {
		g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
	}

}
