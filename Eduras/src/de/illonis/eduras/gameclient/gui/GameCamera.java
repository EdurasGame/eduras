package de.illonis.eduras.gameclient.gui;

import java.awt.Rectangle;

/**
 * GameCamera stores where current viewport of player is.
 * 
 * @author illonis
 * 
 */
public class GameCamera extends Rectangle {

	private static final long serialVersionUID = 1L;
	private double scale;
	private int targetX, targetY;
	private boolean running = true;

	GameCamera() {
		super();
		reset();
		targetX = 0;
		targetY = 0;
		scale = 1;
		Thread t = new Thread(cameraMover);
		t.setName("CameraMover");
		t.start();
	}

	void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Resets camera to original position.
	 * 
	 * @author illonis
	 */
	void reset() {
		setLocation(0, 0);
	}

	@Override
	public void setLocation(int x, int y) {
		targetX = x;
		targetY = y;
	}

	/**
	 * Centers camera to given point.
	 * 
	 * @param newX
	 *            x coordinate of new center.
	 * @param newY
	 *            y coordinate of new center.
	 */
	void centerAt(int newX, int newY) {
		setLocation((int) Math.round(newX - (width / scale) / 2),
				(int) Math.round(newY - (height / scale) / 2));
	}

	private Runnable cameraMover = new Runnable() {

		@Override
		public void run() {
			while (running) {
				int xDiff = targetX - x;
				int yDiff = targetY - y;

				int newX = x + moveStep(xDiff);
				int newY = y + moveStep(yDiff);
				GameCamera.super.setLocation(newX, newY);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		private int moveStep(int diff) {
			if (diff == 0)
				return 0;
			int negative = (diff >= 0) ? 1 : -1;
			int abs = Math.abs(diff);
			if (abs > 100)
				return negative * 10;
			else if (abs > 50)
				return negative * 7;
			else
				return negative;
		}
	};

	/**
	 * Stops automatic camera movement.
	 */
	public void stopMoving() {
		running = false;
	}

}
