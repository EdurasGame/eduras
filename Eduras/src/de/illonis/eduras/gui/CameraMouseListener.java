package de.illonis.eduras.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Listens for mouse movement on gui to trigger camera movement.
 * 
 * @author illonis
 * 
 */
public class CameraMouseListener extends MouseAdapter {

	private GameCamera camera;
	private int dx, dy;
	private boolean running;

	/**
	 * Creates a new listener that modifies given camera.
	 * 
	 * @param camera
	 *            camera to modify.
	 */
	public CameraMouseListener(GameCamera camera) {
		this.camera = camera;
		running = true;
		Thread t = new Thread(new CameraMover());
		t.start();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getX() < (double) camera.width * 0.1) {
			dx = -1;
		} else if (e.getX() > (double) camera.width * 0.9) {
			dx = 1;
		} else
			dx = 0;
		if (e.getY() < (double) camera.height * 0.1) {
			dy = -1;
		} else if (e.getY() > (double) camera.height * .9) {
			dy = 1;
		} else
			dy = 0;
		System.out.println("x=" + e.getX() + " y=" + e.getY());
	}

	/**
	 * Stops camera updating
	 */
	public void stop() {
		running = false;
	}

	private class CameraMover implements Runnable {

		@Override
		public void run() {
			while (running) {
				camera.x += 3 * dx;
				camera.y += 3 * dy;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {

				}
			}
		}
	}
}
