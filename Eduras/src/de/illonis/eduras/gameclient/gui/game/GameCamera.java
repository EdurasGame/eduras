package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * GameCamera stores where current viewport of player is.
 * 
 * @author illonis
 * 
 */
public class GameCamera extends Rectangle {

	private static final long serialVersionUID = 1L;
	private Vector2f cameraMovementKeys;
	private Vector2f cameraMovementMouse;
	private Vector2f cameraOffset;

	public GameCamera() {
		super(0, 0, 10, 10);
		reset();
	}

	/**
	 * Resets camera to original position.
	 * 
	 * @author illonis
	 */
	public void reset() {
		setLocation(0, 0);
		cameraMovementMouse = new Vector2f();
		cameraMovementKeys = new Vector2f();
		cameraOffset = new Vector2f();
	}

	/**
	 * @return the current camera movement per render-step.
	 */
	public Vector2f getCameraMovementMouse() {
		return cameraMovementMouse;
	}

	/**
	 * @return the current camera movement per render-step.
	 */
	public Vector2f getCameraMovementKeys() {
		return cameraMovementKeys;
	}

	/**
	 * @return the current camera offset.
	 */
	public Vector2f getCameraOffset() {
		return cameraOffset;
	}

	/**
	 * Centers camera to given point.
	 * 
	 * @param newX
	 *            x coordinate of new center.
	 * @param newY
	 *            y coordinate of new center.
	 */
	void centerAt(float newX, float newY) {
		setLocation(newX - width / 2, newY - height / 2);
	}
}
