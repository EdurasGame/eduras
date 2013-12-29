package de.illonis.eduras.chat;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.eduras.eventingserver.test.NoSuchClientException;
import de.illonis.edulog.EduLog;

class ChatEventHandlerServer implements EventHandler {

	private final static Logger L = EduLog
			.getLoggerFor(ChatEventHandlerServer.class.getName());

	ChatServerImpl chatServer;

	public ChatEventHandlerServer(ChatServerImpl chatServer) {
		this.chatServer = chatServer;
	}

	@Override
	public void handleEvent(Event event) {
		try {
			switch (event.getEventNumber()) {
			case Chat.SEND_MESSAGE: {
				int idOfSendingUser = (Integer) event.getArgument(0);
				int idOfRoom = (Integer) event.getArgument(1);
				String message = (String) event.getArgument(2);

				L.info("Got the following message from client: "
						+ idOfSendingUser + " in chatroom " + idOfRoom + ": "
						+ message);

				ChatRoom chatRoom;
				try {
					chatRoom = chatServer.getRoomById(idOfRoom);
					if (chatRoom.containsUser(new ChatUser(idOfSendingUser,
							"MyDummyUser"))) {
						Event newMessageEvent = new Event(Chat.NEW_MESSAGE);
						newMessageEvent.putArgument(idOfSendingUser);
						newMessageEvent.putArgument(idOfRoom);
						newMessageEvent.putArgument(message);
						chatServer.server.sendEventToAll(newMessageEvent);
					}
				} catch (NoSuchRoomException e) {
					L.log(Level.WARNING, "error processing sent message", e);
				}

				break;
			}
			case Chat.SET_NAME: {
				int userId = (Integer) event.getArgument(0);
				String newName = (String) event.getArgument(1);

				L.info("User #" + userId + " changes his name to " + newName);

				try {
					ChatUser user = chatServer.findUserById(userId);
					user.setNickName(newName);

					Event nameChangedEvent = ChatServerImpl
							.createSetUserNameEvent(user);
					chatServer.server.sendEventToAll(nameChangedEvent);
				} catch (NoSuchUserException e) {
					L.log(Level.WARNING, "error processing set name event", e);
				}

				break;
			}
			case Chat.CREATE_ROOM: {
				// TODO: Save the owner of the room somewhere
				// int userId = (Integer) event.getArgument(0);
				String nameOfRoom = (String) event.getArgument(1);
				boolean isPublic = (Boolean) event.getArgument(2);

				try {
					chatServer.createRoom(nameOfRoom, isPublic);
				} catch (IllegalArgumentException | NotConnectedException e) {
					L.log(Level.WARNING, "error processing create room event",
							e);
				}
				break;
			}
			case Chat.JOIN_ROOM: {
				int userId = (Integer) event.getArgument(0);
				int roomId = (Integer) event.getArgument(1);

				try {
					ChatUser user;
					ChatRoom room;
					user = chatServer.findUserById(userId);
					room = chatServer.getRoomById(roomId);

					if (room.isPublic() && !room.containsUser(user)) {
						chatServer.addUserToRoom(user, room);
						Event confirmRoomJoinEvent = ChatServerImpl
								.createYouJoinedRoomEvent(room);
						chatServer.server.sendEventToClient(
								confirmRoomJoinEvent, userId);
					}
				} catch (NoSuchUserException | NoSuchRoomException
						| NotConnectedException | IllegalArgumentException
						| NoSuchClientException e) {
					L.log(Level.WARNING, "error processing join room event", e);
				}

				break;
			}
			case Chat.INVITE_USER: {
				int invitingUserId = (Integer) event.getArgument(0);
				int invitedUserId = (Integer) event.getArgument(1);
				int idOfRoom = (Integer) event.getArgument(2);

				try {
					ChatUser invitingUser;
					ChatUser invitedUser;
					ChatRoom room;
					invitingUser = chatServer.findUserById(invitingUserId);
					invitedUser = chatServer.findUserById(invitedUserId);
					room = chatServer.getRoomById(idOfRoom);
					chatServer.addInvitation(new Invitation(room, invitingUser,
							invitedUser));

					Event inviteUserEvent = new Event(Chat.INVITE_TO_ROOM);
					inviteUserEvent.putArgument(invitingUser);
					inviteUserEvent.putArgument(idOfRoom);
					chatServer.server.sendEventToClient(inviteUserEvent,
							invitedUserId);
				} catch (NoSuchUserException | NoSuchRoomException
						| IllegalArgumentException | NoSuchClientException e) {
					L.log(Level.WARNING, "error processing invite user event",
							e);
				}
				break;
			}
			case Chat.ACCEPT_INVITE: {
				int acceptingUserId = (Integer) event.getArgument(0);
				int roomId = (Integer) event.getArgument(1);
				int invitingUserId = (Integer) event.getArgument(2);

				try {
					ChatUser invitingUser;
					ChatUser invitedUser;
					ChatRoom room;
					invitingUser = chatServer.findUserById(invitingUserId);
					invitedUser = chatServer.findUserById(acceptingUserId);
					room = chatServer.getRoomById(roomId);

					Invitation acceptedInvitation = new Invitation(room,
							invitingUser, invitedUser);

					if (chatServer.checkAccept(acceptedInvitation)) {
						chatServer.addUserToRoom(invitedUser, room);
					}
				} catch (NoSuchUserException | NoSuchRoomException
						| IllegalArgumentException | NotConnectedException e) {
					L.log(Level.WARNING,
							"error processing accept invite event", e);
				}
				break;
			}
			}
		} catch (TooFewArgumentsExceptions e) {
			L.log(Level.WARNING, "error processing event", e);
		}
	}
}
