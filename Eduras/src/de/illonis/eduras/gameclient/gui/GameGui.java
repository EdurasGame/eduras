package de.illonis.eduras.gameclient.gui;

import java.awt.Color;

import javax.swing.JPanel;

public class GameGui extends JPanel {

	private static final long serialVersionUID = 1L;

	GameGui() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.black);
		setDoubleBuffered(true);
	}
}
