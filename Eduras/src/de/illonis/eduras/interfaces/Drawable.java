package de.illonis.eduras.interfaces;

import java.awt.Graphics2D;

/**
 * A drawable object.
 * 
 * @author illonis
 * 
 */
public interface Drawable {

	/**
	 * Draws on given graphics.
	 * 
	 * @param g
	 *            graphics object.
	 */
	void draw(Graphics2D g);

}
