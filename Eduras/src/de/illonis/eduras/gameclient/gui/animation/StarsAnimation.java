package de.illonis.eduras.gameclient.gui.animation;

import de.illonis.eduras.math.Vector2D;

public class StarsAnimation extends Animation {

	public final static int NUM_STARS = 20;
	private Vector2D stars[];
	private int width, height;
	private double length;
	Vector2D middle;

	public Vector2D getMiddle() {
		return middle;
	}

	public StarsAnimation() {
		width = height = 500;

		stars = new Vector2D[NUM_STARS];

		middle = new Vector2D(width / 2, height / 2);
		length = middle.getLength();
		int q = NUM_STARS / 4;

		for (int i = 0; i < q; i++) {
			double newl = (i + .1) * (length / q);
			Vector2D add = new Vector2D(i + .1, i + .1);
			add.setLength(newl);
			stars[i] = add;

			Vector2D add2 = new Vector2D(-i - .1, i + .1);
			add2.setLength(newl);
			stars[i + q] = add2;

			Vector2D add3 = new Vector2D(-i - .1, -i - .1);
			add3.setLength(newl);
			stars[i + q * 2] = add3;

			Vector2D add4 = new Vector2D(i + .1, -i - .1);
			add4.setLength(newl);
			stars[i + q * 3] = add4;

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

	public Vector2D[] getStars() {
		return stars;
	}
}
