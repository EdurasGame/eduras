package de.illonis.eduras.serverconsole;

/**
 * Handles a command and reacts to it.
 * 
 * @author illonis
 * 
 */
public interface CommandHandler {

	/**
	 * Called when a command is received to execute it.<br>
	 * To make calls on logic, you can use {@link ServerConsole#getTriggerer()}.
	 * 
	 * @param args
	 *            command with args. args[0] is the command and args[1-n] are
	 *            arguments.
	 */
	void onCommand(String[] args);
}
