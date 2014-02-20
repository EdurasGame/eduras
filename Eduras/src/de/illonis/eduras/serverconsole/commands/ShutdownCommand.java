package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

/**
 * A shutdown command that shutdowns server.
 * 
 * @author illonis
 * 
 */
public class ShutdownCommand extends ConsoleCommand {

	/**
	 * Create a new instance of the command.
	 */
	public ShutdownCommand() {
		super("shutdown", "shuts down server.");
		setExactNumArgs(0);
	}

	@Override
	public void onCommand(String[] args, ConsolePrinter console,
			ConsoleEventTriggerer triggerer) {
		console.println("Shutting server down...");
		triggerer.shutDown();
	}
}
