package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.illonis.eduras.math.Vector2D;

public class TestAnimation extends JPanel {

	private static final long serialVersionUID = 1L;
	private static StarsAnimation stars;

	public TestAnimation() {
		stars = new StarsAnimation();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);
		for (int i = 0; i < StarsAnimation.NUM_STARS; i++) {
			Vector2D star = stars.getStars()[i].copy();
			star.add(stars.getMiddle());
			g2d.fillOval((int) star.getX(), (int) star.getY(), 15, 15);
		}
	}

	void test() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					repaint();
				}
			}
		});
		t.start();
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("test");

		final TestAnimation ani = new TestAnimation();
		f.add(ani);
		ani.test();
		f.setSize(500, 500);
		f.setVisible(true);

		Thread ab = new Thread(stars);
		ab.start();

	}
}
