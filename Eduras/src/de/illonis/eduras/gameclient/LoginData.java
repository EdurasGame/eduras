package de.illonis.eduras.gameclient;

import java.net.InetAddress;

import de.illonis.eduras.networking.ServerClient.ClientRole;

public final class LoginData {

	private final InetAddress address;
	private final int port;
	private final String username;
	private final ClientRole role;
	private String errorMessage;

	public LoginData(InetAddress address, int port, String username,
			ClientRole role) {
		super();
		errorMessage = "";
		this.address = address;
		this.port = port;
		this.username = username;
		this.role = role;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public ClientRole getRole() {
		return role;
	}

	public boolean validate() {
		errorMessage = "";
		if (username.trim().length() < 3)
			errorMessage += "username too short. ";
		if (port <= 0)
			errorMessage += "Invalid port: " + port + ". ";

		return errorMessage.isEmpty();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
