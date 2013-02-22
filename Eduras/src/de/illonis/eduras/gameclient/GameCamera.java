package de.illonis.eduras.gameclient;

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
		x = y = 0;
	}
}
