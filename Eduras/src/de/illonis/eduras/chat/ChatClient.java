package de.illonis.eduras.chat;

import java.util.Collection;

import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;

/**
 * This defines the API of a chat client.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface ChatClient {

	/**
	 * Tries to post a message in the indicated chat room. All users in the
	 * indicated chat room will receive the message.
	 * 
	 * @param message
	 *            The message to post.
	 * @param chatRoom
	 *            The chat room to post the message in.
	 * @throws UserNotInRoomException
	 * @throws NotConnectedException
	 * @throws IllegalArgumentException
	 */
	public void postChatMessage(String message, ChatRoom chatRoom)
			throws UserNotInRoomException, IllegalArgumentException,
			NotConnectedException;

	/**
	 * Set's the users name globally. All other clients will be informed through
	 * the server.
	 * 
	 * @param newName
	 *            The name
	 * @throws NotConnectedException
	 * @throws IllegalArgumentException
	 */
	public void setName(String newName) throws IllegalArgumentException,
			NotConnectedException;

	/**
	 * Invite a user to the indicated room. Can only be called if you are in the
	 * same room.
	 * 
	 * @param user
	 *            The user to invite.
	 * @param chatRoom
	 *            The chat room to invite the user to.
	 * @throws UserNotInRoomException
	 * @throws NotConnectedException
	 * @throws IllegalArgumentException
	 */
	public void inviteUserToRoom(ChatUser user, ChatRoom chatRoom)
			throws UserNotInRoomException, IllegalArgumentException,
			NotConnectedException;

	/**
	 * Returns a collection of all rooms that are visible to the current client
	 * regardless of the user being in the chatroom.
	 * 
	 * @return All visible rooms.
	 */
	public Collection<ChatRoom> getRooms();

	/**
	 * Accept an invitation that has come in before. The client will then be
	 * notified that he has joined the chat room.
	 * 
	 * @param invitation
	 *            The invitation to accept.
	 * @throws TooFewArgumentsExceptions
	 * @throws IllegalArgumentException
	 * @throws NotConnectedException
	 */
	public void acceptInvite(Invitation invitation)
			throws IllegalArgumentException, TooFewArgumentsExceptions,
			NotConnectedException;

	/**
	 * Set the {@link ChatActivityListener} on the ChatClient.
	 * 
	 * @param listener
	 */
	public void setChatActivityListener(ChatActivityListener listener);

	/**
	 * Connect the client to a chat server. It will be in no chat room right
	 * after connection by default.
	 * 
	 * @param hostname
	 * @param port
	 */
	public void connect(String hostname, int port);

	/**
	 * Returns the respective user.
	 * 
	 * @return The user
	 */
	public ChatUser getUser();

}
