package de.illonis.eduras.chat;

import java.util.Collection;
import java.util.LinkedList;

import de.eduras.eventingserver.Server;
import de.eduras.eventingserver.ServerInterface;

class ChatServerImpl implements ChatServer {

	final ServerInterface server;
	private boolean running;
	LinkedList<ChatUser> users;
	LinkedList<ChatRoom> rooms;

	ChatServerImpl() {
		server = new Server();
		users = new LinkedList<ChatUser>();
		rooms = new LinkedList<ChatRoom>();
	}

	@Override
	public boolean start(int port) {
		return server.start("MyChatServer", port);
	}

	@Override
	public void stop() {
		server.stop();
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ChatUser> getUsers() {
		return (LinkedList<ChatUser>) users.clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ChatRoom> getRooms() {
		return (LinkedList<ChatRoom>) rooms.clone();
	}

	@Override
	public boolean disconnectUser(ChatUser user) {
		return false;
	}

	@Override
	public ChatRoom createRoom(String name, boolean isPublic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChatServer create() {
		return new ChatServerImpl();
	}

	@Override
	public void addUserToRoom(ChatUser user, ChatRoom room) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUserFromRoom(ChatUser user, ChatRoom room) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRoom(ChatRoom room) {
		// TODO Auto-generated method stub

	}
}
