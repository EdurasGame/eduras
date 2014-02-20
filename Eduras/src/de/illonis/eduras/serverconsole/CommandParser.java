package de.illonis.eduras.serverconsole;

import java.util.HashMap;

import de.illonis.eduras.serverconsole.commands.ConsoleCommand;

/**
 * A command parser.
 * 
 * @author illonis
 * 
 */
public class CommandParser {

	private final HashMap<String, ConsoleCommand> commands;

	CommandParser(HashMap<String, ConsoleCommand> commands) {
		this.commands = commands;
	}

	/**
	 * Parses a command line input. Checks, if a command exists and has a valid
	 * number of arguments given, then executes it.
	 * 
	 * @param command
	 *            command to parse.
	 * @return the parsed command and arguments.
	 * @throws InvalidCommandException
	 *             if input was invalid.
	 */
	public CommandInput parseCommand(String command)
			throws InvalidCommandException {
		if (command.trim().isEmpty())
			throw new InvalidCommandException("Please provide a command.");

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
}
