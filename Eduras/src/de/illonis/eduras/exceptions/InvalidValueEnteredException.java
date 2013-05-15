package de.illonis.eduras.exceptions;

/**
 * Thrown when an invalid value was entered into a textfield.
 * 
 * @author illonis
 * 
 */
public class InvalidValueEnteredException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception.
	 * 
	 * @param string
	 *            the invalid value.
	 */
	public InvalidValueEnteredException(String string) {
		super(string);
	}
}
