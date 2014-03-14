package de.illonis.eduras.gameobjects;

/**
 * Receives notifications in constant steps of time.<br>
 * When registered to a {@link TimingSource}, the
 * {@link #onIntervalElapsed(long)} method is called periodically in steps of
 * {@link #getInterval()}.
 * 
 * @author illonis
 * 
 */
public interface TimedEventHandler {

	/**
	 * Returns the interval this element should be notified at.
	 * 
	 * @return interval in ms.
	 */
	long getInterval();

	/**
	 * Called each time an interval is elapsed.<br>
	 * The first call is received when one interval has passed either after
	 * adding this handler to a {@link TimingSource} or after the source has
	 * been started.
	 * 
	 * @param delta
	 *            exact time elapsed since last notification in ms.
	 */
	void onIntervalElapsed(long delta);

}
