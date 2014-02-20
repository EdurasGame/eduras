package de.illonis.eduras.serverconsole.commands;

import de.illonis.eduras.serverconsole.ServerConsole;

/**
 * Handles initialization of console commands at startup.
 * 
 * @author illonis
 * 
 */
public class CommandInitializer {

	/**
	 * Initializes default console commands.<br>
	 * A secondary call will result in resetting commands to default action if
	 * they have been overwritten.
	 * 
	 * @param console
	 *            the console to register commands at.
	 */
	public static void initCommands(ServerConsole console) {
		console.registerCommand(new KickPlayerCommand());
		console.registerCommand(new ListPlayersCommand());
		console.registerCommand(new RespawnCommand());
		console.registerCommand(new ShutdownCommand());
		console.registerCommand(new GameModeChangeCommand());
		console.registerCommand(new MapChangeCommand());
		console.registerCommand(new RestartRoundCommand());
		console.registerCommand(new LoadSettingsCommand());
	}
}
