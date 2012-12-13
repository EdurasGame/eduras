package de.illonis.eduras.gameclient.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import de.illonis.eduras.gameclient.GameCamera;
import de.illonis.eduras.logger.EduLog;

/**
 * Listens for mouse movement on gui to trigger camera movement.
 * 
 * @author illonis
 * 
 */
public class CameraMouseListener extends MouseAdapter {

	private static int CAMERA_SPEED = 6;

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
		if (e.getX() < (double) camera.width * 0.15) {
			dx = -CAMERA_SPEED;
		} else if (e.getX() > (double) camera.width * 0.85) {
			dx = CAMERA_SPEED;
		} else
			dx = 0;
		if (e.getY() < (double) camera.height * 0.15) {
			dy = -CAMERA_SPEED;
		} else if (e.getY() > (double) camera.height * .85) {
			dy = CAMERA_SPEED;
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
			EduLog.info("Camera Mover Thread stopped.");
		}
	}
}
