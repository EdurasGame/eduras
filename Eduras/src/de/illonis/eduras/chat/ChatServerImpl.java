package de.illonis.eduras.chat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.Server;
import de.eduras.eventingserver.ServerInterface;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;

public class ChatServerImpl implements ChatServer {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	final ServerInterface server;
	private boolean running;
	LinkedList<ChatUser> users;
	LinkedList<ChatRoom> rooms;
	LinkedList<Invitation> currentInvitations;

	ChatServerImpl() {
		server = new Server();
		users = new LinkedList<ChatUser>();
		rooms = new LinkedList<ChatRoom>();
	}

	@Override
	public boolean start(int port) {
		running = server.start("MyChatServer", port);
		return running;
	}

	@Override
	public void stop() {
		server.stop();
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ChatUser> getUsers() throws NotConnectedException {
		checkRunning();
		return (LinkedList<ChatUser>) users.clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ChatRoom> getRooms() throws NotConnectedException {
		checkRunning();
		return (LinkedList<ChatRoom>) rooms.clone();
	}

	@Override
	public void disconnectUser(ChatUser user) throws NotConnectedException,
			NoSuchUserException {
		checkRunning();

		ChatUser userToKick = getUser(user);

		users.remove(userToKick);
		userToKick.removeSelf();

		Event userRemovedEvent = new Event(Chat.USER_REMOVED);
		userRemovedEvent.putArgument(userToKick.getId());
		try {
			server.sendEventToAll(userRemovedEvent);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}
	}

	private ChatUser getUser(ChatUser user) throws NoSuchUserException {
		if (users.contains(user)) {
			return users.get(users.indexOf(user));
		}
		throw new NoSuchUserException(user.getId());

	}

	@Override
	public ChatRoom createRoom(String name, boolean isPublic)
			throws IllegalArgumentException, NotConnectedException {
		checkRunning();

		ChatRoom newRoom = new ChatRoom(name, ChatRoom.lastId, isPublic);
		ChatRoom.lastId++;
		rooms.add(newRoom);

		Event createRoomEvent = new Event(Chat.ROOM_CREATED);
		createRoomEvent.putArgument(newRoom.getName());
		createRoomEvent.putArgument(newRoom.getRoomId());
		createRoomEvent.putArgument(newRoom.isPublic());
		try {
			server.sendEventToAll(createRoomEvent);
		} catch (TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
			return null;
		}
		return newRoom;
	}

	@Override
	public ChatServer create() {
		return new ChatServerImpl();
	}

	@Override
	public void addUserToRoom(ChatUser user, ChatRoom room)
			throws NotConnectedException, NoSuchUserException,
			NoSuchRoomException {
		checkRunning();

		ChatUser userToAdd = getUser(user);
		ChatRoom roomToAddTo = getRoom(room);

		userToAdd.addToRoom(roomToAddTo);
		roomToAddTo.addUser(userToAdd);

		Event userJoinedRoomEvent = new Event(Chat.USER_JOINED_ROOM);
		userJoinedRoomEvent.putArgument(userToAdd.getId());
		userJoinedRoomEvent.putArgument(roomToAddTo.getRoomId());
		try {
			server.sendEventToAll(userJoinedRoomEvent);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}
	}

	@Override
	public void removeUserFromRoom(ChatUser user, ChatRoom room)
			throws NotConnectedException, NoSuchUserException,
			NoSuchRoomException {
		checkRunning();

		ChatUser userToRemove = getUser(user);
		ChatRoom roomToRemoveFrom = getRoom(room);

		userToRemove.removeFromRoom(roomToRemoveFrom);
		roomToRemoveFrom.removeUser(userToRemove);

		Event userLeftRoomEvent = new Event(Chat.USER_LEFT_ROOM);
		userLeftRoomEvent.putArgument(userToRemove.getId());
		userLeftRoomEvent.putArgument(roomToRemoveFrom.getRoomId());
		try {
			server.sendEventToAll(userLeftRoomEvent);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}
	}

	@Override
	public void removeRoom(ChatRoom room) throws NoSuchRoomException {
		ChatRoom roomToRemove = getRoom(room);

		roomToRemove.removeSelf();
		rooms.remove(roomToRemove);

		Event removeRoomEvent = new Event(Chat.ROOM_REMOVED);
		removeRoomEvent.putArgument(roomToRemove.getRoomId());
		try {
			server.sendEventToAll(removeRoomEvent);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}
	}

	private ChatRoom getRoom(ChatRoom room) throws NoSuchRoomException {
		if (rooms.contains(room)) {
			return rooms.get(rooms.indexOf(room));
		}
		throw new NoSuchRoomException(room.getRoomId());

	}

	private void checkRunning() throws NotConnectedException {
		if (!running) {
			throw new NotConnectedException();
		}
	}

	ChatRoom getRoomById(int idOfRoom) throws NoSuchRoomException {
		ChatRoom dummyRoom = new ChatRoom("DummyRoom", idOfRoom, false);
		if (rooms.contains(dummyRoom)) {
			return rooms.get(rooms.indexOf(dummyRoom));
		}
		throw new NoSuchRoomException(idOfRoom);
	}

	ChatUser findUserById(int userId) throws NoSuchUserException {
		ChatUser dummyUser = new ChatUser(userId, "DummyRoom");
		if (users.contains(dummyUser)) {
			return users.get(users.indexOf(dummyUser));
		}
		throw new NoSuchUserException(userId);
	}

	void addInvitation(Invitation invitation) {
		synchronized (currentInvitations) {
			if (!currentInvitations.contains(invitation)) {
				currentInvitations.add(invitation);
			}
		}
	}

	boolean checkAccept(Invitation acceptedInvitation) {
		synchronized (currentInvitations) {
			if (currentInvitations.contains(acceptedInvitation)) {
				currentInvitations.remove(acceptedInvitation);
				return true;
			} else {
				return false;
			}
		}
	}
}
