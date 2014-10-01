package de.illonis.eduras.exceptions;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class catches uncaught exceptions like runtime exceptions and logs them
 * before shutting down.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 *
 */
public class EdurasUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private Logger logger;

	/**
	 * Create a new ExceptionHandler.
	 * 
	 * @param l
	 *            the logger to print the exception to
	 */
	public EdurasUncaughtExceptionHandler(Logger l) {
		logger = l;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.log(Level.SEVERE, "Uncaught exception in thread '" + t.getName()
				+ "'.", e);
	}
}
