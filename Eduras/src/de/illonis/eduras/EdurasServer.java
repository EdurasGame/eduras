package de.illonis.eduras;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.Client;
import de.eduras.eventingserver.ClientInterface;
import de.eduras.eventingserver.ClientNetworkEventHandler;
import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.Server;
import de.eduras.eventingserver.ServerInterface;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.eduras.remote.EncryptedRemoteServer;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.chat.ChatRoom;
import de.illonis.eduras.chat.ChatServer;
import de.illonis.eduras.chat.ChatServerActivityListener;
import de.illonis.eduras.chat.ChatServerImpl;
import de.illonis.eduras.chat.ChatUser;
import de.illonis.eduras.chat.NoSuchRoomException;
import de.illonis.eduras.chat.NoSuchUserException;
import de.illonis.eduras.chat.NotConnectedException;
import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.logic.ServerEventTriggerer;
import de.illonis.eduras.logic.ServerGameEventListener;
import de.illonis.eduras.logic.ServerLogic;
import de.illonis.eduras.maps.FunMap;
import de.illonis.eduras.networking.EventParser;
import de.illonis.eduras.networking.InetPolizei;
import de.illonis.eduras.networking.ServerNetworker;
import de.illonis.eduras.networking.discover.MetaServer;
import de.illonis.eduras.networking.discover.ServerDiscoveryListener;
import de.illonis.eduras.networking.discover.ServerSearcher;
import de.illonis.eduras.serverconsole.NoConsoleException;
import de.illonis.eduras.serverconsole.ServerConsole;

/**
 * The eduras server main executable.
 * 
 * @author illonis
 * 
 */
public class EdurasServer {

	private final static int DEFAULT_PORT = 4387;
	private final static String DEFAULT_NAME = "Eduras Server";
	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());
	private static final Level DEFAULT_LOGLIMIT = Level.WARNING;

	/**
	 * Starts an Eduras? server.
	 * 
	 * @param args
	 *            Arguments passed from console.
	 *            <ul>
	 *            <li><b>arg0:</b> custom server name.</li>
	 *            <li><b>arg1:</b> custom port to listen on.</li>
	 *            </ul>
	 */
	public static void main(String[] args) {
		SysOutCatcher.startCatching();
		SimpleDateFormat simpleDate = new SimpleDateFormat("y-M-d-H-m-s");

		try {
			EduLog.init(simpleDate.format(new Date()) + "-server.log", 2097152);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// arguments are of form <parametername>=<parametervalue>
		String[][] parametersWithValues = new String[args.length][2];

		for (int i = 0; i < args.length; i++) {
			parametersWithValues[i] = args[i].split("=");
		}

		// read arguments
		Level logLimit = DEFAULT_LOGLIMIT;
		int port = DEFAULT_PORT;
		String name = DEFAULT_NAME;
		boolean consoleOn = false;
		boolean registerAtMetaserver = false;
		for (int i = 0; i < args.length; i++) {

			String parameterName = parametersWithValues[i][0];
			String parameterValue = parametersWithValues[i][1];

			if (parameterName.equalsIgnoreCase("port")) {
				try {
					port = Integer.parseInt(parameterValue);
				} catch (NumberFormatException e) {
					L.severe(Localization.getStringF("Server.invalidportarg",
							parameterValue));
					return;
				}
				continue;
			}

			if (parameterName.equalsIgnoreCase("name")) {
				name = parameterValue;
			}

			if (parameterName.equalsIgnoreCase("registeratmetaserver")) {
				registerAtMetaserver = Boolean.parseBoolean(parameterValue);
			}

			if (parameterName.equalsIgnoreCase("loglimit")) {
				logLimit = Level.parse(parameterValue);
			}

			if (parameterName.equalsIgnoreCase("console")) {
				consoleOn = Boolean.parseBoolean(parameterValue);
			}
		}

		EduLog.setBasicLogLimit(logLimit);
		EduLog.setConsoleLogLimit(logLimit);

		L.info("Caching shapes...");
		GraphicsPreLoader.preLoadShapes();
		L.info("Caching shapes done.");

		L.info(Localization.getString("Server.startstart"));

		ServerInterface server = new Server();

		GameInformation gameInfo = new GameInformation();
		ServerLogic logic = new ServerLogic(gameInfo);

		logic.setGameEventListener(new ServerGameEventListener(server));
		ServerEventTriggerer eventTriggerer = new ServerEventTriggerer(logic,
				server);

		gameInfo.setEventTriggerer(eventTriggerer);

		server.setEventHandler(new EventParser(logic));

		final ChatServer chatServer = new ChatServerImpl();
		chatServer.start(port + 1);
		try {
			final ChatRoom matchChatRoom = chatServer.createRoom("MatchChat",
					true);
			chatServer
					.setChatServerActivityListener(new ChatServerActivityListener() {

						@Override
						public void onUserDisconnected(ChatUser user) {
							L.info("User " + user.getNickName()
									+ " disconnected from chat.");
						}

						@Override
						public void onUserConnected(ChatUser user) {
							L.info("User " + user.getNickName()
									+ " connected to chat.");
							try {
								chatServer.addUserToRoom(user, matchChatRoom);
							} catch (NotConnectedException
									| NoSuchUserException | NoSuchRoomException e) {
								L.info("Chat will not be started as the following exception occured: ");
								L.warning(e.getMessage());
								chatServer.stop();
							}
						}
					});

		} catch (IllegalArgumentException | NotConnectedException e) {
			L.log(Level.SEVERE, "An error appeared. Chat will not be working.",
					e);
		}

		server.setNetworkEventHandler(new ServerNetworker(gameInfo));
		server.setPolicy(new InetPolizei());

		eventTriggerer.changeMap(new FunMap());

		server.start(name, port);

		getInterfaces();
		ServerConsole console = new ServerConsole(new ConsoleEventTriggerer(
				eventTriggerer, server));

		if (consoleOn) {
			try {
				console.startCommandPrompt();
			} catch (NoConsoleException e) {
				L.log(Level.WARNING, "Could not find console", e);
			}
		}

		// TODO: use command line argument for remote server port.
		console.startRemoteServer(
				EncryptedRemoteServer.DEFAULT_REMOTE_SERVER_PORT, "password");

		ServerDiscoveryListener sdl = new ServerDiscoveryListener(
				server.getName(), port);
		sdl.start();

		if (registerAtMetaserver)
			registerAtMetaServer(server.getName(), "localhost", port);
	}

	private static void registerAtMetaServer(String nameOfServer,
			String ipOfServer, int portOfServer) {
		L.info("Registering at meta server.");
		ClientInterface metaServerClient = new Client();
		metaServerClient
				.setNetworkEventHandler(new ClientNetworkEventHandler() {

					@Override
					public void onClientDisconnected(int clientId) {
						// don't care
					}

					@Override
					public void onClientConnected(int clientId) {
						// don't care
					}

					@Override
					public void onServerIsFull() {
						// shouldn't appear
					}

					@Override
					public void onPingReceived(long latency) {
						// don't care
					}

					@Override
					public void onDisconnected() {
						L.info("Disconnected from MetaServer.");
					}

					@Override
					public void onConnectionLost() {
						L.warning("Lost connection to MetaServer.");
					}

					@Override
					public void onClientKicked(int clientId, String reason) {
						// don't care
					}
				});
		metaServerClient.connect(ServerSearcher.METASERVER_ADDRESS,
				ServerDiscoveryListener.META_SERVER_PORT);

		if (!metaServerClient.isConnected()) {
			L.warning("Could not connect to MetaServer. This server won't be discoverable via Internet.");
		}

		Event registerEvent = new Event(MetaServer.REGISTER_REQUEST);
		registerEvent.putArgument(metaServerClient.getClientId());
		registerEvent.putArgument(nameOfServer);
		registerEvent.putArgument(ipOfServer);
		registerEvent.putArgument(portOfServer);
		try {
			metaServerClient.sendEvent(registerEvent);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.log(Level.WARNING, "Cannot register at metaserver.", e);
			return;
		}
	}

	/**
	 * Lists all IPv4 addresses server is reachable at.
	 */
	public static void getInterfaces() {

		ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();
		try {
			Enumeration<NetworkInterface> e = NetworkInterface
					.getNetworkInterfaces();

			while (e.hasMoreElements()) {

				NetworkInterface ni = e.nextElement();

				Enumeration<InetAddress> e2 = ni.getInetAddresses();

				while (e2.hasMoreElements()) {
					InetAddress ip = e2.nextElement();
					if (ip instanceof Inet4Address)
						addresses.add(ip);
				}
			}
		} catch (SocketException e) {
			L.log(Level.SEVERE, Localization.getString("Server.noaddresses"), e);
			return;
		}

		StringBuilder b = new StringBuilder(
				Localization.getString("Server.reachable") + " ");
		for (InetAddress inetAddress : addresses) {
			b.append(inetAddress.toString().substring(1));
			b.append(", ");
		}
		L.info(b.toString());
	}
}
