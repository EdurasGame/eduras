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
	 */
	public static void initCommands() {
		ServerConsole.registerCommand(new KickPlayerCommand());
	}
}
