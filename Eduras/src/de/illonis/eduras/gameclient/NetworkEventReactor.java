package de.illonis.eduras.gameclient;

/**
 * Reacts to an network event.
 * 
 * @author illonis
 * 
 */
public interface NetworkEventReactor {

	/**
	 * 
	 * Indicates that a client's network connection was successfully
	 * established.
	 * 
	 * @param clientId
	 *            id of client that connected.
	 */
	void onConnected(int clientId);

	/**
	 * Indicates that network connection was lost. This is fired when connection
	 * closed gracefully.
	 */
	void onConnectionLost();

	/**
	 * Indicates that client wants to disconnect. Write your
	 * disconnect-procedure here!
	 */
	void onDisconnect();

	/**
	 * Indicates that the player object that is assigned to this client was
	 * received.
	 */
	void onPlayerReceived();

	/**
	 * Indicates that the game client was successfully registered on server and
	 * the game is ready.
	 */
	void onGameReady();
}
