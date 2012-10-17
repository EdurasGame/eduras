package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.illonis.eduras.Game;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.Logic;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * A server that handles a game and its clients.
 * 
 * @author illonis
 * 
 */
public class Server implements GameEventListener {

	/**
	 * Default port where server listens for new clients.
	 */
	public final static int DEFAULT_PORT = 4387;

	private final Buffer inputBuffer, outputBuffer;
	private final ServerSender serverSender;
	private final ServerLogic serverLogic;
	private final Game game;

	public Server() {
		game = new Game();
		GameObject obj = new GameObject();
		game.setPlayer1(obj);
		Logic logic = new Logic(game);
		logic.addGameEventListener(this);
		inputBuffer = new Buffer();
		outputBuffer = new Buffer();
		serverLogic = new ServerLogic(inputBuffer, logic);
		serverLogic.start();
		serverSender = new ServerSender(this, outputBuffer);

		try {
			ConnectionListener cl = new ConnectionListener();
			cl.start();
		} catch (IOException e) {
			System.err.println("[SERVER] Could not start server. Quitting.");
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Notifies ServerLogik that there are new messages to parse.
	 */
	public void wakeLogic() {
		synchronized (serverLogic) {
			serverLogic.notify();
		}
	}

	/**
	 * Handles a new connection and assigns a new ServerReceiver to it.
	 * 
	 * @param client
	 *            Socket to handle.
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
			server = new ServerSocket(DEFAULT_PORT);
			startServing();
		}

		/**
		 * Listens for new clients and passes them to client handler.
		 */
		private void startServing() {
			System.out.println("[SERVER] Listening on " + DEFAULT_PORT);
			while (true) {
				Socket client = null;
				try {
					client = server.accept();
					System.out.println("[SERVER] New client...");
					handleConnection(client);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Removes client from serversender.
	 * 
	 * @param client
	 *            Client to remove.
	 */
	public void removeClient(Socket client) {
		serverSender.remove(client);
	}

	@Override
	public void onWorldChanged() {
		MovementEvent me;
		try {
			me = new MovementEvent(GameEventNumber.MOVE_POS, game.getPlayer1()
					.getId());
			me.setNewXPos(game.getPlayer1().getXPosition() + 20);
			me.setNewYPos(game.getPlayer1().getYPosition() + 20);
			String msg = NetworkMessageSerializer.serialize(me);
			outputBuffer.append(msg);
		} catch (GivenParametersDoNotFitToEventException e) {
			e.printStackTrace();
		}

	}

}
