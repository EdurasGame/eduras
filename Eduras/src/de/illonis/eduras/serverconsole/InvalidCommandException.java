package de.illonis.eduras.serverconsole;

/**
 * Indicates that a wrong command or unsufficient command line arguments have
 * been entered.<br>
 * Use {@link #getMessage()} to retrieve an error message.
 * 
 * @author illonis
 * 
 */
public class InvalidCommandException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 *            the error message.
	 */
	public InvalidCommandException(String msg) {
		super(msg);
	}
}
