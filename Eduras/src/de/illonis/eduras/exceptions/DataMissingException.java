package de.illonis.eduras.exceptions;

/**
 * Indicates that data that should be available is not available.
 * 
 * @author illonis
 * 
 */
public class DataMissingException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link DataMissingException}.
	 */
	public DataMissingException() {
		super();
	}

	/**
	 * Creates a new {@link DataMissingException} with a describing string.
	 * 
	 * @param e
	 *            The string to describe the missing data.
	 */
	public DataMissingException(String e) {
		super(e);
	}
}
