package de.illonis.eduras.gameclient.gui.animation;

import de.illonis.eduras.math.Vector2D;

/**
 * Simulates a simple star animation. To vary animation you can change static
 * variables as you like.
 * 
 * @author illonis
 * 
 */
public class StarsAnimation extends Animation {

	/**
	 * Number of axis that stars will fly on from center.
	 */
	public final static int AXES = 20;
	/**
	 * Stars per axis.
	 */
	public final static int STARS_PER_AXES = 15;
	/**
	 * Size of stars.
	 */
	public final static int CIRCLE_SIZE = 5;

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
		stars = new Vector2D[AXES * STARS_PER_AXES];

		middle = new Vector2D(width / 2, height / 2);
		length = middle.getLength();

		for (int i = 0; i < STARS_PER_AXES; i++) {
			double newl = (i + 00.1) * (length / STARS_PER_AXES);
			Vector2D add = new Vector2D(i + .001, i + .001);
			add.setLength(newl);

			// add AXES stars with different rotation
			for (int j = 0; j < AXES; j++) {
				add.rotate((double) 360 / AXES);
				stars[i + STARS_PER_AXES * j] = add.copy();
			}
		}
	}

	@Override
	public void run() {
		Vector2D add = new Vector2D(1, 1);
		while (true) {
			for (int i = 0; i < stars.length; i++) {
				stars[i].modifyLength(add.getLength());
				if (stars[i].getLength() > length) {
					stars[i].setLength(5);
				}
			}

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	Vector2D[] getStars() {
		return stars;
	}
}
