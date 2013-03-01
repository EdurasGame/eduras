package de.illonis.eduras.serverconsole;

/**
 * Represents a console command that can be executed.
 * 
 * @author illonis
 * 
 */
public abstract class ConsoleCommand implements CommandHandler {
	private String command;
	private String description;

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

}
