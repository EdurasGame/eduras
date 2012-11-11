package de.illonis.eduras.gui;

import java.awt.Canvas;
import java.awt.Color;

/**
 * A panel that represents the gameworld. All world objects and user interface
 * are displayed here.
 * 
 * @author illonis
 * 
 */
public class GamePanel extends Canvas {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new gamePanel with black background.
	 */
	public GamePanel() {
		setFocusable(true);
		setBackground(Color.black);

	}

}
