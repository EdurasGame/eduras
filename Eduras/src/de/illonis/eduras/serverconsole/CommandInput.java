package de.illonis.eduras.serverconsole;

import de.illonis.eduras.serverconsole.commands.ConsoleCommand;

/**
 * A parsed command line input.
 * 
 * @author illonis
 * 
 */
public class CommandInput {

	private final ConsoleCommand command;
	private final String[] args;

	/**
	 * Creates a {@link CommandInput} object.
	 * 
	 * @param command
	 *            the command called.
	 * @param args
	 *            the arguments (first argument is command itself).
	 */
	public CommandInput(ConsoleCommand command, String[] args) {
		this.command = command;
		this.args = args;
	}

	/**
	 * @return a list of arguments passed.
	 */
	public String[] getArgs() {
		return args;
	}

	/**
	 * @return the console command called.
	 */
	public ConsoleCommand getCommand() {
		return command;
	}
}
