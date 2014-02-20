package de.illonis.eduras.serverconsole;

import java.io.Console;
import java.util.Formatter;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.commands.ConsoleCommand;

/**
 * Provides a command line interface on server to make administrator able to run
 * some commands at runtime.<br>
 * Command line cannot be left unless a command is registerd that calls
 * {@link ServerConsole#stop()}.<br>
 * By default, a <code>help</code>-command is implemented that echoes a list of
 * available commands. You can override this command by registering a new
 * command with the same command name.<br>
 * <i>Note that this feature is not available when debugging/running in
 * eclipse.</i>
 * 
 * @author illonis
 * 
 */
public class ServerConsole implements Runnable {

	private final static Logger L = EduLog.getLoggerFor(ServerConsole.class
			.getName());

	private final static String CMD_PROMPT = "eduras>";
	private final static String HELP_COMMAND = "help";
	private final static String HELP_DESCRIPTION = "Prints all available commands";
	private static ServerConsole instance;
	private Thread thread;
	private ConsoleEventTriggerer triggerer;

	private final HashMap<String, ConsoleCommand> commands;
	private final Console console;
	private boolean running = false;

	/**
	 * Sets event triggerer to given triggerer.
	 * 
	 * @param triggerer
	 *            new triggerer.
	 */
	public static void setEventTriggerer(ConsoleEventTriggerer triggerer) {
		try {
			getInstance().triggerer = triggerer;
		} catch (NoConsoleException e) {
			L.log(Level.SEVERE, "no console found", e);
		}
	}

	/**
	 * Creates a new console that can be accessed to.
	 * 
	 * @throws NoConsoleException
	 *             when no command line is available.
	 */
	private ServerConsole() throws NoConsoleException {
		if (!exists())
			throw new NoConsoleException();
		console = System.console();
		commands = new HashMap<String, ConsoleCommand>();
		// init help command
		commands.put(HELP_COMMAND, new ConsoleCommand(HELP_COMMAND,
				HELP_DESCRIPTION) {

			@Override
			public void onCommand(String[] args, ServerConsole c,
					ConsoleEventTriggerer t) {
				listCommands();
			}
		});
		instance = this;
	}

	/**
	 * Starts server console. Has no effect if a console is already running.
	 * 
	 * @see #stop()
	 * @throws NoConsoleException
	 *             when there is no command line available.
	 */
	public static void start() throws NoConsoleException {
		if (!isRunning())
			getInstance().runMeAsync();
	}

	private void runMeAsync() {
		thread = new Thread(this);
		thread.setName("ServerConsole");
		running = true;
		thread.start();
	}

	private void stopMe() {
		if (thread != null) {
			running = false;
			thread.interrupt();
		}
	}

	/**
	 * Checks if a console exists where server console can be displayed. If a
	 * console exists, commands can be added without throwing an exception.
	 * 
	 * @return true if a console exists.
	 */
	public static boolean exists() {
		return (System.console() != null);
	}

	private static ServerConsole getInstance() throws NoConsoleException {
		if (instance == null) {
			new ServerConsole();
		}
		return instance;
	}

	/**
	 * Checks whether a server console instance is running.
	 * 
	 * @return true if a server console is running, false otherwise.
	 */
	public static boolean isRunning() {
		if (instance == null)
			return false;
		return instance.running;
	}

	/**
	 * Stops server console. A stopped server can be started again. Has no
	 * effect if no console is running.
	 * 
	 * @throws NoConsoleException
	 *             if no console available.
	 * 
	 * @see #start()
	 */
	public static void stop() throws NoConsoleException {
		getInstance().stopMe();
	}

	/**
	 * Registers given command with this console. Command names are unique. That
	 * means, if a command with same name exists, it will be overwritten.
	 * 
	 * @param command
	 *            command that should be registered.
	 */
	public static void registerCommand(ConsoleCommand command) {
		try {
			getInstance().commands.put(command.getCommand(), command);
		} catch (NoConsoleException e) {
			L.log(Level.SEVERE, "error registering command", e);
		}
	}

	/**
	 * Checks if given command already exists.
	 * 
	 * @param cmd
	 *            command name.
	 * @return true if command exists, false otherwise.
	 * @throws NoConsoleException
	 *             when no command line is available.
	 */
	public static boolean commandExists(String cmd) throws NoConsoleException {
		return getInstance().commands.containsKey(cmd);
	}

	@Override
	public void run() {
		readFromConsole();
	}

	/**
	 * Reads from console repeatedly.
	 */
	private void readFromConsole() {
		String command;
		while (running) {
			command = console.readLine(CMD_PROMPT);
			if (command == null) {
				L.warning("End of stream reached in console. Closing console.");
				return;
			}
			parseCommand(command);
		}
	}

	/**
	 * Parses a command line input. Checks, if a command exists and has a valid
	 * number of arguments given, then executes it.
	 * 
	 * @param command
	 *            command to parse.
	 */
	private void parseCommand(String command) {
		if (command.trim().isEmpty())
			return;
		L.fine("Received command: " + command);

		String[] args = command.split(" ");

		ConsoleCommand cmd = commands.get(args[0]);
		if (cmd == null) {
			println("Command not found: " + command);
		} else {
			if (cmd.argumentCountMatches(args.length - 1)) {
				cmd.onCommand(args, this, triggerer);
			} else {
				if (cmd.getMinimumArguments() == cmd.getMaximumArguments()) {
					printlnf(
							"Invalid number of arguments given. Command '%s' requires exactly %d arguments.",
							args[0], cmd.getMaximumArguments());
				} else {
					printlnf(
							"Invalid number of arguments given. Command '%s' requires at least %d arguments, to a a maximum of %d arguments.",
							args[0], cmd.getMinimumArguments(),
							cmd.getMaximumArguments());
				}
			}
		}
	}

	/**
	 * Prints given (formatted) string to current server console.<br>
	 * <i>Note: This method neither throws an exception nor prints anything if a
	 * command line is available but no server console running. Use
	 * {@link EduLog} for that.</i>
	 * 
	 * @see Formatter
	 * 
	 * @param s
	 *            A (format) string that should be printed.
	 * 
	 * @param args
	 *            Format arguments.
	 * 
	 * @throws IllegalFormatException
	 *             If a format string contains an illegal syntax, a format
	 *             specifier that is incompatible with the given arguments,
	 *             insufficient arguments given the format string, or other
	 *             illegal conditions. For specification of all possible
	 *             formatting errors, see the detail section of
	 *             {@link Formatter} class specification.
	 */
	public void printlnf(String s, Object... args) {
		if (exists()) {
			System.out.printf(s + "\n", args);
		}
	}

	/**
	 * Prints given text line to console.
	 * 
	 * @param text
	 *            text to print.
	 */
	public void println(String text) {
		if (exists())
			System.out.println(text);
	}

	/**
	 * Lists all available commands.
	 */
	private void listCommands() {
		for (ConsoleCommand command : commands.values()) {
			println(command.getCommand() + " - " + command.getDescription());
		}
	}
}