package de.illonis.eduras.exceptions;

/**
 * Thrown when a name is invalid.
 * 
 * @author illonis
 * 
 */
public class InvalidNameException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link InvalidNameException}.
	 * 
	 * @param string
	 *            the invalid name.
	 */
	public InvalidNameException(String string) {
		super(string);

	}
}
