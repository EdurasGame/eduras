package de.illonis.eduras.logger;

public interface LogListener {

	/**
	 * Fired when a new log entry was added to log.
	 * 
	 * @param entry
	 *            added log entry.
	 */
	void onNewLogEntry(LogEntry entry);

	/**
	 * Fired when log output type is changed.
	 * 
	 * @param newType
	 *            new output type.
	 */
	void logOutputTypeChanged(int newType);

	/**
	 * Fired when a new class was found while logging.
	 */
	void logClassAdded(String className);
}
