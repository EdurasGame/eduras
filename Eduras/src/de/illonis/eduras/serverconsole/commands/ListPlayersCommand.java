package de.illonis.eduras.serverconsole.commands;

import java.util.Collection;

import de.illonis.eduras.Player;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

/**
 * A command that lists all players currently online.
 * 
 * @author illonis
 * 
 */
public class ListPlayersCommand extends ConsoleCommand {

	/**
	 * Creates a new instant of the command.
	 */
	public ListPlayersCommand() {
		super("players", "Lists all players that are online.");
		setExactNumArgs(0);
	}

	@Override
	public void onCommand(String[] args, ConsolePrinter console,
			ConsoleEventTriggerer triggerer) {
		Collection<Player> players = triggerer.getPlayers();
		if (players.isEmpty())
			console.println("No players online.");
		else {
			for (Player player : players) {
				console.printlnf("%d - %s", player.getPlayerId(),
						player.getName());
			}
		}
	}
}
