package de.illonis.eduras.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * A log entry.
 * 
 * @author illonis
 * 
 */
public class LogEntry {

	private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	private Date logTime;
	private Level level;
	private String message;
	private StackTraceElement[] stackTrace;

	/**
	 * Creates a new log entry with given parameters.
	 * 
	 * @param level
	 *            message level.
	 * @param s
	 *            message.
	 * @param stackTraceElements
	 *            stack trace.
	 */
	public LogEntry(Level level, String s,
			StackTraceElement[] stackTraceElements) {
		this.level = level;
		this.message = s;
		this.stackTrace = stackTraceElements;
		logTime = new Date();
	}

	/**
	 * Returns {@link Level} of log entry. The level indicates the severeness of
	 * the message.
	 * 
	 * @return message level.
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Returns message of logentry.
	 * 
	 * @return message of logentry
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the stack trace.
	 * 
	 * @return stack trace.
	 */
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public String toFullString() {
		StringBuilder b = new StringBuilder(toString());
		b.append("\n");
		int i = 1;
		for (StackTraceElement trace : stackTrace) {
			b.append(trace);
			b.append("\n");
			for (int n = 0; n < i; n++)
				b.append(" ");
			// b.append(" at (" + trace.getFileName() + ":"
			// + trace.getLineNumber() + ") in " + trace.getMethodName()
			// + "\n");
			i += 3;
		}
		return b.toString();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(format.format(logTime));
		b.append(" [" + getLevel().getName() + "] ");
		b.append(message);
		return b.toString();
	}

	/**
	 * Checks if entry contains a trace containing given classname.
	 * 
	 * @param className
	 *            class name to search for.
	 * @return true if log entry contains class name, false otherwise.
	 */
	public boolean containsClass(String className) {
		for (StackTraceElement ste : stackTrace) {
			if (ste.getClassName().equalsIgnoreCase(className))
				return true;
		}
		return false;
	}

	/**
	 * Returns time when entry was recored.
	 * 
	 * @return time event logged.
	 */
	public Date getLogTime() {
		return logTime;
	}
}
