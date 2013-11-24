package de.illonis.eduras.chat;

public class NoSuchRoomException extends Exception {
	public NoSuchRoomException(int idOfMissingRoom) {
		super("The room with id " + idOfMissingRoom + " cannot be found");
	}
}
