package de.illonis.eduras.mapeditor;

/**
 * Sets status bar text.
 * 
 * @author illonis
 * 
 */
public interface StatusListener {

	/**
	 * Sets the text of the status bar.
	 * 
	 * @param text
	 *            new text.
	 */
	void setStatus(String text);

	/**
	 * Sets statustext to coordinate display.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 */
	void setCoordinate(float x, float y);
}
