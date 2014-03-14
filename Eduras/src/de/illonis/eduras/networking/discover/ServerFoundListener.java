package de.illonis.eduras.networking.discover;

/**
 * Provides methods for async callback when searching for servers.
 * 
 * @author illonis
 * 
 */
public interface ServerFoundListener {

	/**
	 * Called when a server was found.
	 * 
	 * @param info
	 *            an object holding server information.
	 */
	void onServerFound(ServerInfo info);

	/**
	 * Called when an error occured during discovery.
	 * 
	 * @author illonis
	 */
	void onDiscoveryFailed();
}
