package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ServerConsole;

public class KickPlayerCommand extends ConsoleCommand {

	KickPlayerCommand() {
		super("kick", "Kicks a player with given player id. Usage: kick <id>");
		setExactNumArgs(1);
	}

	@Override
	public boolean onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {
		if (!super.onCommand(args, console, triggerer))
			return false;
		int player;
		try {
			player = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			console.printf("Invalid player number: %d", args[1]);
			return false;
		}
		if (triggerer.kickPlayerById(player)) {
			console.printf("Player %d was kicked from server.", player);
			return true;
		} else {
			console.printf("Player %d does not exist.", player);
			return false;
		}
	}
}
