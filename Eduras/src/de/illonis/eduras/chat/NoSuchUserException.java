package de.illonis.eduras.chat;

/**
 * Thrown if there is no such user in the chat.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NoSuchUserException extends Exception {

	private static final long serialVersionUID = 1L;

	NoSuchUserException(int userId) {
		super("User with id " + userId + " doesnt exist.");
	}
}
