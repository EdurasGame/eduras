package de.illonis.eduras.chat;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This class wraps the information about a chat room.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ChatRoom {

	static int lastId = 0;

	private String name;
	private LinkedList<ChatUser> usersInTheRoom;
	private int roomId;
	private boolean isPublic;

	ChatRoom(String name, int id, boolean isPublic) {
		this.name = name;
		this.roomId = id;
		this.isPublic = isPublic;

		usersInTheRoom = new LinkedList<ChatUser>();
	}

	int getRoomId() {
		return roomId;
	}

	/**
	 * Tells whether the given user is in the room.
	 * 
	 * @param user
	 * @return True if the user is in the room, false otherwise
	 */
	public boolean containsUser(ChatUser user) {
		return usersInTheRoom.contains(user);
	}

	/**
	 * Returns the room's name.
	 * 
	 * @return The room's name.
	 */
	public String getName() {
		return name;
	}

	void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * Tells whether the room is public or not.
	 * 
	 * @return True if the room is public, false otherwise.
	 */
	public boolean isPublic() {
		return isPublic;
	}

	/**
	 * Returns all the users in the room.
	 * 
	 * @return A collection of users in the room.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ChatUser> getUsersInRoom() {
		return (LinkedList<ChatUser>) usersInTheRoom.clone();
	}

	ChatUser findUserById(int id) {
		return usersInTheRoom.get(usersInTheRoom.indexOf(new ChatUser(id,
				"Wayne")));
	}

	void removeUser(ChatUser user) {
		usersInTheRoom.remove(user);
	}

	void removeSelf() {
		for (ChatUser user : usersInTheRoom) {
			user.removeFromRoom(this);
		}
	}

	void addUser(ChatUser user) {
		usersInTheRoom.add(user);
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof ChatRoom)
				&& ((ChatRoom) other).getRoomId() == roomId;
	}
}
