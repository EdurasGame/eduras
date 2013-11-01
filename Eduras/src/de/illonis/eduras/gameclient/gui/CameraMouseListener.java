package de.illonis.eduras.gameclient.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Listens for mouse movement on gui to trigger camera movement.
 * 
 * @author illonis
 * 
 */
public class CameraMouseListener extends MouseAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(CameraMouseListener.class.getName());

	private static int CAMERA_SPEED = 6;
	private final static double SIDE_RANGE = 0.04;

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

	/**
	 * Starts camera mouse listener.
	 */
	public void start() {
		running = true;
		dx = dy = 0;
		Thread t = new Thread(new CameraMover());
		t.setName("CameraMover");
		t.start();
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
			L.info("Camera Mover Thread stopped.");
		}
	}
}