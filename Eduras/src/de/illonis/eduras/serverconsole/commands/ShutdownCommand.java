package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ServerConsole;

/**
 * A shutdown command that shutdowns server.
 * 
 * @author illonis
 * 
 */
public class ShutdownCommand extends ConsoleCommand {

	public ShutdownCommand() {
		super("shutdown", "shuts down server.");
		setExactNumArgs(0);
	}

	@Override
	public void onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {
		console.println("Shutting server down...");
		triggerer.shutDown();
	}
}