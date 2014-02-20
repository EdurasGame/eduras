package de.illonis.eduras.serverconsole;

import de.illonis.eduras.serverconsole.commands.ConsoleCommand;

public class CommandInput {

	private final ConsoleCommand command;
	private final String[] args;

	public CommandInput(ConsoleCommand command, String[] args) {
		this.command = command;
		this.args = args;
	}

	public String[] getArgs() {
		return args;
	}

	public ConsoleCommand getCommand() {
		return command;
	}
}
