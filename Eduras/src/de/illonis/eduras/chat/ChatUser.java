package de.illonis.eduras.chat;

import java.util.LinkedList;

/**
 * Encapsulates information about a chat user.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ChatUser {

	private final int id;
	private LinkedList<ChatRoom> usersRooms;
	private String nickName;

	ChatUser(int id, String name) {
		usersRooms = new LinkedList<ChatRoom>();
		nickName = name;
		this.id = id;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof ChatUser) && ((ChatUser) other).getId() == id;
	}

	public int getId() {
		return id;
	}

	/**
	 * Returns the user's nickname
	 * 
	 * @return The nickname
	 */
	public String getNickName() {
		return nickName;
	}

	void setNickName(String name) {
		nickName = name;
	}

	/**
	 * Returns a collection of the rooms the user is in.
	 * 
	 * @return The rooms the user is in.
	 */
	public LinkedList<ChatRoom> getOccupiedRooms() {
		return new LinkedList<ChatRoom>(usersRooms);
	}

	/**
	 * Searches in all the rooms that this user is in for a user witht the given
	 * id
	 * 
	 * @param userId
	 *            of the user to find.
	 * @return The user
	 * @throws UserNotInRoomException
	 *             Thrown if there is no user with such an id.
	 */
	ChatUser findUser(int userId) throws UserNotInRoomException {

		ChatUser dummyUser = new ChatUser(userId, "Wayne Rooney");
		for (ChatRoom room : usersRooms) {
			if (room.containsUser(dummyUser)) {
				return room.findUserById(userId);
			}
		}

		throw new UserNotInRoomException();
	}

	ChatRoom getRoomById(int roomId) throws NoSuchRoomException {
		ChatRoom dummyChat = new ChatRoom("Dummy", roomId, false);
		if (usersRooms.contains(dummyChat)) {
			return usersRooms.get(usersRooms.indexOf(dummyChat));
		}
		throw new NoSuchRoomException(roomId);
	}

	void removeSelf() {
		for (ChatRoom room : usersRooms) {
			room.removeUser(this);
		}
	}

	void removeFromRoom(ChatRoom chatRoom) {
		usersRooms.remove(chatRoom);
	}

	void addToRoom(ChatRoom room) {
		usersRooms.add(room);
	}
}
