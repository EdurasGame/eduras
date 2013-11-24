package de.illonis.eduras.chat;

import java.util.logging.Logger;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;

class ChatEventHandlerClient implements EventHandler {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

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
					L.warning(e.getMessage());
					return;
				}
				ChatUser user = room.findUserById(sendingUser);
				chatClient.chatActivityListener.onNewMessage(new ChatMessage(
						user, room, message));
				break;
			}
			case Chat.NAME_CHANGED: {
				int userId = (Integer) event.getArgument(0);
				String newName = (String) event.getArgument(1);
				try {
					ChatUser user = chatClient.getUser().findUser(userId);
					user.setNickName(newName);
					chatClient.chatActivityListener.onNameChanged(user);
				} catch (UserNotInRoomException e) {
					L.warning(e.getMessage());
				}
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
					L.warning(e.getMessage());
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
				break;
			}
			case Chat.CONFIRM_ROOM_JOIN: {
				int roomId = (Integer) event.getArgument(0);
				ChatRoom chatRoom;
				try {
					chatRoom = chatClient.findRoomById(roomId);
					chatClient.chatActivityListener.onYouJoined(chatRoom);
				} catch (NoSuchRoomException e) {
					L.warning(e.getMessage());
				}
				break;
			}
			case Chat.USER_CREATED: {
				int userId = (Integer) event.getArgument(0);
				chatClient.allUsers.add(new ChatUser(userId, "Unknown"));
				break;
			}
			case Chat.USER_REMOVED: {
				int userId = (Integer) event.getArgument(0);
				try {
					ChatUser user = chatClient.findUserById(userId);
					chatClient.removeUser(user);
				} catch (NoSuchUserException e) {
					L.warning(e.getMessage());
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
					L.warning(e.getMessage());
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
					L.warning(e.getMessage());
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
					L.warning(e.getMessage());
				}
				break;
			}

		} catch (TooFewArgumentsExceptions e) {
			L.warning(e.getMessage());
		}

	}
}
