package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ServerNotReadyForStartException;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;

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
	private final HashMap<Integer, ServerReceiver> serverReceivers;
	private ServerDecoder serverLogic;
	private GameInformation game;
	private GameLogicInterface logic;
	private final int port;

	/**
	 * Creates a new Server listening on default port.
	 */
	public Server() {
		this(DEFAULT_PORT);
	}

	/**
	 * Creates a new server using a custom port.
	 * 
	 * @param port
	 *            port to listen on.
	 */
	public Server(int port) {
		this.port = port;
		inputBuffer = new Buffer();
		outputBuffer = new Buffer();
		serverSender = new ServerSender(outputBuffer, null);
		serverReceivers = new HashMap<Integer, ServerReceiver>();

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
			EduLog.passException(e);
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
		logic.addGameEventListener(new ServerGameEventListener(outputBuffer,
				serverSender));
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
	 * @param clientSocket
	 *            Socket to handle.
	 * @throws IOException
	 */
	private void handleConnection(Socket clientSocket) throws IOException {
		ServerClient client = serverSender.add(clientSocket);

		ServerReceiver sr = new ServerReceiver(this, inputBuffer, client);
		sr.start();
		serverReceivers.put(client.getClientId(), sr);

		ConnectionEstablishedEvent connectionEstablished = new ConnectionEstablishedEvent(
				client.getClientId());
		try {
			serverSender.sendMessageToClient(client.getClientId(),
					NetworkMessageSerializer.serialize(connectionEstablished));
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		}

		ObjectFactoryEvent newPlayerEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, ObjectType.PLAYER);
		newPlayerEvent.setOwner(client.getClientId());
		logic.onGameEventAppeared(newPlayerEvent);

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
			server = new ServerSocket(port);
		}

		/**
		 * Listens for new clients and passes them to client handler.
		 */
		@Override
		public void run() {
			EduLog.info(Localization
					.getStringF("Server.startedlistening", port));
			while (true) {
				Socket client = null;
				try {
					client = server.accept();
					EduLog.info(Localization.getStringF("Server.newclient",
							client.getInetAddress()));
					handleConnection(client);
				} catch (IOException e) {
					EduLog.passException(e);
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
	void removeClient(ServerClient client) {
		serverSender.remove(client);
	}

	/**
	 * Sends an event to all clients.
	 * 
	 * @param event
	 *            The event to send to all clients.
	 */
	public void sendEventToAll(Event event) {
		String serializedEvent = "";
		try {
			serializedEvent = NetworkMessageSerializer.serialize(event);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
		serverSender.sendMessage(serializedEvent);
	}

	/**
	 * Removes a player from the logic if the correlating client disconnected.
	 * 
	 * @param client
	 *            The client's socket.
	 * @param clientId
	 *            The id of the client.
	 */
	public void handleClientDisconnect(ServerClient client) {
		removeClient(client);

		try {
			client.closeConnection();
		} catch (IOException e) {
			EduLog.passException(e);
		}

		serverReceivers.get(client.getClientId()).stopRunning();

		ObjectFactoryEvent gonePlayerEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_REMOVE, ObjectType.PLAYER);
		gonePlayerEvent.setId(client.getClientId());
		logic.onGameEventAppeared(gonePlayerEvent);
		sendEventToAll(gonePlayerEvent);
	}
}
