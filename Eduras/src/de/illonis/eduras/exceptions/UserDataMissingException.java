package de.illonis.eduras.exceptions;

/**
 * Indicates that progamm tried to load user data that do not exist.
 * 
 * @author illonis
 * 
 */
public class UserDataMissingException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link UserDataMissingException}.
	 */
	public UserDataMissingException() {
		super();
	}
}
