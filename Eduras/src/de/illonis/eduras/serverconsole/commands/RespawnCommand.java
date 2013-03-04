package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ServerConsole;

/**
 * Respawns a player that is stuck.
 * 
 * @author illonis
 * 
 */
public class RespawnCommand extends ConsoleCommand {

	public RespawnCommand() {
		super("respawn", "Respawns player with given player id.");
		setExactNumArgs(1);
	}

	@Override
	public void onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {
		int player;
		try {
			player = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			console.printlnf("Invalid player id: %s", args[1]);
			return;
		}
		triggerer.respawnPlayerById(player);
		console.printlnf("Player %d has been respawned.", player);
	}
}
