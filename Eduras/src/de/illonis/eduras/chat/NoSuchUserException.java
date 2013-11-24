package de.illonis.eduras.chat;

class NoSuchUserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	NoSuchUserException(int userId) {
		super("User with id " + userId + " doesnt exist.");
	}
}
