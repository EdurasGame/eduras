package de.illonis.eduras.gameobjects;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * This {@link TimedEventHandler} only waits for the interval to elapse once.
 * After it has elapsed, it automatically deregisters from the timingsource.
 * 
 * @author Florian 'Ren' Mai
 * 
 */
public abstract class OneTimeTimedEventHandler implements TimedEventHandler {
	private final static Logger L = EduLog
			.getLoggerFor(OneTimeTimedEventHandler.class.getName());

	private TimingSource timingSource;

	/**
	 * Create a OneTimeTimedEventHandler.
	 * 
	 * @param timingSource
	 *            the timing source to register at.
	 */
	public OneTimeTimedEventHandler(TimingSource timingSource) {
		this.timingSource = timingSource;
		timingSource.addTimedEventHandler(this);
	}

	@Override
	public void onIntervalElapsed(long delta) {
		intervalElapsed();
		timingSource.removeTimedEventHandler(this);
	}

	/**
	 * Called when the interval has elapsed for the first time.
	 */
	public abstract void intervalElapsed();
}
