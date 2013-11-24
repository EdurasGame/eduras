package de.illonis.eduras.chat;

import java.util.LinkedList;
import java.util.logging.Logger;

import de.eduras.eventingserver.Client;
import de.eduras.eventingserver.ClientInterface;
import de.eduras.eventingserver.ClientNetworkEventHandler;
import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;

class ChatClientImpl implements ChatClient {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	private ClientInterface client;
	private ChatUser user;
	private boolean connected;
	ChatActivityListener chatActivityListener;
	LinkedList<ChatRoom> visibleChatRooms;
	LinkedList<ChatRoom> allRooms;
	LinkedList<ChatUser> allUsers;

	public ChatClientImpl() {
		client = new Client();
		client.setEventHandler(new ChatEventHandlerClient(this));
		client.setNetworkPolicy(new ChatPolicy());
		connected = false;

		client.setNetworkEventHandler(new ClientNetworkEventHandler() {

			@Override
			public void onClientDisconnected(int clientId) {
				// not interesting as own disconnection is already covered and
				// other user's disconnection is handled seperately
			}

			@Override
			public void onClientConnected(int clientId) {

				if (clientId == client.getClientId()) {
					chatActivityListener.onConnectionEstablished();
					user = new ChatUser(client.getClientId(), "Unknown");
					connected = true;
				}

				// other user's connection are handled seperately

			}

			@Override
			public void onServerIsFull() {
				// not gonna happen as there is no limit
			}

			@Override
			public void onDisconnected() {
				connected = false;
				chatActivityListener.onConnectionAborted();
			}

			@Override
			public void onConnectionLost() {
				onDisconnected();
			}

			@Override
			public void onClientKicked(int clientId, String reason) {
				// not gonna happen

			}

			@Override
			public void onPingReceived(long latency) {
				// not gonna happen

			}
		});
	}

	@Override
	public void postChatMessage(String message, ChatRoom chatRoom)
			throws UserNotInRoomException, IllegalArgumentException,
			NotConnectedException {
		checkConnection();

		if (!chatRoom.containsUser(user)) {
			throw new UserNotInRoomException(user, chatRoom);
		}

		Event sendMessageEvent = new Event(Chat.SEND_MESSAGE);
		sendMessageEvent.putArgument(client.getClientId());
		sendMessageEvent.putArgument(chatRoom.getRoomId());
		sendMessageEvent.putArgument(message);

		try {
			client.sendEvent(sendMessageEvent);
		} catch (TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}
	}

	private void checkConnection() throws NotConnectedException {
		if (!connected) {
			throw new NotConnectedException();
		}
	}

	@Override
	public void setName(String newName) throws IllegalArgumentException,
			NotConnectedException {
		checkConnection();

		Event setNameEvent = new Event(Chat.SET_NAME);
		setNameEvent.putArgument(client.getClientId());
		setNameEvent.putArgument(newName);

		try {
			client.sendEvent(setNameEvent);
		} catch (TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}
	}

	@Override
	public void inviteUserToRoom(ChatUser invitedUser, ChatRoom chatRoom)
			throws UserNotInRoomException, IllegalArgumentException,
			NotConnectedException {
		checkConnection();

		if (!chatRoom.containsUser(invitedUser)) {
			throw new UserNotInRoomException(invitedUser, chatRoom);
		}

		Event inviteUserToRoomEvent = new Event(Chat.INVITE_USER);

		inviteUserToRoomEvent.putArgument(client.getClientId());
		inviteUserToRoomEvent.putArgument(invitedUser.getId());
		inviteUserToRoomEvent.putArgument(chatRoom.getRoomId());

		try {
			client.sendEvent(inviteUserToRoomEvent);
		} catch (TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<ChatRoom> getRooms() {
		return (LinkedList<ChatRoom>) visibleChatRooms.clone();
	}

	@Override
	public void acceptInvite(Invitation invite)
			throws IllegalArgumentException, TooFewArgumentsExceptions,
			NotConnectedException {
		checkConnection();

		Event acceptInviteEvent = new Event(Chat.ACCEPT_INVITE);
		acceptInviteEvent.putArgument(client.getClientId());
		acceptInviteEvent.putArgument(invite.getRoom().getRoomId());

		client.sendEvent(acceptInviteEvent);
	}

	@Override
	public void setChatActivityListener(ChatActivityListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException(
					"Cannot set the listener to null!");
		}
		this.chatActivityListener = listener;
	}

	@Override
	public void connect(String hostname, int port) {
		client.connect(hostname, port);
	}

	@Override
	public ChatUser getUser() {
		return user;
	}

	ChatRoom findRoomById(int roomId) throws NoSuchRoomException {
		ChatRoom dummyRoom = new ChatRoom("Dummy", roomId, false);
		if (allRooms.contains(dummyRoom)) {
			return allRooms.get(allRooms.indexOf(dummyRoom));
		}
		throw new NoSuchRoomException(roomId);
	}

	ChatUser findUserById(int invitingUserId) throws NoSuchUserException {
		ChatUser dummyUser = new ChatUser(invitingUserId, "Dummy");
		if (allUsers.contains(dummyUser)) {
			return allUsers.get(allUsers.indexOf(dummyUser));
		}
		throw new NoSuchUserException(invitingUserId);
	}

	void removeUser(ChatUser userToRemove) {
		allUsers.remove(userToRemove);
		userToRemove.removeSelf();
	}

	void removeRoom(ChatRoom room) {
		allRooms.remove(room);
		visibleChatRooms.remove(room);
		room.removeSelf();
	}
}
