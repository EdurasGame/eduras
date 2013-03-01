package de.illonis.eduras.serverconsole;

import de.illonis.eduras.logic.ConsoleEventTriggerer;

public class ExampleCommand extends ConsoleCommand {

	public ExampleCommand() {
		super("example", "Prints an example text.");
	}

	@Override
	public void onCommand(String[] args, ConsoleEventTriggerer triggerer) {
		ServerConsole.print("Hat geklappt.");
	}
}
