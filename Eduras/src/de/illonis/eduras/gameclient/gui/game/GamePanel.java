package de.illonis.eduras.gameclient.gui.game;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * The panel where the game is drawn on.
 * 
 * @author illonis
 * 
 */
public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	GamePanel() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.black);
		setDoubleBuffered(true);
	}
}
