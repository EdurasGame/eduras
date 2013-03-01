package de.illonis.eduras.server;

public class ExampleCommand extends ConsoleCommand {

	public ExampleCommand() {
		super("example", "Prints an example text.");
	}

	@Override
	public void onCommand(String[] args) {
		ServerConsole.print("Hat geklappt.");
	}
}
