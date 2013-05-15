package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class represents a client from the view of the server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerClient {

	private final int clientId;
	private final Socket socket;
	private final PrintWriter printWriter;
	private final BufferedReader bufferedReader;
	private boolean connected;

	/**
	 * Specifies the role of the client. If the client states to be spectator,
	 * it will receive all the info a player-client also gets, but it can't pass
	 * information to the server.
	 * 
	 * @author Florian Mai <florian.ren.mai@googlemail.com>
	 * 
	 */
	public enum ClientRole {

		/**
		 * A player can actively play.
		 */
		PLAYER(0),
		/**
		 * a spectator can only spectate and not interact with game.
		 */
		SPECTATOR(1);

		private int typeNumber;

		/**
		 * Returns the {@link ClientRole} that has given number.
		 * 
		 * @param clientRoleNum
		 *            the number of clientrole.
		 * @return the role.
		 * 
		 * @author illonis
		 */
		public static ClientRole getValueOf(int clientRoleNum) {
			switch (clientRoleNum) {
			case 0:
				return ClientRole.PLAYER;
			case 1:
				return ClientRole.SPECTATOR;
			default:
				return null;
			}
		}

		private ClientRole(int type) {
			typeNumber = type;
		}

		/**
		 * Returns the type number of this role.
		 * 
		 * @return the type number.
		 * 
		 * @author illonis
		 */
		public int getTypeNum() {
			return typeNumber;
		}

	}

	/**
	 * Creates a new ServerClient with the given id and that uses the given
	 * socket.
	 * 
	 * @param clientId
	 *            The client's id.
	 * @param socket
	 *            The client's socket.
	 * @throws IOException
	 *             Is thrown if the socket is somehow broken.
	 */
	ServerClient(int clientId, Socket socket) throws IOException {
		this.clientId = clientId;
		this.socket = socket;

		this.printWriter = new PrintWriter(socket.getOutputStream(), true);
		this.bufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		connected = false;

	}

	/**
	 * Returns the id of the client.
	 * 
	 * @return The client's id.
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * Returns the socket that belongs to the client.
	 * 
	 * @return The client's socket.
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Closes the socket.
	 * 
	 * @throws IOException
	 */
	public void closeConnection() throws IOException {
		socket.close();
	}

	/**
	 * Returns a printwriter that sends on the socket.
	 * 
	 * @return The printwriter.
	 */
	public PrintWriter getOutputStream() {
		return printWriter;
	}

	/**
	 * Returns a bufferedReader to read from the socket of the client.
	 * 
	 * @return The buffered reader.
	 */
	public BufferedReader getInputStream() {
		return bufferedReader;
	}

	/**
	 * Returns the host address of the client as string.
	 * 
	 * @return The client's host address.
	 */
	public String getHostAddress() {
		return socket.getInetAddress().getHostAddress();
	}

	/**
	 * Returns whether the client is connected or not.
	 * 
	 * @return True if connected, false otherwise.
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Sets the connected status.
	 * 
	 * @param connected
	 *            The new status.
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

}
