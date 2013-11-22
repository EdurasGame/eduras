package de.illonis.eduras.chat;

import java.util.Collection;

public class ChatServerImpl implements ChatServer {

	@Override
	public boolean start(int port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<ChatUser> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ChatRoom> getRooms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean disconnectUser(ChatUser user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean kickUserFromRoom(ChatUser user, ChatRoom room) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ChatRoom createRoom(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
