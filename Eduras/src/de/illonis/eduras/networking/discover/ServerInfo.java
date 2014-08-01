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

	private static final int NUMBER_OF_COMPONENTS = 7;
	private final int port;
	private final String name;
	private final InetAddress url;
	private final String version;
	private final String map;
	private final String gameMode;
	private final int numberOfPlayers;

	/**
	 * Creates a new serverinfo object.
	 * 
	 * @param name
	 *            name of server.
	 * @param url
	 *            url of server.
	 * @param port
	 *            port of server.
	 * @param version
	 *            version the server is running on
	 * @param numberOfPlayers
	 *            number of players on the server
	 * @param gameMode
	 *            game mode being played on the server
	 * @param mapName
	 *            name of the map being played on the server
	 */
	public ServerInfo(final String name, final InetAddress url, final int port,
			final String version, int numberOfPlayers, String gameMode,
			String mapName) {
		this.name = name;
		this.url = url;
		this.port = port;
		this.version = version;
		this.map = mapName;
		this.numberOfPlayers = numberOfPlayers;
		this.gameMode = gameMode;
	}

	/**
	 * Creates a new serverinfo object where only the InetAddress and the port
	 * are known and the rest is left blank.
	 * 
	 * @param url
	 * @param port
	 */
	public ServerInfo(final InetAddress url, final int port) {
		this("", url, port, "", 0, "", "");
	}

	/**
	 * @return the name of the discovered server.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the server's version (hash of most recent commit).
	 * 
	 * @return version
	 */
	public String getVersion() {
		return version;
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

	/**
	 * @return name of map being played
	 */
	public String getMap() {
		return map;
	}

	/**
	 * @return name of game mode being played
	 */
	public String getGameMode() {
		return gameMode;
	}

	/**
	 * @return number of players
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
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

	/**
	 * This method always returns the number of components a ServerInfo consists
	 * of and as such, how it is transmitted via network.
	 * 
	 * @return number of ServerInfo components
	 */
	public static int getNumberOfServerInfoComponents() {
		return NUMBER_OF_COMPONENTS;
	}
}
