package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ServerConsole;
import de.illonis.eduras.settings.S;

/**
 * A command that loads the file at the given path and restarts the game with
 * the new settings.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class LoadSettingsCommand extends ConsoleCommand {

	/**
	 * Create a new LoadSettingsCommand.
	 */
	public LoadSettingsCommand() {
		super("load_settings",
				"Loads the settings in the given file and restarts the map.");
		setExactNumArgs(1);
	}

	@Override
	public void onCommand(String[] args, ServerConsole console,
			ConsoleEventTriggerer triggerer) {

		console.println("Loading settings " + args[1]);

		try {
			if (args[1].equals("highspeed")) {
				console.println("Internal settings specified: " + args[1]);
				triggerer.loadSettings(S.class.getResource(args[1]).getPath());
			} else {
				triggerer.loadSettings(args[1]);
			}
		} catch (IllegalArgumentException e) {
			console.println("Error: " + e);
		}

	}
}
