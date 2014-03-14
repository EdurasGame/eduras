package de.illonis.eduras.gameclient.gui.game;

import java.awt.Canvas;
import java.awt.Color;

/**
 * The panel where the game is drawn on.
 * 
 * @author illonis
 * 
 */
public class GamePanel extends Canvas {

	private static final long serialVersionUID = 1L;

	GamePanel() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.black);
		setIgnoreRepaint(true);
	}
}
