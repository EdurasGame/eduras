/**
 * 
 */
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

}
