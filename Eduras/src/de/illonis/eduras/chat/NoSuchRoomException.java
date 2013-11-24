package de.illonis.eduras.chat;

class NoSuchRoomException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	NoSuchRoomException(int idOfMissingRoom) {
		super("The room with id " + idOfMissingRoom + " cannot be found");
	}
}
