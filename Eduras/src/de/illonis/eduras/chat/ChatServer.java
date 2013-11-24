package de.illonis.eduras.chat;

import java.util.Collection;

/**
 * Defines the API for the chat server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface ChatServer {

	/**
	 * Start listening for and serving clients on the given port.
	 * 
	 * @param port
	 *            The port.
	 * @return A success flag. Returns true if everything goes well and false
	 *         otherwise.
	 */
	public boolean start(int port);

	/**
	 * Stop the server.
	 */
	public void stop();

	/**
	 * Tells whether the server is running.
	 * 
	 * @return True if it is running.
	 */
	public boolean isRunning();

	/**
	 * Returns all users that are currently connected to the server, regardless
	 * of if they are in a room or not.
	 * 
	 * @return The users
	 */
	public Collection<ChatUser> getUsers();

	/**
	 * Returns all rooms that are currently open regardless of visibility.
	 * 
	 * @return A collection of all rooms.
	 */
	public Collection<ChatRoom> getRooms();

	/**
	 * Disconnects the given user from the server, that is, it will leave all
	 * chat rooms it is currently in and the connection gets closed. All users
	 * that were in a room with the disconnected user will be notified.
	 * 
	 * @param user
	 *            The user to disconnect.
	 * @return Success flag, returns false if for example the user is not
	 *         connected anymore.
	 */
	public boolean disconnectUser(ChatUser user);

	/**
	 * Creates a chat room. Note that this is different to a user creating a
	 * room.
	 * 
	 * @param name
	 *            Name of the room to create.
	 * @param isPublic
	 *            Indicates whether the room will be public or not.
	 * @return Returns the chat room that was created.
	 */
	public ChatRoom createRoom(String name, boolean isPublic);

	/**
	 * Adds the given user to the given room. Both the user and all the users in
	 * the given room will be informed.
	 * 
	 * @param room
	 *            The room to add the user to.
	 * @param user
	 *            The user to add to the room.
	 */
	public void addUserToRoom(ChatUser user, ChatRoom room);

	/**
	 * Removes the given user from the given room. Both the respective user and
	 * all the users in the given room will be informed.
	 * 
	 * @param user
	 *            The user to remove from the given room.
	 * @param room
	 *            The room to remove the user from.
	 */
	public void removeUserFromRoom(ChatUser user, ChatRoom room);

	/**
	 * Removes all users in the given room from the room first and then deletes
	 * the room itself. The respective users will be informed.
	 * 
	 * @param room
	 *            The room to remove.
	 */
	public void removeRoom(ChatRoom room);

	/**
	 * Creates a server.
	 * 
	 * @return The created server
	 */
	public ChatServer create();
}
