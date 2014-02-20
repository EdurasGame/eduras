package de.illonis.eduras.serverconsole;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.commands.CommandInitializer;
import de.illonis.eduras.serverconsole.commands.ConsoleCommand;
import de.illonis.eduras.serverconsole.remote.EdurasRemoteNetworkEventHandler;
import de.illonis.eduras.serverconsole.remote.RemoteConsoleServer;

/**
 * Provides a command line interface on server to make administrator able to run
 * some commands at runtime.<br>
 * Command line cannot be left unless a command is registerd that calls
 * {@link ServerConsole#stopCommandPrompt()}.<br>
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
	private Thread thread;
	private final ConsoleEventTriggerer triggerer;
	private RemoteConsoleServer rcs;
	private SystemConsole console;

	private final HashMap<String, ConsoleCommand> commands;
	private boolean running = false;

	/**
	 * Creates a new console that can be accessed to.
	 * 
	 * @param triggerer
	 *            the eventtriggerer.
	 */
	public ServerConsole(ConsoleEventTriggerer triggerer) {
		this.triggerer = triggerer;

		commands = new HashMap<String, ConsoleCommand>();
		// init help command
		commands.put(HELP_COMMAND, new ConsoleCommand(HELP_COMMAND,
				HELP_DESCRIPTION) {

			@Override
			public void onCommand(String[] args, ConsolePrinter printer,
					ConsoleEventTriggerer t) {
				listCommands(printer);
			}
		});

		CommandInitializer.initCommands(this);
	}

	/**
	 * Starts reading from command prompt.
	 * 
	 * @throws NoConsoleException
	 *             if there is no terminal available.
	 * @see #stopCommandPrompt()
	 */
	public void startCommandPrompt() throws NoConsoleException {
		if (!running) {
			console = new SystemConsole(triggerer);
			runMeAsync();
		}
	}

	/**
	 * Starts remote console server.
	 * 
	 * @param port
	 *            the port to listen on.
	 * 
	 * @param remotePassword
	 *            the password required to authenticate at the server.
	 * 
	 */
	public void startRemoteServer(int port, String remotePassword) {
		try {
			rcs = new RemoteConsoleServer(this, triggerer, remotePassword);
			rcs.setNetworkEventHandler(new EdurasRemoteNetworkEventHandler(rcs));
			rcs.start(port);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the remote server.
	 */
	public void stopRemoteServer() {
		if (rcs != null)
			rcs.stop();
	}

	private void runMeAsync() {
		thread = new Thread(this);
		thread.setName("ServerConsole");
		running = true;
		thread.start();
	}

	/**
	 * Stops server console. A stopped server can be started again. Has no
	 * effect if no console is running.
	 * 
	 * @see #startCommandPrompt()
	 */
	public void stopCommandPrompt() {
		if (thread != null) {
			running = false;
			thread.interrupt();
		}
	}

	/**
	 * Registers given command with this console. Command names are unique. That
	 * means, if a command with same name exists, it will be overwritten.
	 * 
	 * @param command
	 *            command that should be registered.
	 */
	public void registerCommand(ConsoleCommand command) {
		commands.put(command.getCommand(), command);
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
	public boolean commandExists(String cmd) throws NoConsoleException {
		return commands.containsKey(cmd);
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
			try {
				CommandInput input = parseCommand(command);
				input.getCommand().onCommand(input.getArgs(), console,
						triggerer);
			} catch (InvalidCommandException e) {
				console.println(e.getMessage());
			}
		}
	}

	/**
	 * Parses a command line input. Checks, if a command exists and has a valid
	 * number of arguments given, then executes it.
	 * 
	 * @param command
	 *            command to parse.
	 * @return the parsed command and arguments.
	 * @throws InvalidCommandException
	 */
	public CommandInput parseCommand(String command)
			throws InvalidCommandException {
		if (command.trim().isEmpty())
			throw new InvalidCommandException("Please provide a command.");
		L.fine("Received command: " + command);

		String[] args = command.split(" ");

		ConsoleCommand cmd = commands.get(args[0]);
		if (cmd == null) {
			throw new InvalidCommandException("Command not found: " + command);
		} else {
			if (cmd.argumentCountMatches(args.length - 1)) {
				return new CommandInput(cmd, args);
			} else {
				if (cmd.getMinimumArguments() == cmd.getMaximumArguments()) {
					throw new InvalidCommandException(
							String.format(
									"Invalid number of arguments given. Command '%s' requires exactly %d arguments.",
									args[0], cmd.getMaximumArguments()));
				} else {
					throw new InvalidCommandException(
							String.format(
									"Invalid number of arguments given. Command '%s' requires at least %d arguments, to a a maximum of %d arguments.",
									args[0], cmd.getMinimumArguments(),
									cmd.getMaximumArguments()));
				}
			}
		}
	}

	/**
	 * Lists all available commands.
	 */
	private void listCommands(ConsolePrinter printer) {
		for (ConsoleCommand command : commands.values()) {
			printer.println(command.getCommand() + " - "
					+ command.getDescription());
		}
	}
}