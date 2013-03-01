package de.illonis.eduras.server;

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
	 * 
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

}
