package de.illonis.eduras.chat;

public class NoSuchUserException extends Exception {
	public NoSuchUserException(int userId) {
		super("User with id " + userId + " doesnt exist.");
	}
}
