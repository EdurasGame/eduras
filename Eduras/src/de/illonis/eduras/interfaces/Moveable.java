package de.illonis.eduras.interfaces;

import de.illonis.eduras.math.ShapeGeometry;

/**
 * A moveable object.
 * 
 * @author illonis
 * 
 */
public interface Moveable {

	/**
	 * Indicates an object move.<br>
	 * To calculate distance we need the elapsed time.
	 * 
	 * @param delta
	 *            elapsed time in milliseconds.
	 * @param geometry
	 *            the geometry to use for collision detection.
	 */
	void onMove(long delta, ShapeGeometry geometry);

	/**
	 * This method is called when an object rotates.
	 * 
	 * @param rotationAngle
	 *            The absolute angle an object tries to rotate to.
	 * @param geometry
	 *            the geometry to use for collision detection.
	 */
	void onRotate(float rotationAngle, ShapeGeometry geometry);

}
