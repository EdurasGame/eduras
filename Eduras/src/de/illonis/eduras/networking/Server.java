package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.illonis.eduras.Game;
import de.illonis.eduras.Logic;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.locale.Localization;

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
		Player obj = new Player(game);
		game.setPlayer1(obj);
		Logic logic = new Logic(game);
		logic.addGameEventListener(this);
		inputBuffer = new Buffer();
		outputBuffer = new Buffer();
		serverLogic = new ServerLogic(inputBuffer, logic);
		serverLogic.start();
		serverSender = new ServerSender(this, outputBuffer);
		serverSender.start();

		try {
			ConnectionListener cl = new ConnectionListener();
			cl.start();
		} catch (IOException e) {
			System.err.println(Localization.getString("Server.startuperror")); //$NON-NLS-1$
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * <b>Depeciated:</b> It is no longer neccessary to wake serverlogic as it
	 * waits until a element becomes available of its own.<br>
	 * <br>
	 * <s>Notifies ServerLogik that there are new messages to parse.</s>
	 */
	@Deprecated
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
		}

		/**
		 * Listens for new clients and passes them to client handler.
		 */
		@Override
		public void run() {
			System.out.println(Localization.getStringF(
					"Server.startedlistening", DEFAULT_PORT));
			while (true) {
				Socket client = null;
				try {
					client = server.accept();
					System.out.println(Localization
							.getString("Server.newclient")); //$NON-NLS-1$
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
			me = new MovementEvent(GameEventNumber.SET_POS, game.getPlayer1()
					.getId());
			me.setNewXPos((int) (game.getPlayer1().getXPosition() + 20));
			me.setNewYPos((int) (game.getPlayer1().getYPosition() + 20));
			String msg = NetworkMessageSerializer.serialize(me);
			outputBuffer.append(msg);
		} catch (GivenParametersDoNotFitToEventException e) {
			e.printStackTrace();
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}

	}

}
