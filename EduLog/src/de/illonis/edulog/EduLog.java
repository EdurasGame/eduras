package de.illonis.edulog;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides logging features for Eduras?.<br>
 * For logging, {@link java.util.logging.Logger} is used. This class handles
 * initialization and structuration. By default, this logger supports logging to
 * console and file.<br>
 * The logging system has to be initialized once at startup by calling
 * {@link #init()}. You may pass a filename if you do not want to use the
 * default one ({@value #DEFAULT_LOG_FILE}).<br>
 * Logging thresholds can be adjusted using {@link #setBasicLogLimit(Level)},
 * {@link #setConsoleLogLimit(Level)} and {@link #setFileLogLimit(Level)}. Set
 * one of the limits to {@link Level#OFF} to disable that logging.
 * 
 * @author illonis
 * 
 */
public final class EduLog {

	public final static int VERSION = 2;

	private final static String DEFAULT_LOG_FILE = "logfile.txt";
	private static Level logLimit = Level.WARNING;
	private static long startTime = 0;

	private static FileHandler fileTxt;
	private static ConsoleHandler consoleHandler;
	private static LogFormatter formatterTxt;

	/**
	 * Initializes EduLog with given logfile. By default, logging threshold is
	 * set to WARNING.
	 * 
	 * @param logFileName
	 *            the name of the logfile.
	 * @throws IOException
	 *             if creation of logfile failed.
	 */
	public static void init(String logFileName) throws IOException {
		startTime = System.currentTimeMillis();
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		Handler[] handlers = logger.getHandlers();
		for (Handler handler : handlers) {
			logger.removeHandler(handler);
		}
		consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new LogFormatter());
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
		fileTxt = new FileHandler(logFileName, 8096, 1, true);
		// create txt Formatter
		formatterTxt = new LogFormatter();
		fileTxt.setFormatter(formatterTxt);
		fileTxt.setLevel(Level.ALL);
		logger.addHandler(fileTxt);
		logger.info("Logging started.");
		setBasicLogLimit(Level.WARNING);
		setFileLogLimit(Level.WARNING);
		setConsoleLogLimit(Level.WARNING);
	}

	static long getStartTime() {
		return startTime;
	}

	/**
	 * Initializes EduLog using default logfile. By default, logging threshold
	 * is set to WARNING.
	 * 
	 * @throws IOException
	 *             if creation of logfile failed.
	 * @see #init(String)
	 */
	public static void init() throws IOException {
		init(DEFAULT_LOG_FILE);
	}

	/**
	 * Limits logging to prevent less severe errors from being displayed. This
	 * disables log reporting for all messages with lower severeness than given
	 * level.<br>
	 * For example, if you pass {@link Level#WARNING}, messages with
	 * {@link Level#INFO} and {@link Level#FINE} will not be logged.<br>
	 * <b>Note:</b> Setting this to a higher level than console / file logging
	 * prevents lower messages to be logged.
	 * 
	 * @param limit
	 *            level limit. Messages below will not be logged.
	 */
	public static void setBasicLogLimit(Level limit) {
		logLimit = limit;
		Logger.getLogger("").setLevel(limit);
	}

	/**
	 * Limits logging to file to given logging level.
	 * 
	 * @param limit
	 *            new logging threshold.
	 */
	public static void setFileLogLimit(Level limit) {
		fileTxt.setLevel(limit);
	}

	/**
	 * Limits logging to console to given logging level.
	 * 
	 * @param limit
	 *            new logging threshold.
	 */
	public static void setConsoleLogLimit(Level limit) {
		consoleHandler.setLevel(limit);
	}

	/**
	 * Retrieves the logger for given class.
	 * 
	 * @param className
	 *            name of logging class.
	 * @return the logger.
	 */
	public static Logger getLoggerFor(String className) {
		Logger logger = Logger.getLogger(className);
		logger.setLevel(logLimit);
		return logger;
	}

	// =============== old ===========
	/**
	 * Logging modes specify the way logging data is saved or displayed. Can be
	 * multiple values at once.
	 * 
	 * @see EduLog#setLogOutput(LogMode...)
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	@Deprecated
	public static enum LogMode {
		CONSOLE(0x100), GUI(0x1), FILE(0x10), NONE(0x0);

		private int id;

		private LogMode(int id) {
			this.id = id;
		}

		/**
		 * Returns the id of this log mode.
		 * 
		 * @return the id.
		 */
		public int getId() {
			return id;
		}
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
	@Deprecated
	public static void setLogOutput(LogMode... modes) {
	}

	/**
	 * Sets maximum track size to given value. If trackSize is 0, full track is
	 * displayed. Track size specifies, how many steps will be taken to create
	 * stacktrace.
	 * 
	 * @param trackSize
	 *            new track size
	 */
	@Deprecated
	public static void setTrackDetail(int trackSize) {
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
	@Deprecated
	public static void log(Level level, String s) {

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
	@Deprecated
	public static void error(String s) {
		// log(Level.SEVERE, s);
	}

	/**
	 * Adds given localized error message to log. A stacktrace is added to
	 * message automatically.
	 * 
	 * @param localeKey
	 *            the locale key. This method automatically uses this key to
	 *            obtain the localized message.
	 * @see #error(String)
	 * @see #errorLF(String, Object...)
	 * @see Localization#getString(String)
	 * 
	 * @author illonis
	 */
	@Deprecated
	public static void errorL(String localeKey) {
		// error(Localization.getString(localeKey));
	}

	/**
	 * Adds given localized and parameterized error message to log. A stacktrace
	 * is added to message automatically.
	 * 
	 * @param localeKey
	 *            the locale key. This method automatically uses this key to
	 *            obtain the localized message.
	 * @param args
	 *            formatting arguments (see
	 *            {@link Localization#getStringF(String, Object...)}
	 * 
	 * @see #error(String)
	 * @see #errorL(String)
	 * @see Localization#getStringF(String, Object...)
	 * 
	 * @author illonis
	 */
	@Deprecated
	public static void errorLF(String localeKey, Object... args) {
		// error(Localization.getStringF(localeKey, args));
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
	@Deprecated
	public static void warning(String s) {
		log(Level.WARNING, s);
	}

	/**
	 * Adds given localized warning message to log. A stacktrace is added to
	 * message automatically.
	 * 
	 * @param localeKey
	 *            the locale key. This method automatically uses this key to
	 *            obtain the localized message.
	 * @see #warning(String)
	 * @see #warningLF(String, Object...)
	 * @see Localization#getString(String)
	 * 
	 * @author illonis
	 */
	@Deprecated
	public static void warningL(String localeKey) {
		// warning(Localization.getString(localeKey));
	}

	/**
	 * Adds given localized and parameterized warning message to log. A
	 * stacktrace is added to message automatically.
	 * 
	 * @param localeKey
	 *            the locale key. This method automatically uses this key to
	 *            obtain the localized message.
	 * @param args
	 *            formatting arguments (see
	 *            {@link Localization#getStringF(String, Object...)}
	 * 
	 * @see #warning(String)
	 * @see #warningL(String)
	 * @see Localization#getStringF(String, Object...)
	 * 
	 * @author illonis
	 */
	@Deprecated
	public static void warningLF(String localeKey, Object... args) {
		// warning(Localization.getStringF(localeKey, args));
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
	@Deprecated
	public static void info(String s) {
		log(Level.INFO, s);
	}

	/**
	 * Adds given localized info message to log. A stacktrace is added to
	 * message automatically.
	 * 
	 * @param localeKey
	 *            the locale key. This method automatically uses this key to
	 *            obtain the localized message.
	 * @see #info(String)
	 * @see #infoLF(String, Object...)
	 * @see Localization#getString(String)
	 * 
	 * @author illonis
	 */
	@Deprecated
	public static void infoL(String localeKey) {
		// info(Localization.getString(localeKey));
	}

	/**
	 * Adds given localized and parameterized info message to log. A stacktrace
	 * is added to message automatically.
	 * 
	 * @param localeKey
	 *            the locale key. This method automatically uses this key to
	 *            obtain the localized message.
	 * @param args
	 *            formatting arguments (see
	 *            {@link Localization#getStringF(String, Object...)}
	 * 
	 * @see #info(String)
	 * @see #infoL(String)
	 * @see Localization#getStringF(String, Object...)
	 * 
	 * @author illonis
	 */
	@Deprecated
	public static void infoLF(String localeKey, Object... args) {
		// info(Localization.getStringF(localeKey, args));
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
	@Deprecated
	public static void fine(String s) {
		// log(Level.FINE, s);
	}

	/**
	 * Passes an exception to logger so it will be logged.
	 * 
	 * @param e
	 *            exception to log.
	 */
	@Deprecated
	public static void passException(Exception e) {
		e.printStackTrace();
	}

	/**
	 * Prints a message to standard console.
	 * 
	 * @param s
	 *            message string.
	 */
	@Deprecated
	public static void print(String s) {
		System.out.println(s);
	}

	/**
	 * Prints a message to error console.
	 * 
	 * @param s
	 *            message string.
	 */
	@Deprecated
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
	@Deprecated
	public static boolean printsOn(LogMode mode) {
		return false;
	}

}
