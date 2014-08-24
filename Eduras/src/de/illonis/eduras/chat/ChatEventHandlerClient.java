package de.illonis.eduras.chat;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;

class ChatEventHandlerClient implements EventHandler {

	private final static Logger L = EduLog
			.getLoggerFor(ChatEventHandlerClient.class.getName());

	private ChatClientImpl chatClient;

	public ChatEventHandlerClient(ChatClientImpl client) {
		this.chatClient = client;
	}

	@Override
	public void handleEvent(Event event) {

		try {
			switch (event.getEventNumber()) {
			case Chat.NEW_MESSAGE: {

				int sendingUser = (Integer) event.getArgument(0);
				int roomId = (Integer) event.getArgument(1);
				String message = (String) event.getArgument(2);

				ChatRoom room;
				try {
					room = chatClient.getUser().getRoomById(roomId);
				} catch (NoSuchRoomException e) {
					L.log(Level.WARNING, "Error sending message.", e);
					return;
				}
				ChatUser user = room.findUserById(sendingUser);
				chatClient.chatActivityListener.onNewMessage(new ChatMessage(
						user, room, message, System.currentTimeMillis()));

				L.finest("Received message " + message + " by user "
						+ user.getNickName() + " in room " + room.getName()
						+ ".");
				break;
			}
			case Chat.NAME_CHANGED: {
				int userId = (Integer) event.getArgument(0);
				String newName = (String) event.getArgument(1);
				try {
					ChatUser user = chatClient.findUserById(userId);
					user.setNickName(newName);
					chatClient.chatActivityListener.onNameChanged(user);
				} catch (NoSuchUserException e) {
					L.log(Level.WARNING, "Error changing name.", e);
				}
				L.fine("User with id " + userId + " changed his name to "
						+ newName);
				break;
			}
			case Chat.INVITE_TO_ROOM: {
				int invitingUserId = (Integer) event.getArgument(0);
				int roomId = (Integer) event.getArgument(1);
				ChatRoom chatRoom;
				try {
					chatRoom = chatClient.findRoomById(roomId);
					ChatUser invitingUser = chatClient
							.findUserById(invitingUserId);
					Invitation invitation = new Invitation(chatRoom,
							invitingUser, chatClient.getUser());
					chatClient.chatActivityListener
							.onInviteReceived(invitation);
				} catch (NoSuchRoomException | NoSuchUserException e) {
					L.log(Level.WARNING, "Error inviting user to room.", e);
				}
				break;
			}
			case Chat.ROOM_CREATED: {
				String roomName = (String) event.getArgument(0);
				int roomId = (Integer) event.getArgument(1);
				boolean isPublic = (Boolean) event.getArgument(2);
				ChatRoom newRoom = new ChatRoom(roomName, roomId, isPublic);
				chatClient.allRooms.add(newRoom);
				if (isPublic) {
					chatClient.visibleChatRooms.add(newRoom);
				}
				L.info("New room " + roomName + " with id " + roomId
						+ " created. The room "
						+ (isPublic ? "is public" : "is not public."));
				break;
			}
			case Chat.CONFIRM_ROOM_JOIN: {
				int roomId = (Integer) event.getArgument(0);
				ChatRoom chatRoom;
				try {
					chatRoom = chatClient.findRoomById(roomId);
					chatClient.chatActivityListener.onYouJoined(chatRoom);
				} catch (NoSuchRoomException e) {
					L.log(Level.WARNING, "Error confirming room join.", e);
				}
				break;
			}
			case Chat.USER_CREATED: {
				int userId = (Integer) event.getArgument(0);
				chatClient.allUsers.add(new ChatUser(userId, "Unknown"));

				if (chatClient.getUser() != null
						&& userId == chatClient.getUser().getId()) {
					L.log(Level.WARNING,
							"Got a USER_CREATED message with the id of own user. That shouldn't happen, but instead be done via a YOU_CONNECTED message.");
				}

				L.info("User was created with id " + userId);
				break;
			}
			case Chat.YOU_CONNECTED: {
				int userId = (Integer) event.getArgument(0);

				ChatUser me;
				try {
					me = chatClient.findUserById(userId);
				} catch (NoSuchUserException e) {
					L.severe("Couldn't find an own user when received you connected message.");
					return;
				}

				chatClient.setUser(me);
				chatClient.connected = true;
				chatClient.chatActivityListener.onConnectionEstablished();
				L.info("You connected to the chat.");

				break;
			}
			case Chat.USER_REMOVED: {
				int userId = (Integer) event.getArgument(0);
				try {
					ChatUser user = chatClient.findUserById(userId);
					chatClient.removeUser(user);
				} catch (NoSuchUserException e) {
					L.log(Level.WARNING, "Error removing user.", e);
				}
				break;
			}
			case Chat.ROOM_REMOVED: {
				int roomId = (Integer) event.getArgument(0);
				ChatRoom room;
				try {
					room = chatClient.findRoomById(roomId);
					chatClient.removeRoom(room);
				} catch (NoSuchRoomException e) {
					L.log(Level.WARNING, "Error removing room.", e);
				}
				break;
			}
			case Chat.USER_JOINED_ROOM: {
				int userId = (Integer) event.getArgument(0);
				int roomId = (Integer) event.getArgument(1);
				try {
					ChatUser user = chatClient.findUserById(userId);
					ChatRoom room = chatClient.findRoomById(roomId);
					user.addToRoom(room);
					room.addUser(user);
				} catch (NoSuchRoomException | NoSuchUserException e) {
					L.log(Level.WARNING, "Error joining room.", e);
				}
				break;
			}
			case Chat.USER_LEFT_ROOM:
				int userId = (Integer) event.getArgument(0);
				int roomId = (Integer) event.getArgument(1);
				try {
					ChatUser user = chatClient.findUserById(userId);
					ChatRoom room = chatClient.findRoomById(roomId);
					user.removeFromRoom(room);
					room.removeUser(user);
				} catch (NoSuchRoomException | NoSuchUserException e) {
					L.log(Level.WARNING, "Error leaving room.", e);
				}
				break;
			}

		} catch (TooFewArgumentsExceptions e) {
			L.log(Level.WARNING, "Too few arguments", e);
		}

	}
}
