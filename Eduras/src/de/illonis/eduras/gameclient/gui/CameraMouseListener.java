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
	private final static double SIDE_RANGE = 0.15;

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
		dx = dy = 0;
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
		if (e.getX() < (double) camera.width * SIDE_RANGE) {
			dx = -CAMERA_SPEED;
		} else if (e.getX() > (double) camera.width * (1 - SIDE_RANGE)) {
			dx = CAMERA_SPEED;
		} else
			dx = 0;
		if (e.getY() < (double) camera.height * SIDE_RANGE) {
			dy = -CAMERA_SPEED;
		} else if (e.getY() > (double) camera.height * (1 - SIDE_RANGE)) {
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
				if (inGui) {
					camera.x += dx;
					camera.y += dy;
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {

				}
			}
			EduLog.info("Camera Mover Thread stopped.");
		}
	}
}