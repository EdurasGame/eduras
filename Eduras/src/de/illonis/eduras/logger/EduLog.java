package de.illonis.eduras.logger;

import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * Provides logging features for Eduras?. Logging results are available via
 * different access methods. For available logging outputs, see {@link LogMode}.<br>
 * You will never have to instantiate this class in order to use it. Instead,
 * use its logging methods (e.g. {@link #error(String)}).
 * 
 * @author illonis
 * 
 */
public final class EduLog {

	private static EduLog instance;
	private Date startDate;
	private int outputMode = LogMode.CONSOLE.getId();
	private int trackSize = 0;
	private HashSet<String> classlist;
	private ArrayList<LogEntry> logdata;
	private LinkedList<LogListener> listeners;
	private Level logLimit;

	/**
	 * Logging modes specify the way logging data is saved or displayed. Can be
	 * multiple values at once.
	 * 
	 * @see EduLog#setLogOutput(LogMode)
	 * @author illonis
	 * 
	 */
	public static enum LogMode {
		CONSOLE(0x100), GUI(0x1), FILE(0x10), NONE(0x0);

		private int id;

		private LogMode(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	/**
	 * Returns current instance of {@link EduLog}.
	 * 
	 * @return instance of logger.
	 */
	static EduLog getInstance() {
		if (instance == null)
			instance = new EduLog();
		return instance;
	}

	/**
	 * initializes a new logger.
	 */
	private EduLog() {
		startDate = new Date();
		setLimit(Level.WARNING);
		classlist = new HashSet<String>();
		logdata = new ArrayList<LogEntry>();
		listeners = new LinkedList<LogListener>();
	}

	/**
	 * Sets logging output to given targets. You can use various outputs at the
	 * same time by giving multiple values (e.g.
	 * <code>setLogOutput(GUI, FILE)</code>). To disable logging, use
	 * {@link LogMode#NONE}.<br>
	 * <br>
	 * <b>Notes:</b>
	 * <ul>
	 * <li>New logging output will only apply for messages send after mode is
	 * assigned.</li>
	 * <li>Logging will be disabled if any of given parameters is
	 * {@link LogMode#NONE} even if more parameters are given.</li>
	 * </ul>
	 * 
	 * @see LogMode
	 * 
	 * @param modes
	 *            new output targets.
	 */
	public static void setLogOutput(LogMode... modes) {
		getInstance().setMode(modes);
	}

	/**
	 * Limits logging to prevent less severe errors from display. This disables
	 * log reporting for all messages with lower severeness than given level.<br>
	 * For example, if you pass {@link Level#WARNING}, messages with
	 * {@link Level#INFO} and {@link Level#FINE} will not be logged.<br>
	 * <b>Note:</b> This does not filter displayed messages but will rather
	 * don't even track them. So by resetting limit to lower values, you won't
	 * see lower messages logged before.
	 * 
	 * @param level
	 *            level limit. Messages below will not be logged.
	 */
	public static void setLogLimit(Level level) {
		setLogLimit(level);
	}

	/**
	 * @see #setLogLimit(Level)
	 * @param level
	 *            level limit.
	 */
	private void setLimit(Level level) {
		logLimit = level;
	}

	/**
	 * Sets maximum track size to given value. If trackSize is 0, full track is
	 * displayed. Track size specifies, how many steps will be taken to create
	 * stacktrace.
	 * 
	 * @param trackSize
	 *            new track size
	 */
	public static void setTrackDetail(int trackSize) {
		getInstance().setTrackSize(trackSize);
	}

	/**
	 * @see #setTrackDetail(int)
	 * @param trackSize
	 */
	private void setTrackSize(int trackSize) {
		if (trackSize < 0)
			trackSize = 0;
		this.trackSize = trackSize;
	}

	/**
	 * @see EduLog#setLogOutput(LogMode...)
	 * @param modes
	 *            new output targets.
	 */
	private void setMode(LogMode... modes) {
		int newMode = 0;
		for (LogMode mode : modes) {
			if (mode == LogMode.NONE) {
				newMode = LogMode.NONE.getId();
				break;
			}
			if (mode == LogMode.GUI && GraphicsEnvironment.isHeadless())
				throw new HeadlessException();
			newMode |= mode.getId();
		}
		outputMode = newMode;
		broadcastOutputChanged();
	}

	/**
	 * Builds the stacktrace. We need to abandon the first four entries of stack
	 * trace due to internal logger structure.
	 * 
	 * @return the stacktrace.
	 */
	private StackTraceElement[] getStackTrace() {
		StackTraceElement[] s = new Throwable().fillInStackTrace()
				.getStackTrace();
		int start = (trackSize > 0) ? Math.max(4, s.length - 4 - trackSize) : 4;
		System.out.println(s.length);
		System.out.println(start);
		StackTraceElement[] newElements = new StackTraceElement[s.length
				- start];

		for (int i = start; i < s.length; i++) {
			newElements[i - start] = s[i];
			if (classlist.add(s[i].getClassName()))
				broadcastClassAdded(s[i].getClassName());
		}
		return newElements;
	}

	/**
	 * Appends given message to log and adds its stacktrace. This methods pushes
	 * given entry to specified log outputs.
	 * 
	 * @see #append(LogEntry)
	 * 
	 * @param level
	 *            message level.
	 * @param s
	 *            message string.
	 */
	private void append(Level level, String s) {
		if (outputMode == LogMode.NONE.getId())
			return;
		LogEntry entry = new LogEntry(level, s, getStackTrace());
		append(entry);
	}

	/**
	 * Appends given log entry to log. This methods pushes given entry to all
	 * specified log outputs.
	 * 
	 * @see #append(Level, String)
	 * 
	 * @param entry
	 *            log entry to add.
	 */
	private void append(LogEntry entry) {
		if (entry.getLevel().intValue() < logLimit.intValue())
			return;
		if (outputMode == LogMode.NONE.getId())
			return;

		// gui
		if (printsOn(LogMode.GUI)) {
			logdata.add(entry);
		}

		// file
		if (printsOn(LogMode.FILE)) {
			getLogWriter().append(entry);
		}

		// console
		if (printsOn(LogMode.CONSOLE)) {
			if (entry.getLevel() == Level.SEVERE)
				System.err.println(entry.toFullString());
			else
				System.out.println(entry.toFullString());
		}
		broadcastNewEntry(entry);
	}

	/**
	 * Returns current logwriter.
	 * 
	 * @return current logwriter.
	 */
	private LogWriter getLogWriter() {
		return null;
	}

	/**
	 * Adds given message with a specified severity to log. A stacktrace is
	 * added to message automatically.
	 * 
	 * @see Level
	 * @see #error(String)
	 * @see #warning(String)
	 * @see #info(String)
	 * @see #fine(String)
	 * 
	 * @param level
	 *            error level.
	 * @param s
	 *            message.
	 */
	public static void log(Level level, String s) {
		getInstance().append(level, s);
	}

	/**
	 * Adds given error message to log. A stacktrace is added to message
	 * automatically.
	 * 
	 * @see Level#SEVERE
	 * @see #log(Level, String)
	 * 
	 * @param s
	 *            message.
	 */
	public static void error(String s) {
		log(Level.SEVERE, s);
	}

	/**
	 * Adds given warning message to log. A stacktrace is added to message
	 * automatically.
	 * 
	 * @see Level#WARNING
	 * @see #log(Level, String)
	 * 
	 * @param s
	 *            message.
	 */
	public static void warning(String s) {
		log(Level.SEVERE, s);
	}

	/**
	 * Adds given info message to log. A stacktrace is added to message
	 * automatically.
	 * 
	 * @see Level#INFO
	 * @see #log(Level, String)
	 * 
	 * @param s
	 *            message.
	 */
	public static void info(String s) {
		log(Level.INFO, s);
	}

	/**
	 * Adds given fine message to log. A stacktrace is added to message
	 * automatically.
	 * 
	 * @see Level#FINE
	 * @see #log(Level, String)
	 * 
	 * @param s
	 *            message.
	 */
	public static void fine(String s) {
		log(Level.FINE, s);
	}

	/**
	 * Passes an exception to logger so it will be logged.
	 * 
	 * @param e
	 *            exception to log.
	 */
	public static void passException(Exception e) {
		LogEntry entry = new LogEntry(Level.SEVERE, e.getMessage(),
				e.getStackTrace());
		getInstance().append(entry);
	}

	/**
	 * Prints a message to standard console.
	 * 
	 * @param s
	 *            message string.
	 */
	public static void print(String s) {
		System.out.println(s);
	}

	/**
	 * Prints a message to error console.
	 * 
	 * @param s
	 *            message string.
	 */
	public static void printError(String s) {
		System.err.println(s);
	}

	/**
	 * Returns true if logger prints on given logging mode.
	 * 
	 * @param mode
	 *            mode to test.
	 * @return true if logger prints on given logging mode, false otherwise.
	 */
	public static boolean printsOn(LogMode mode) {
		return (mode.getId() & getInstance().getOutputMode()) > 0;
	}

	/**
	 * Returns current output mode.
	 * 
	 * @return current output mode.
	 */
	int getOutputMode() {
		return outputMode;
	}

	/**
	 * Returns all logdata.
	 * 
	 * @return logdata.
	 */
	ArrayList<LogEntry> getLogdata() {
		return logdata;
	}

	/**
	 * Broadcasts a new entry to listeners.
	 * 
	 * @param e
	 *            new log entry.
	 */
	private void broadcastNewEntry(LogEntry e) {
		for (LogListener l : listeners) {
			l.onNewLogEntry(e);
		}
	}

	/**
	 * Notify all listeners that the broadcast output mode changed.
	 */
	private void broadcastOutputChanged() {
		for (LogListener l : listeners) {
			l.logOutputTypeChanged(outputMode);
		}
		info("[LOGGER] Changed output mode to " + outputMode);
	}

	/**
	 * Notify all listeners that a new class has been recognized.
	 * 
	 * @param c
	 *            new class.
	 */
	private void broadcastClassAdded(String c) {
		for (LogListener l : listeners) {
			l.logClassAdded(c);
		}
	}

	/**
	 * Adds a log listener to logger.
	 * 
	 * @param l
	 *            listener to add.
	 */
	void addLogListener(LogListener l) {
		listeners.add(l);
	}

	/**
	 * Removes a log listener from logger.
	 * 
	 * @param l
	 *            listener to remove.
	 */
	void removeLogListener(LogListener l) {
		listeners.remove(l);
	}

	/**
	 * Returns timestamp when logging started.
	 * 
	 * @return logging start.
	 */
	Date getStartDate() {
		return startDate;
	}

	/**
	 * Returns current logging limit.
	 * 
	 * @return current logging limit.
	 */
	Level getLogLimit() {
		return logLimit;
	}

	/**
	 * Returns a list of all classes that occured while listening.
	 * 
	 * @return list of all classes.
	 */
	HashSet<String> getClasslist() {
		return classlist;
	}
}
