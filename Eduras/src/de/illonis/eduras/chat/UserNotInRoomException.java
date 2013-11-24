package de.illonis.eduras.chat;

/**
 * Thrown when the given user is not in the given room.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class UserNotInRoomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new UserNotInRoomException.
	 * 
	 * @param user
	 *            The user that could not be found in the room.
	 * @param chatRoom
	 *            The respective room.
	 */
	public UserNotInRoomException(ChatUser user, ChatRoom chatRoom) {
		super("Cannot send message of user " + user.getNickName() + " in room "
				+ chatRoom.getName() + " because he is not in this room");
	}

	/**
	 * Creaes a new UserNotInRoomException.
	 */
	public UserNotInRoomException() {
		super("Cannot find user in the given set of rooms.");
	}
}
