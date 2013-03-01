package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ServerConsole;

public class ExampleCommand extends ConsoleCommand {

	ExampleCommand() {
		super("example", "Prints an example text.");
	}

	@Override
	public boolean onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {
		console.print("Hat geklappt.");
		return true;
	}
}
