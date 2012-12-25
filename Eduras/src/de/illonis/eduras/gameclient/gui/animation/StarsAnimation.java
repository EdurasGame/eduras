package de.illonis.eduras.gameclient.gui.animation;

import de.illonis.eduras.math.Vector2D;

/**
 * Simulates a simple star animation.
 * 
 * @author illonis
 * 
 */
public class StarsAnimation extends Animation {

	// Must be NUM_STARS % 4 == 0
	public final static int NUM_STARS = 20;
	private Vector2D stars[];
	private int width, height;
	private double length;
	Vector2D middle;

	Vector2D getMiddle() {
		return middle;
	}

	StarsAnimation() {
		width = height = 500;
		// init stars
		stars = new Vector2D[NUM_STARS];

		middle = new Vector2D(width / 2, height / 2);
		length = middle.getLength();
		int q = NUM_STARS / 4;

		for (int i = 0; i < q; i++) {
			double newl = (i + .1) * (length / q);
			Vector2D add = new Vector2D(i + .1, i + .1);
			add.setLength(newl);

			// add 4 stars with different rotation
			for (int j = 0; j < 4; j++) {
				add.rotate(90);
				stars[i + q * j] = add.copy();
			}
		}
	}

	@Override
	public void run() {
		Vector2D add = new Vector2D(5, 5);
		while (true) {
			for (int i = 0; i < stars.length; i++) {
				stars[i].modifyLength(add.getLength());
				if (stars[i].getLength() > length) {
					stars[i].setLength(5);
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	Vector2D[] getStars() {
		return stars;
	}
}
