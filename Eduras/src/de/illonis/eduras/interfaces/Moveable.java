package de.illonis.eduras.interfaces;

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
	 */
	void onMove(long delta);

	/**
	 * Indicate that object leaves map with current move.
	 */
	void onMapBoundsReached();

}
