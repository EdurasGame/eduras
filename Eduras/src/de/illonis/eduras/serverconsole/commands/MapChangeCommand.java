package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

/**
 * A console command to change the current map.
 * 
 * @author illonis
 * 
 */
public class MapChangeCommand extends ConsoleCommand {

	/**
	 * Creates a new instance of the command.
	 */
	public MapChangeCommand() {
		super("map", "Changes map.");
		setExactNumArgs(1);
	}

	@Override
	public void onCommand(String[] args, ConsolePrinter console,
			ConsoleEventTriggerer triggerer) {
		console.println("Chan");
		if (triggerer.changeMap(args[1])) {
			console.printlnf("Changed map to %s", args[1]);
		} else {
			console.printlnf("%s is no valid map name.", args[1]);
		}
	}

}
