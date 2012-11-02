package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ServerNotReadyForStartException;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.locale.Localization;

/**
 * A server that handles a game and its clients.
 * 
 * @author illonis
 * 
 *         (fma)The common workflow for the server is as follows:<br>
 *         First you use the default constructor to create a new server. <br>
 *         Then you set the initial game and the logic (must implement
 *         GameLogicInterface). <br>
 *         At last use start() to make your server listen to clients and start
 *         working.(/fma)
 * 
 */
public class Server {

	/**
	 * Default port where server listens for new clients.
	 */
	public final static int DEFAULT_PORT = 4387;

	private final Buffer inputBuffer, outputBuffer;
	private final ServerSender serverSender;
	private ServerDecoder serverLogic;
	private GameInformation game;
	private GameLogicInterface logic;

	/**
	 * Creates a new server, that is not started yet.
	 */
	public Server() {
		inputBuffer = new Buffer();
		outputBuffer = new Buffer();
		serverSender = new ServerSender(this, outputBuffer);

	}

	/**
	 * (fma)Starts the server.
	 * 
	 * @throws ServerNotReadyForStartException
	 *             Thrown if the game or the serverlogic has not been set
	 *             before.
	 */
	public void start() throws ServerNotReadyForStartException {

		if (game == null || serverLogic == null) {
			throw new ServerNotReadyForStartException();
		}

		serverLogic.start();
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
	 * (fma)Set the game info.
	 * 
	 * @param game
	 *            The game.
	 */
	public void setGame(GameInformation game) {
		this.game = game;
	}

	/**
	 * (fma)Set the logic and networkEventListener the server uses.
	 * 
	 * @param logic
	 *            The logic.
	 * @param listener
	 *            The listener
	 */
	public void setLogic(GameLogicInterface logic,
			NetworkEventListener eventListener) {
		this.logic = logic;
		logic.addGameEventListener(new ServerGameEventListener(outputBuffer));
		serverLogic = new ServerDecoder(inputBuffer, logic, eventListener);
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
		int clientId = serverSender.add(client);

		ObjectFactoryEvent newPlayerEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, ObjectType.PLAYER);
		newPlayerEvent.setId(clientId);
		logic.onGameEventAppeared(newPlayerEvent);

		ServerReceiver sr = new ServerReceiver(this, inputBuffer, client);
		sr.start();

		ConnectionEstablishedEvent connectionEstablished = new ConnectionEstablishedEvent(
				clientId);
		try {
			serverSender.sendMessageToClient(clientId,
					NetworkMessageSerializer.serialize(connectionEstablished));
		} catch (MessageNotSupportedException e) {
			System.out.println(e.getEventMessage());
			System.out
					.println("For type: " + e.getGameEventNumber().toString());
			e.printStackTrace();
		}
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

}
