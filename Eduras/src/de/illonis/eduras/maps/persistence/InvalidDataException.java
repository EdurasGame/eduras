package de.illonis.eduras.maps.persistence;

/**
 * Signals that invalid data were tried to be read.
 * 
 * @author illonis
 * 
 */
public class InvalidDataException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new InvalidDataException.
	 * 
	 * @param msg
	 *            the error message.
	 * @param line
	 *            the line in file the error occured.
	 */
	public InvalidDataException(String msg, int line) {
		super(msg + " at line " + line);
	}
}
