package de.illonis.eduras.chat;

/**
 * Thrown if there is no such room in the chat.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NoSuchRoomException extends Exception {

	private static final long serialVersionUID = 1L;

	NoSuchRoomException(int idOfMissingRoom) {
		super("The room with id " + idOfMissingRoom + " cannot be found");
	}
}
