package de.illonis.eduras.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.GameReadyEvent;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.ServerNotReadyForStartException;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.networking.ServerClient.ClientRole;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.utils.CollectionUtils;

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
	/**
	 * Default server name.
	 */
	public final static String DEFAULT_NAME = "unnamed server";

	private final Buffer inputBuffer, outputBuffer;
	private final ServerSender serverSender;
	private final HashMap<Integer, ServerTCPReceiver> serverTCPReceivers;
	private ServerDecoder serverLogic;
	private GameInformation game;
	private GameLogicInterface logic;
	private final int port;
	private boolean running = true;
	private final String name;
	private UDPMessageReceiver serverUDPReceiver;

	/**
	 * Creates a new Server listening on default port.
	 * 
	 * @see Server#Server(String)
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
		this(port, DEFAULT_NAME);
	}

	/**
	 * Creates a new server that listens on default port.
	 * 
	 * @param serverName
	 *            name of the server.
	 */
	public Server(String serverName) {
		this(DEFAULT_PORT, serverName);
	}

	/**
	 * Creates a new server.
	 * 
	 * @param port
	 *            port server listens on.
	 * @param serverName
	 *            name of the server.
	 */
	public Server(int port, String serverName) {
		this.name = serverName;
		this.port = port;
		inputBuffer = new Buffer();
		outputBuffer = new Buffer();
		serverSender = new ServerSender(outputBuffer, null);
		serverTCPReceivers = new HashMap<Integer, ServerTCPReceiver>();
		serverUDPReceiver = new UDPMessageReceiver();
	}

	/**
	 * Receives UDP messages and processes them.
	 * 
	 * @author Florian Mai <florian.ren.mai@googlemail.com>
	 * 
	 */
	class UDPMessageReceiver extends Thread {

		private static final int MAX_UDP_SIZE = 1024;

		public UDPMessageReceiver() {
			super("UDPMessageReceiver");
		}

		@Override
		public void run() {
			DatagramSocket udpSocket = null;
			try {
				udpSocket = new DatagramSocket(port);
			} catch (SocketException e) {
				EduLog.error("UDP connection failed. See next exception.");
				EduLog.passException(e);
				stopServer();
			}

			while (running) {
				DatagramPacket packet = new DatagramPacket(
						new byte[MAX_UDP_SIZE], MAX_UDP_SIZE);
				try {
					udpSocket.receive(packet);
					String messages = new String(packet.getData(), 0,
							packet.getLength());
					inputBuffer.append(messages);
				} catch (IOException e) {
					EduLog.error("UDP connection to server closed. See next exception.");
					EduLog.passException(e);
					stopServer();
				}
			}
		}
	}

	/**
	 * @return name of this server.
	 */
	public String getName() {
		return name;
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
		serverUDPReceiver.start();

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
	 * @param eventListener
	 *            The listener
	 */
	public void setLogic(GameLogicInterface logic,
			NetworkEventListener eventListener) {
		this.logic = logic;
		logic.setGameEventListener(new ServerGameEventListener(outputBuffer,
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

		// inform client about clientconnection.
		ConnectionEstablishedEvent connectionEstablished = new ConnectionEstablishedEvent(
				client.getClientId());
		try {
			serverSender.sendMessageToClient(client.getClientId(),
					NetworkMessageSerializer.serialize(connectionEstablished));
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		}

		InitInformationEvent initInfo = getInitInfos(client);

		// extract role and name
		if (initInfo.getRole() == ClientRole.PLAYER) {
			logic.getGame().getGameSettings().getGameMode()
					.onConnect(client.getClientId());

			String playerName = initInfo.getName();
			try {
				logic.onGameEventAppeared(new ClientRenameEvent(client
						.getClientId(), playerName));
			} catch (InvalidNameException e) {
				EduLog.passException(e);
			}
		}

	}

	/**
	 * Waits for the client to send initial data and sends back game infos in
	 * case of success.
	 * 
	 * @param client
	 *            The client to get information from.
	 * @return Returns the client's role.
	 */
	private InitInformationEvent getInitInfos(ServerClient client) {
		synchronized (client) {
			// do init stuff
			BufferedReader inputStream = client.getInputStream();

			// wait for the client to pass information
			InitInformationEvent initInfoEvent;
			String initInformation;
			Object obj = null;
			try {

				initInformation = inputStream.readLine();
				LinkedList<Event> events = NetworkMessageDeserializer
						.deserialize(initInformation);

				obj = CollectionUtils.getElementOfClass(
						InitInformationEvent.class, events);
			} catch (IOException e1) {
				EduLog.passException(e1);
				return null;
			}

			if (obj != null) {
				initInfoEvent = (InitInformationEvent) obj;
			} else {
				return null;
			}

			if (initInfoEvent.getRole() == ClientRole.PLAYER) {
				ServerTCPReceiver sr = new ServerTCPReceiver(this, client);
				sr.start();
				serverTCPReceivers.put(client.getClientId(), sr);
			}

			// transfer information to client
			GameInfoRequest gameInfos = new GameInfoRequest(
					client.getClientId());
			logic.onGameEventAppeared(gameInfos);

			// inform client that the connectionprocess has finished
			GameReadyEvent gameReady = new GameReadyEvent();
			try {
				serverSender.sendMessageToClient(client.getClientId(),
						NetworkMessageSerializer.serialize(gameReady));
			} catch (MessageNotSupportedException e) {
				EduLog.passException(e);
			}

			// wake up receiver
			client.setConnected(true);
			client.notify();
			return initInfoEvent;
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
			setName("ConnectionListener");
			server = new ServerSocket(port);
		}

		/**
		 * Listens for new clients and passes them to client handler.
		 */
		@Override
		public void run() {
			EduLog.info(Localization
					.getStringF("Server.startedlistening", port));
			while (running) {
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
			try {
				server.close();
			} catch (IOException e) {
				EduLog.passException(e);
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
		serverTCPReceivers.get(client.getClientId()).stopRunning();
	}

	/**
	 * Sends an event to all clients.
	 * 
	 * @param event
	 *            The event to send to all clients.
	 */
	public void sendEventToAll(Event event) {
		try {
			serverSender.sendMessage(NetworkMessageSerializer.serialize(event));
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		}
	}

	/**
	 * Removes a player from the logic if the correlating client disconnected.
	 * 
	 * @param client
	 *            The client.
	 */
	public void handleClientDisconnect(ServerClient client) {
		removeClient(client);

		try {
			client.closeConnection();
		} catch (IOException e) {
			EduLog.passException(e);
		}

		ObjectFactoryEvent gonePlayerEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_REMOVE, ObjectType.PLAYER);

		int clientId = client.getClientId();
		int objectId;
		PlayerMainFigure mainFigure;
		try {
			mainFigure = game.getPlayerByOwnerId(clientId);
			objectId = mainFigure.getId();
		} catch (ObjectNotFoundException e) {
			EduLog.error("Player of object id " + e.getObjectId()
					+ " not found!");
			return;
		}

		gonePlayerEvent.setId(objectId);
		logic.getObjectFactory().onObjectFactoryEventAppeared(gonePlayerEvent);
		sendEventToAll(gonePlayerEvent);
	}

	/**
	 * Returns the outputbuffer of this server.
	 * 
	 * @return The outputbuffer.
	 */
	public Buffer getOutputBuffer() {
		return outputBuffer;
	}

	/**
	 * Here should something be done, for example to restart the server.
	 */
	public void onMatchEnd() {
		stopServer();
	}

	/**
	 * Stops the server.
	 */
	public void stopServer() {

		running = false;

		serverSender.stopSender();

		for (ServerTCPReceiver receiver : serverTCPReceivers.values()) {
			receiver.stopRunning();
			receiver.interrupt();
		}
	}

	/**
	 * Returns the serverreceivers.
	 * 
	 * @return The serverreceivers.
	 */
	HashMap<Integer, ServerTCPReceiver> getServerReceivers() {
		return serverTCPReceivers;
	}

	/**
	 * Returns the inputbuffer.
	 * 
	 * @return The inputbuffer.
	 */
	public Buffer getInputBuffer() {
		return inputBuffer;
	}

	/**
	 * Returns the server's logic
	 * 
	 * @return the logic.
	 */
	public GameLogicInterface getLogic() {
		return logic;
	}

	/**
	 * Returns the serverclient that handles given client.
	 * 
	 * @param ownerId
	 *            id of client.
	 * @return the client with given id.
	 * 
	 * @author illonis
	 */
	public ServerClient getClientById(int ownerId) {
		return serverSender.getClientById(ownerId);
	}

}
