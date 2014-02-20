package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

/**
 * A command that kicks a specific player from server.
 * 
 * @author illonis
 * 
 */
public class KickPlayerCommand extends ConsoleCommand {

	KickPlayerCommand() {
		super("kick",
				"Kicks a player with given player id. Usage: kick <player-id>");
		setExactNumArgs(1);
	}

	@Override
	public void onCommand(String[] args, ConsolePrinter console,
			ConsoleEventTriggerer triggerer) {
		int player;
		try {
			player = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			console.printlnf("Invalid player number: %d", args[1]);
			return;
		}
		if (triggerer.kickPlayerById(player)) {
			console.printlnf("Player %d was kicked from server.", player);
			return;
		} else {
			console.printlnf("Player %d does not exist.", player);
			return;
		}
	}
}
