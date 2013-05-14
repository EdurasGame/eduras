package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.CommandHandler;
import de.illonis.eduras.serverconsole.ServerConsole;

/**
 * Represents a console command that can be executed.<br>
 * A command can have arguments. The number of arguments must be in a given
 * range (default: exactly zero).
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
	 *            the console command that must be typed to call this command.
	 * @param description
	 *            command description. Command and its arguments should be
	 *            explained here.
	 */
	public ConsoleCommand(String command, String description) {
		this.command = command;
		this.description = description;
	}

	/**
	 * Sets minimum count of command arguments to given value.<br>
	 * Argument number is always checked before calling
	 * {@link #onCommand(String[], ServerConsole, ConsoleEventTriggerer)}.
	 * 
	 * @param minArgs
	 *            new minimum number of args. Must be >= 0. If new value is
	 *            greater than current maximum number of arguments, maximum
	 *            number will be increased to be equal to new value.
	 * 
	 * @see #setMaximumNumArgs(int)
	 */
	protected final void setMinimumNumArgs(int minArgs) {
		minimumArguments = Math.min(0, minArgs);
		if (minimumArguments > maximumArguments)
			maximumArguments = minimumArguments;
	}

	/**
	 * Sets maximum count of command arguments to given value.<br>
	 * Argument number is always checked before calling
	 * {@link #onCommand(String[], ServerConsole, ConsoleEventTriggerer)}.
	 * 
	 * @param maxArgs
	 *            new maximum number of arguments. Must be greater than or equal
	 *            to minimum value. Otherwise, it will be set to minimum value.
	 * @see #setMinimumNumArgs(int)
	 */
	protected final void setMaximumNumArgs(int maxArgs) {
		minimumArguments = Math.max(minimumArguments, maxArgs);
	}

	/**
	 * Sets exact number or arguments required. This sets minimum and maximum
	 * number of arguments to the new value.<br>
	 * Argument number is always checked before calling
	 * {@link #onCommand(String[], ServerConsole, ConsoleEventTriggerer)}.
	 * 
	 * @param numArgs
	 *            new value.
	 * @see #setMinimumNumArgs(int)
	 * @see #setMaximumNumArgs(int)
	 */
	protected final void setExactNumArgs(int numArgs) {
		minimumArguments = maximumArguments = numArgs;
	}

	/**
	 * Returns maximum number of arguments that is allowed.
	 * 
	 * @return maximum arguments count.
	 */
	public final int getMaximumArguments() {
		return maximumArguments;
	}

	/**
	 * Returns minimum number of arguments that is allowed.
	 * 
	 * @return minimum arguments count.
	 */
	public final int getMinimumArguments() {
		return minimumArguments;
	}

	/**
	 * Returns command name.
	 * 
	 * @return command name.
	 */
	public final String getCommand() {
		return command;
	}

	/**
	 * Returns description of this command.
	 * 
	 * @return command description.
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * Compares given argument count to required argument count.
	 * 
	 * @param argCount
	 *            argument count to compare.
	 * @return true if given argument count is in range of allowed argument
	 *         counts.
	 */
	public final boolean argumentCountMatches(int argCount) {
		return !(argCount < minimumArguments || argCount > maximumArguments);
	}
}
