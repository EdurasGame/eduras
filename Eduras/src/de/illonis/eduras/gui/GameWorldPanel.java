package de.illonis.eduras.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * A panel that shows gameworld.
 * 
 * @author illonis
 * 
 */
public class GameWorldPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image img;

	/**
	 * Creates a new gameworldpanel with black background.
	 */
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

	/**
	 * Sets draw image to given bufferimage. That image is painted on screen on
	 * repaint.
	 * 
	 * @param dbImage
	 *            new image.
	 */
	public void setImage(Image dbImage) {
		img = dbImage;

	}
}
