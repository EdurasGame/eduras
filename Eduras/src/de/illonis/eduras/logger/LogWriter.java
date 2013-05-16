package de.illonis.eduras.logger;

import java.io.File;

/**
 * A module that features logging to file.
 * 
 * @author illonis
 * 
 */
public class LogWriter {

	private File logFile;

	public LogWriter() {

	}

	public void append(LogEntry entry) {
	}

	/**
	 * @return the logFile
	 */
	public File getLogFile() {
		return logFile;
	}

	/**
	 * @param logFile
	 *            the logFile to set
	 */
	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

}
