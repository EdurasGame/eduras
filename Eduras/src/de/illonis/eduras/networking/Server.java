package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.illonis.eduras.Game;
import de.illonis.eduras.Logic;

public class Server {

	/**
	 * Port where server listens for new clients.
	 */
	public final static int PORT = 4387;

	private Buffer inputBuffer, outputBuffer;
	private ServerSender serverSender;
	private ServerLogic serverLogic;

	public Server() {
		Game g = new Game();
		Logic logic = new Logic(g);
		inputBuffer = new Buffer();
		outputBuffer = new Buffer();
		serverLogic = new ServerLogic(inputBuffer, logic);

		serverSender = new ServerSender(this, outputBuffer);

		try {
			ConnectionListener cl = new ConnectionListener();
			cl.start();
		} catch (IOException e) {
			System.err.println("Could not start server. Quitting.");
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Handles a new connection and assigns a new ServerReceiver to it.
	 * 
	 * @param client
	 * @throws IOException
	 */
	private void handleConnection(Socket client) throws IOException {
		serverSender.add(client);

		ServerReceiver sr = new ServerReceiver(this, inputBuffer, client);
		sr.start();
	}

	/**
	 * A connection listener that listens for new connections and handles them.
	 * 
	 * @author illonis
	 * 
	 */
	private class ConnectionListener extends Thread {

		private final ServerSocket server;

		public ConnectionListener() throws IOException {
			server = new ServerSocket(PORT);
			startServing();
		}

		/**
		 * Start the whole thing with listening...
		 */
		private void startServing() {

			while (true) {
				Socket client = null;
				try {
					client = server.accept();
					handleConnection(client);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (client != null)
						try {
							client.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
	}

	public void removeClient(Socket client) {
		serverSender.remove(client);
	}

}
