package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.geom.Rectangle;

/**
 * GameCamera stores where current viewport of player is.
 * 
 * @author illonis
 * 
 */
public class GameCamera extends Rectangle {

	private static final long serialVersionUID = 1L;


	GameCamera() {
		super(0, 0, 10, 10);
		reset();
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
	void centerAt(float newX, float newY) {
		setLocation(newX - width / 2, newY - height / 2);
	}
}
