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

	GameCamera() {
		reset();
	}

	/**
	 * Resets camera to original position.
	 * 
	 * @author illonis
	 */
	public void reset() {
		setLocation(0, 0);
	}
}
