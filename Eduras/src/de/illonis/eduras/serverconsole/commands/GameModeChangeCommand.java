package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ServerConsole;

/**
 * Changes game mode to a given game mode.
 * 
 * @author illonis
 * 
 */
public class GameModeChangeCommand extends ConsoleCommand {

	/**
	 * Creates a new instance of the command.
	 */
	public GameModeChangeCommand() {
		super("mode", "Changes game mode to given mode.");
		setExactNumArgs(1);
	}

	@Override
	public void onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {

		console.printlnf("Changing game mode to %s...", args[1]);
		if (triggerer.changeGameMode(args[1])) {
			console.println("Success.");
		} else {
			console.printlnf("%s is no valid game mode.", args[1]);
		}
	}
}
