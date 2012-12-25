package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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

		g.setColor(Color.BLUE);
		for (int i = 0; i < stars.getStars().length; i++) {
			Vector2D star = stars.getStars()[i].copy();
			star.add(stars.getMiddle());
			g2d.fillOval((int) star.getX(), (int) star.getY(),
					StarsAnimation.CIRCLE_SIZE, StarsAnimation.CIRCLE_SIZE);
		}
	}

	void test() {
		// render thread
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(40);
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
