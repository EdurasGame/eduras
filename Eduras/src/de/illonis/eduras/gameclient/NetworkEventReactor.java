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
	 * Indicates that network connection of a client was lost. This is fired
	 * when connection closed gracefully.
	 * 
	 * @param client
	 *            the client that lost connection.
	 */
	void onConnectionLost(int client);

	/**
	 * Indicates that a client has disconnected from server.
	 * 
	 * @param client
	 *            the client that quit session.
	 */
	void onDisconnect(int client);

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

	/**
	 * Indicates that the server successfully received the initial UDP message
	 * from the client.
	 * 
	 * @param clientId
	 *            The id of the client.
	 */
	void onUDPReady(int clientId);
}
