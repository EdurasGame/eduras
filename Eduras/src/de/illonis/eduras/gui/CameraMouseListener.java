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
	private boolean inGui;

	/**
	 * Creates a new listener that modifies given camera.
	 * 
	 * @param camera
	 *            camera to modify.
	 */
	public CameraMouseListener(GameCamera camera) {
		inGui = true;
		this.camera = camera;
		running = true;
		Thread t = new Thread(new CameraMover());
		t.start();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		inGui = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		inGui = false;
		dx = 0;
		dy = 0;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!inGui)
			return;
		if (e.getX() < (double) camera.width * 0.1) {
			dx = -3;
		} else if (e.getX() > (double) camera.width * 0.9) {
			dx = 3;
		} else
			dx = 0;
		if (e.getY() < (double) camera.height * 0.1) {
			dy = -3;
		} else if (e.getY() > (double) camera.height * .9) {
			dy = 3;
		} else
			dy = 0;
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
				camera.x += dx;
				camera.y += dy;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {

				}
			}
		}
	}
}
