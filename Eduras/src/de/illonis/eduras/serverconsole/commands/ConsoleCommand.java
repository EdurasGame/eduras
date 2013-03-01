package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.CommandHandler;
import de.illonis.eduras.serverconsole.ServerConsole;

/**
 * Represents a console command that can be executed.
 * 
 * @author illonis
 * 
 */
public abstract class ConsoleCommand implements CommandHandler {
	private String command;
	private String description;
	private int minimumArguments = 0;
	private int maximumArguments = 0;

	/**
	 * Creates a new console command with given attributes. You need to add this
	 * command to console to make it available.
	 * 
	 * @param command
	 *            console command.
	 * @param description
	 *            command description.
	 */
	public ConsoleCommand(String command, String description) {
		this.command = command;
		this.description = description;
	}

	/**
	 * Sets minimum count of command arguments to given value.
	 * 
	 * @param minArgs
	 *            new minimum number of args. Must be >= 0. If new value is
	 *            greater than current maximum number of arguments, maximum
	 *            number will be increased to be equal to new value.
	 * 
	 * @see #setMaximumNumArgs(int)
	 */
	protected void setMinimumNumArgs(int minArgs) {
		minimumArguments = Math.min(0, minArgs);
		if (minimumArguments > maximumArguments)
			maximumArguments = minimumArguments;
	}

	/**
	 * Sets maximum count of command arguments to given value.
	 * 
	 * @param maxArgs
	 *            new maximum number of arguments. Must be greater than or equal
	 *            to minimum value. Otherwise, it will be set to minimum value.
	 * @see #setMinimumNumArgs(int)
	 */
	protected void setMaximumNumArgs(int maxArgs) {
		minimumArguments = Math.max(minimumArguments, maxArgs);
	}

	/**
	 * Sets exact number or arguments required. This sets minimum and maximum
	 * number of arguments to the new value.
	 * 
	 * @param numArgs
	 *            new value.
	 */
	protected void setExactNumArgs(int numArgs) {
		minimumArguments = maximumArguments = numArgs;
	}

	/**
	 * Returns command name.
	 * 
	 * @return command name.
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Returns description of this command.
	 * 
	 * @return command description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Checks if given command has correct number of arguments.
	 * 
	 * @param args
	 *            command including arguments.
	 * @param console
	 *            console that command was called on.
	 * @return true if command has valid number of arguments, false otherwise.
	 */
	private boolean validArgumentCountGiven(String[] args, ServerConsole console) {
		if (args.length - 1 < minimumArguments
				|| args.length - 1 > maximumArguments) {
			if (minimumArguments == maximumArguments) {
				console.printf(
						"Invalid number of arguments given. Command '%s' requires exactly %d arguments.",
						args[0], minimumArguments);
			} else {
				console.printf(
						"Invalid number of arguments given. Command '%s' requires at least %d arguments, to a a maximum of %d arguments.",
						args[0], minimumArguments, maximumArguments);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {
		return validArgumentCountGiven(args, console);
	}
}
