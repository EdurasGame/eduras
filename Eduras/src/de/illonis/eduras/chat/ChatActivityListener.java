package de.illonis.eduras.chat;

/**
 * Implement this interface and set your implementation on the
 * {@link ChatServer}.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface ChatActivityListener {

	/**
	 * Gets called when some user posted a message in a room that you are in.
	 * 
	 * @param message
	 */
	void onNewMessage(ChatMessage message);

	/**
	 * Gets called when a user has changed his name.
	 * 
	 * @param user
	 *            The user that has changed its name.
	 */
	void onNameChanged(ChatUser user);

	/**
	 * Gets called when the client received an invitation to a chat room.
	 * 
	 * @param invitation
	 */
	void onInviteReceived(Invite invitation);

	/**
	 * Gets called when the client has joined a chat room.
	 * 
	 * @param chatRoom
	 *            The chat room joined to by the user.
	 */
	void onYouJoined(ChatRoom chatRoom);

	/**
	 * Gets called when any user has joined a chat room that the current client
	 * is in.
	 * 
	 * @param chatRoom
	 *            The chat room the user joined in.
	 * @param user
	 *            The user which joined the chat room.
	 */
	void onUserJoinedRoom(ChatRoom chatRoom, ChatUser user);
}
