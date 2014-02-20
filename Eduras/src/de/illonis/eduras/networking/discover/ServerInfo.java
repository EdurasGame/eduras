package de.illonis.eduras.networking.discover;

import java.net.InetAddress;

/**
 * Holds information about a discovered server that allow a client to connect to
 * that server.
 * 
 * @author illonis
 * 
 */
public class ServerInfo {

	private final int port;
	private final String name;
	private final InetAddress url;

	/**
	 * Creates a new serverinfo object.
	 * 
	 * @param name
	 *            name of server.
	 * @param url
	 *            url of server.
	 * @param port
	 *            port of server.
	 */
	public ServerInfo(final String name, final InetAddress url, final int port) {
		this.name = name;
		this.url = url;
		this.port = port;
	}

	/**
	 * @return the name of the discovered server.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the port of the discovered server.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the url of the discovered server.
	 */
	public InetAddress getUrl() {
		return url;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ServerInfo) {
			ServerInfo other = (ServerInfo) obj;
			return other.getUrl().equals(getUrl())
					&& getPort() == other.getPort();
		}
		return false;
	}
}
