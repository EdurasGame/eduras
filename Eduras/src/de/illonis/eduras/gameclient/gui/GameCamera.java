package de.illonis.eduras.gameclient.gui;

import java.awt.Rectangle;

/**
 * GameCamera stores where current viewport of player is.
 * 
 * @author illonis
 * 
 */
public class GameCamera extends Rectangle {

	private static final long serialVersionUID = 1L;
	private double scale;

	GameCamera() {
		reset();
		scale = 1;
	}

	void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Resets camera to original position.
	 * 
	 * @author illonis
	 */
	void reset() {
		setLocation(0, 0);
	}

	/**
	 * Centers camera to given point.
	 * 
	 * @param newX
	 *            x coordinate of new center.
	 * @param newY
	 *            y coordinate of new center.
	 */
	void centerAt(int newX, int newY) {
		setLocation((int) Math.round(newX - (width / scale) / 2),
				(int) Math.round(newY - (height / scale) / 2));
	}

}
