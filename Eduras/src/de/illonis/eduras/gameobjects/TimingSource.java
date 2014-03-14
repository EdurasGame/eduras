package de.illonis.eduras.gameobjects;

/**
 * A timing source periodically notifies multiple {@link TimedEventHandler} at
 * their requested rate.
 * 
 * @author illonis
 * 
 */
public interface TimingSource {

	/**
	 * Adds an eventhandler so it receives notifications periodically.
	 * 
	 * @param eventHandler
	 *            the handler to add.
	 */
	void addTimedEventHandler(TimedEventHandler eventHandler);

	/**
	 * Removes an assigned handler from the handler list. A removed handler does
	 * not receive any notifications from this source anymore and is not
	 * notified of this removal.
	 * 
	 * @param eventHandler
	 *            the handler to remove.
	 */
	void removeTimedEventHandler(TimedEventHandler eventHandler);

	/**
	 * @return true when this source is active.
	 */
	boolean isRunning();

}
