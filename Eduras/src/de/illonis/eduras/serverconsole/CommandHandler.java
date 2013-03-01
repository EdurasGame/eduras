package de.illonis.eduras.serverconsole;

import de.illonis.eduras.logic.ConsoleEventTriggerer;

/**
 * Handles a command and reacts to it.
 * 
 * @author illonis
 * 
 */
public interface CommandHandler {

	/**
	 * Called when a command is received to execute it.<br>
	 * 
	 * @param args
	 *            command with args. args[0] is the command and args[1-n] are
	 *            arguments.
	 * @param triggerer
	 *            the triggerer where events can be called.
	 */
	void onCommand(String[] args, ConsoleEventTriggerer triggerer);
}
