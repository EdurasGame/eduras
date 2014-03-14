package de.illonis.eduras.gameclient;

import java.net.InetAddress;

import de.illonis.eduras.networking.ClientRole;

/**
 * Holds login credentials.
 * 
 * @author illonis
 * 
 */
public final class LoginData {

	private final InetAddress address;
	private final int port;
	private final String username;
	private final ClientRole role;
	private String errorMessage;

	/**
	 * @param address
	 *            the server address to connect to.
	 * @param port
	 *            the server port.
	 * @param username
	 *            user's username.
	 * @param role
	 *            the clientrole of the user.
	 */
	public LoginData(InetAddress address, int port, String username,
			ClientRole role) {
		super();
		errorMessage = "";
		this.address = address;
		this.port = port;
		this.username = username;
		this.role = role;
	}

	/**
	 * @return the target server address.
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * @return the target server port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the clientrole.
	 */
	public ClientRole getRole() {
		return role;
	}

	/**
	 * Validates the hold login data for formal correctness.
	 * 
	 * @return true if data are valid, false otherwise.
	 */
	public boolean validate() {
		errorMessage = "";
		if (username.trim().length() < 3)
			errorMessage += "username too short. ";
		if (port <= 0)
			errorMessage += "Invalid port: " + port + ". ";

		return errorMessage.isEmpty();
	}

	/**
	 * Retrieves an error message if {@link #validate()} returned <b>false</b>.
	 * 
	 * @return the error message. Will be empty if validation succeeded.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

}
