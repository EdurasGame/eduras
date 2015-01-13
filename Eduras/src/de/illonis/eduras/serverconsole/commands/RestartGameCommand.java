package de.illonis.eduras.serverconsole.commands;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

/**
 * This command restarts the match with the current map resetting all player
 * stats and the round count.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class RestartGameCommand extends ConsoleCommand {
	public RestartGameCommand() {
		super("restartgame", "Restarts the game/match");
		setExactNumArgs(0);
	}

	private final static Logger L = EduLog
			.getLoggerFor(RestartGameCommand.class.getName());

	@Override
	public void onCommand(String[] args, ConsolePrinter console,
			ConsoleEventTriggerer triggerer) {
		triggerer.restartGame();
		console.println("Restarted match!");

	}
}
