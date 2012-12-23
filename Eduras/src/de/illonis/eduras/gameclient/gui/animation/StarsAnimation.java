package de.illonis.eduras.gameclient.gui.animation;

import de.illonis.eduras.math.Vector2D;

public class StarsAnimation extends Animation {

	public final static int NUM_STARS = 13;
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
		System.out.println(length);
		for (int i = 0; i < stars.length; i++) {
			Vector2D add = new Vector2D(i * 19, i * 19);
			stars[i] = new Vector2D(add);
			System.out.println(stars[i]);
		}
	}

	@Override
	public void run() {
		Vector2D add = new Vector2D(5, 5);
		while (true) {
			for (int i = 0; i < stars.length; i++) {
				stars[i].add(add);
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
