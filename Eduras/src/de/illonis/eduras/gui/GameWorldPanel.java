package de.illonis.eduras.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class GameWorldPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image img;

	public GameWorldPanel() {
		setBackground(Color.black);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			g.drawImage(img, 0, 0, null);
		}
	}

	public void setImage(Image dbImage) {
		img = dbImage;

	}
}
