package de.illonis.eduras.chat;

import java.util.LinkedList;

public class ChatRoom {

	private String name;
	private LinkedList<ChatUser> usersInTheRoom;
	private int roomId;
	private boolean isPublic;

	ChatRoom(String name, int id, boolean isPublic) {
		this.name = name;
		this.roomId = id;
		this.isPublic = isPublic;
	}

	public int getRoomId() {
		return roomId;
	}

	public boolean containsUser(ChatUser user) {
		return usersInTheRoom.contains(user);
	}

	public String getName() {
		return name;
	}

	void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	boolean isPublic() {
		return isPublic;
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
}
