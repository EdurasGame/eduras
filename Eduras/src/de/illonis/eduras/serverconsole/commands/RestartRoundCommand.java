package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

/**
 * This command restarts the match with the current map resetting all player
 * stats.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class RestartRoundCommand extends ConsoleCommand {

	/**
	 * Create a new RestartRoundCommand
	 */
	public RestartRoundCommand() {
		super("restartround", "Restarts the current map");
		setExactNumArgs(0);
	}

	@Override
	public void onCommand(String[] args, ConsolePrinter console,
			ConsoleEventTriggerer triggerer) {

		triggerer.restartRound();
		console.println("Restart map...");

	}
}
