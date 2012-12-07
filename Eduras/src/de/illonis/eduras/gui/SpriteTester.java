package de.illonis.eduras.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

import de.illonis.eduras.exceptions.ImageLoadingError;
import de.illonis.eduras.images.SpriteSheet;
import de.illonis.eduras.logger.EduLog;

/**
 * This class tests SpriteSheets. It draws all sprites from a spritesheet on a
 * frame. Therefore a testing sprite sheet "testsprite.png" is used. Each sprite
 * is drawn on a frame at random position. Positions are updated every
 * {@value #REFRESH_RATE}ms.
 * 
 * @author illonis
 * 
 */
public class SpriteTester extends JFrame {

	private static final long serialVersionUID = 1L;
	private final static int REFRESH_RATE = 1500;

	private BufferedImage[] img;
	private Random r;

	public SpriteTester() {
		super("Spritetester");
		r = new Random();
		setSize(400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		SpriteSheet s = null;
		try {
			s = new SpriteSheet("testsprite.png", 30);
		} catch (ImageLoadingError e) {
			EduLog.passException(e);
		}

		img = new BufferedImage[s.getTileCount()];

		for (int i = 0; i < s.getTileCount(); i++) {
			img[i] = s.getTileImage(i);
		}

		Thread t = new Thread(new Repainter());
		t.start();

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (img != null) {
			for (BufferedImage im : img) {
				g.drawImage(im, r.nextInt(getWidth()), r.nextInt(getHeight()),
						null);
			}

		}
	}

	public static void main(String[] args) {
		SpriteTester t = new SpriteTester();
		t.setLocationRelativeTo(null);
		t.setVisible(true);
	}

	private class Repainter implements Runnable {
		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(REFRESH_RATE);
				} catch (InterruptedException e) {
					EduLog.passException(e);
				}
			}
		}
	}
}
