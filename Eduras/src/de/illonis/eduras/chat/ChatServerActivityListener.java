package de.illonis.eduras.chat;

/**
 * Callback interface for certain events on the {@link ChatServer}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface ChatServerActivityListener {

	/**
	 * Is called when a user has connected.
	 * 
	 * @param user
	 *            The user that has connected.
	 */
	public void onUserConnected(ChatUser user);

	/**
	 * Is called when a user has disconnected.
	 * 
	 * @param user
	 *            The user that has disconnected.
	 */
	public void onUserDisconnected(ChatUser user);

}
