package de.illonis.eduras.serverconsole;

/**
 * Thrown when no console is available.
 * 
 * @author illonis
 * 
 */
public class NoConsoleException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of this exception.
	 */
	public NoConsoleException() {
		super("No console available.");
	}
}
