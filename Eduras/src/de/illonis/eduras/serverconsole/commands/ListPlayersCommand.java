package de.illonis.eduras.serverconsole.commands;

import java.util.Collection;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ServerConsole;
import de.illonis.eduras.units.Player;

/**
 * A command that lists all players currently online.
 * 
 * @author illonis
 * 
 */
public class ListPlayersCommand extends ConsoleCommand {

	public ListPlayersCommand() {
		super("players", "Lists all players that are online.");
		setExactNumArgs(0);
	}

	@Override
	public void onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {
		Collection<Player> players = triggerer.getPlayers();
		if (players.isEmpty())
			console.println("No players online.");
		else {
			for (Player player : players) {
				console.printlnf("%d - %s", player.getOwner(), player.getName());
			}
		}
	}
}
