package de.illonis.eduras.networking.discover;

import java.net.InetAddress;

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
	 * @param serverName
	 *            the server's name.
	 * 
	 * @param url
	 *            the server ip.
	 * 
	 * @param port
	 *            the port of server.
	 * 
	 * @author illonis
	 */
	void onServerFound(String serverName, InetAddress url, int port);

}
