package de.illonis.eduras;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
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
import de.illonis.eduras.exceptions.EdurasUncaughtExceptionHandler;
import de.illonis.eduras.exceptions.NoSuchGameModeException;
import de.illonis.eduras.exceptions.NoSuchMapException;
import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
import de.illonis.eduras.gamemodes.BasicGameMode;
import de.illonis.eduras.gamemodes.Deathmatch;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.logic.ServerEventTriggerer;
import de.illonis.eduras.logic.ServerGameEventListener;
import de.illonis.eduras.logic.ServerLogic;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.persistence.InvalidDataException;
import de.illonis.eduras.networking.EventParser;
import de.illonis.eduras.networking.InetPolizei;
import de.illonis.eduras.networking.ServerNetworker;
import de.illonis.eduras.networking.discover.MetaServer;
import de.illonis.eduras.networking.discover.ServerDiscoveryListener;
import de.illonis.eduras.networking.discover.ServerSearcher;
import de.illonis.eduras.serverconsole.NoConsoleException;
import de.illonis.eduras.serverconsole.ServerConsole;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.utils.ReflectionTools;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.WebFetcher;

/**
 * This is the main class to configure and start an Eduras? server.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EdurasServer {

	public final static int DEFAULT_PORT = 4387;
	private final static String DEFAULT_NAME = "Eduras Server";
	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());
	private static final Level DEFAULT_LOGLIMIT = Level.WARNING;

	private final ServerInterface server;
	private final ChatServer chatServer;

	private String serverHostAddress;
	private boolean registerAtMetaserver;

	private int edurasPort;
	private String name;

	private ServerConsole console;
	private boolean localConsoleOn;

	private int remoteConsolePort;
	private boolean remoteConsoleOn;
	private String remoteConsolePassword;

	private Map startMap;
	private String startGameMode;
	private File startSettings;
	private int maxNumberOfClients;

	private ServerEventTriggerer eventTriggerer;

	/**
	 * Create a new EdurasServer with default settings.
	 */
	public EdurasServer() {
		server = new Server();
		chatServer = new ChatServerImpl();
		init();
	}

	private void init() {

		edurasPort = DEFAULT_PORT;
		name = DEFAULT_NAME;

		registerAtMetaserver = false;
		serverHostAddress = "";

		localConsoleOn = false;

		remoteConsoleOn = true;
		remoteConsolePort = EncryptedRemoteServer.DEFAULT_REMOTE_SERVER_PORT;
		remoteConsolePassword = "password";

		maxNumberOfClients = 11;
	}

	/**
	 * Returns the hostaddress to be registered at the metaserver.
	 * 
	 * @return hostaddress
	 */
	public String getServerHostAddress() {
		return serverHostAddress;
	}

	/**
	 * Sets the hostaddress to be registered at the metaserver.
	 * 
	 * @param serverHostAddress
	 *            The hostaddress
	 */
	public void setServerHostAddress(String serverHostAddress) {
		this.serverHostAddress = serverHostAddress;
	}

	/**
	 * Tells whether the Eduras? server registeres at the metaserver when
	 * starting.
	 * 
	 * @return True if it does register at the metaserver.
	 */
	public boolean isRegisterAtMetaserver() {
		return registerAtMetaserver;
	}

	/**
	 * If you pass true, the Eduras? server will register at the metaserver.
	 * 
	 * @param registerAtMetaserver
	 */
	public void setRegisterAtMetaserver(boolean registerAtMetaserver) {
		this.registerAtMetaserver = registerAtMetaserver;
	}

	/**
	 * Sets the maximum number of players that are allowed to join the server.
	 * 
	 * @param maxPlayers
	 */
	public void setMaxPlayers(int maxPlayers) {
		this.maxNumberOfClients = maxPlayers;
	}

	/**
	 * Returns the maximum number of players that are allowed to connect to the
	 * server.
	 * 
	 * @return max number of players
	 */
	public int getMaxPlayers() {
		return maxNumberOfClients;
	}

	/**
	 * Returns the port the Eduras? server will be running on.
	 * 
	 * @return The portnumber
	 */
	public int getEdurasPort() {
		return edurasPort;
	}

	/**
	 * Sets the port the Eduras? server will be running on.
	 * 
	 * @param edurasPort
	 */
	public void setEdurasPort(int edurasPort) {
		this.edurasPort = edurasPort;
	}

	/**
	 * Returns the name the Eduras? server will be displayed under.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name the Eduras? server will be displayed under.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Tells if a local console will be run.
	 * 
	 * @return True if so.
	 */
	public boolean isLocalConsoleOn() {
		return localConsoleOn;
	}

	/**
	 * Pass true if you want a local console to be run on the Eduras? server.
	 * 
	 * @param localConsoleOn
	 */
	public void setLocalConsoleOn(boolean localConsoleOn) {
		this.localConsoleOn = localConsoleOn;
	}

	/**
	 * Returns the number of the port the remote console will be running on.
	 * 
	 * @return remote console port
	 */
	public int getRemoteConsolePort() {
		return remoteConsolePort;
	}

	/**
	 * Sets the number of the port the remote console will be running on.
	 * 
	 * @param remoteConsolePort
	 */
	public void setRemoteConsolePort(int remoteConsolePort) {
		this.remoteConsolePort = remoteConsolePort;
	}

	/**
	 * Tells if a remote console will be run on the Eduras? server.
	 * 
	 * @return True if so.
	 */
	public boolean isRemoteConsoleOn() {
		return remoteConsoleOn;
	}

	/**
	 * Pass true if you want a remote console to be run on the Eduras? server.
	 * 
	 * @param remoteConsoleOn
	 */
	public void setRemoteConsoleOn(boolean remoteConsoleOn) {
		this.remoteConsoleOn = remoteConsoleOn;
	}

	/**
	 * Returns the remote console's password.
	 * 
	 * @return password
	 */
	public String getRemoteConsolePassword() {
		return remoteConsolePassword;
	}

	/**
	 * Sets the remote console's password.
	 * 
	 * @param remoteConsolePassword
	 */
	public void setRemoteConsolePassword(String remoteConsolePassword) {
		this.remoteConsolePassword = remoteConsolePassword;
	}

	/**
	 * Returns the name of the map that is initially started.
	 * 
	 * @return name of the map
	 */
	public String getStartMap() {
		return startMap.getName();
	}

	/**
	 * Sets the map of the server on startup.
	 * 
	 * @param startMap
	 *            Name of the map.
	 * @throws NoSuchMapException
	 *             Thrown if the map is unknown.
	 * @throws InvalidDataException
	 *             if loading from file failed due to syntax error in mapfile.
	 */
	public void setStartMap(String startMap) throws NoSuchMapException,
			InvalidDataException {
		this.startMap = Map.getMapByName(startMap);
	}

	/**
	 * Returns the name of the game mode that is initially started.
	 * 
	 * @return name of game mode
	 */
	public String getStartGameMode() {
		return startGameMode;
	}

	/**
	 * Sets the gamemode of the Eduras? server on startup.
	 * 
	 * @param startGameMode
	 *            name of game mode
	 */
	public void setStartGameMode(String startGameMode) {
		this.startGameMode = startGameMode;
	}

	/**
	 * Returns the a file containing the settings set on server startup.
	 * 
	 * @return settings-file
	 */
	public File getStartSettings() {
		return startSettings;
	}

	/**
	 * Set the settings file that will be loaded when server starts.
	 * 
	 * @param startSettings
	 */
	public void setStartSettings(File startSettings) {
		this.startSettings = startSettings;
	}

	/**
	 * Runs the server with the configurations set on the EdurasServer before.
	 * 
	 * @throws NoSuchGameModeException
	 *             Thrown if the game mode set on the server is unknown.
	 */
	public void runServer() throws NoSuchGameModeException {
		L.info("Caching shapes.");
		GraphicsPreLoader.preLoadShapes();

		L.info(Localization.getString("Server.startstart"));

		setupEdurasServer();
		setupChatServer();

		if (localConsoleOn) {
			try {
				console.startCommandPrompt();
			} catch (NoConsoleException e) {
				L.log(Level.WARNING, "Could not find console", e);
			}
		}

		if (remoteConsoleOn) {
			console.startRemoteServer(remoteConsolePort, remoteConsolePassword);
		}

		ServerDiscoveryListener sdl = new ServerDiscoveryListener(
				server.getName(), edurasPort, eventTriggerer.getGameInfo());
		sdl.start();

		if (registerAtMetaserver) {
			registerAtMetaserver();
		}
	}

	private void registerAtMetaserver() {
		if (serverHostAddress.equals("")) {
			L.info("No IP was specified under which the Eduras server is supposed to register itself at the meta server.");
			try {
				L.info("Fetching WAN-IP...");
				serverHostAddress = WebFetcher.get(new URL(
						"http://www.icanhazip.com"),
						new HashMap<String, String>());
				L.info("WAN-IP is " + serverHostAddress);
			} catch (IOException e) {
				L.log(Level.WARNING, "Cannot fetch IP for server.", e);
			}

		}
		new MetaServerRegisterer(name, serverHostAddress, edurasPort,
				eventTriggerer.getGameInfo()).start();

	}

	private void setupChatServer() {
		chatServer.start(edurasPort + 1);
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
	}

	private void setupEdurasServer() throws NoSuchGameModeException {

		GameInformation gameInfo = new GameInformation();
		ServerLogic logic = new ServerLogic(gameInfo);
		logic.setGameEventListener(new ServerGameEventListener(server));
		eventTriggerer = new ServerEventTriggerer(logic, server);
		gameInfo.setEventTriggerer(eventTriggerer);

		server.setMaximumClients(maxNumberOfClients);
		server.setEventHandler(new EventParser(logic));
		server.setNetworkEventHandler(new ServerNetworker(gameInfo));
		server.setPolicy(new InetPolizei());
		server.start(name, edurasPort);

		console = new ServerConsole(new ConsoleEventTriggerer(eventTriggerer,
				server));

		switchToStartSettings();
		switchToStartMap();
		switchToStartGameMode();

		getInterfaces();

	}

	private void switchToStartSettings() {
		if (startSettings != null) {
			eventTriggerer.loadSettings(startSettings);
		}
	}

	private void switchToStartMap() {
		if (startMap == null) {
			try {
				setStartMap("funmap");
			} catch (NoSuchMapException e) {
				L.log(Level.SEVERE, "Could not find default map (funmap)", e);
				System.exit(-1);
			} catch (InvalidDataException e) {
				L.log(Level.SEVERE, "Could not load default map (funmap)", e);
				System.exit(-1);
			}
		}
		eventTriggerer.changeMap(startMap);
		eventTriggerer.restartGame();
	}

	private void switchToStartGameMode() throws NoSuchGameModeException {
		if (startGameMode == null) {
			eventTriggerer.changeGameMode(new Deathmatch(eventTriggerer
					.getGameInfo()));
		} else {
			eventTriggerer.changeGameMode(BasicGameMode.getGameModeByName(
					startGameMode, eventTriggerer.getGameInfo()));
		}
	}

	/**
	 * Starts an Eduras? server.
	 * 
	 * @param args
	 *            Arguments passed from console. Put them in the form
	 *            "parametername1=parametervalue1 parametername2=parametervalue2 ..."
	 *            where parameternameX is one of the following
	 *            <ul>
	 *            <li><b>port:</b> Port of the Eduras? server.</li>
	 *            <li><b>name:</b> Name of Eduras? server.</li>
	 *            <li><b>serverhostaddress:</b> The address under which the
	 *            server shall register itself at the metaserver.</li>
	 *            <li><b>registeratmetaserver:</b> Tells whether the server
	 *            shall register at the metaserver. Values: true/false.</li>
	 *            <li><b>loglimit:</b> Set the loglimit for all outputs.</li>
	 *            <li><b>localconsole:</b> Tells whether the local console shall
	 *            be run or not. Values: true/false.</li>
	 *            <li><b>remoteconsoleport:</b> Sets the port of the
	 *            remoteconsole.</li>
	 *            <li><b>remoteconsole:</b> Determines whether a remoteconsole
	 *            shall be run or not.</li>
	 *            <li><b>remoteconsolepassword:</b> Sets the password of the
	 *            remoteconsole.</li>
	 *            <li><b>startmap:</b> Sets the map the server is started with.</li>
	 *            <li><b>startgamemode:</b> Sets the gamemode the server is
	 *            started with.</li>
	 *            <li><b>startconfig:</b> Path to config file to be loaded
	 *            initially.</li>
	 *            </ul>
	 */
	public static void main(String[] args) {
		SimpleDateFormat simpleDate = new SimpleDateFormat("y-M-d-H-m-s");

		try {
			EduLog.init(new File("logs").getAbsolutePath(),
					simpleDate.format(new Date()) + "-server.log", 2097152);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread.setDefaultUncaughtExceptionHandler(new EdurasUncaughtExceptionHandler(
				L));

		EdurasServer edurasServer = new EdurasServer();

		try {
			ResourceManager.extractMaps();
		} catch (IOException e1) {
			L.log(Level.SEVERE, "Could not extract resources.", e1);
			return;
		}

		// arguments are of form <parametername>=<parametervalue>
		String[][] parametersWithValues = new String[args.length][2];

		for (int i = 0; i < args.length; i++) {
			parametersWithValues[i] = args[i].split("=");
		}

		// read arguments
		Level logLimit = DEFAULT_LOGLIMIT;
		for (int i = 0; i < args.length; i++) {

			String parameterName = parametersWithValues[i][0];
			String parameterValue = parametersWithValues[i][1];

			switch (parameterName.toLowerCase()) {
			case "port": {
				try {
					edurasServer
							.setEdurasPort(Integer.parseInt(parameterValue));
				} catch (NumberFormatException e) {
					L.severe(Localization.getStringF("Server.invalidportarg",
							parameterValue));
					return;
				}
				break;
			}

			case "serverhostaddress": {
				edurasServer.setServerHostAddress(parameterValue);
				break;
			}

			case "name": {
				edurasServer.setName(parameterValue);
				break;
			}

			case "registeratmetaserver": {
				edurasServer.setRegisterAtMetaserver(Boolean
						.parseBoolean(parameterValue));
				break;
			}

			case "loglimit": {
				logLimit = Level.parse(parameterValue);
				break;
			}

			case "localconsole": {
				edurasServer.setLocalConsoleOn(Boolean
						.parseBoolean(parameterValue));
				break;
			}

			case "remoteconsoleport": {
				edurasServer.setRemoteConsolePort(Integer
						.parseInt(parameterValue));
				break;
			}

			case "remoteconsole": {
				edurasServer.setRemoteConsoleOn(Boolean
						.parseBoolean(parameterValue));
				break;
			}

			case "remoteconsolepassword": {
				edurasServer.setRemoteConsolePassword(parameterValue);
				break;
			}

			case "startmap": {
				try {
					edurasServer.setStartMap(parameterValue);
				} catch (NoSuchMapException e) {
					L.log(Level.SEVERE, "There is no such map: "
							+ parameterValue, e);
					System.exit(-1);
				} catch (InvalidDataException e) {
					L.log(Level.SEVERE, "Could not load map " + parameterValue
							+ ".", e);
					System.exit(-1);
				}
				break;
			}

			case "startgamemode": {
				edurasServer.setStartGameMode(parameterValue);
				break;
			}

			case "startconfig": {
				File settingsFile = new File(parameterValue);
				if (!settingsFile.exists()) {
					System.out.println("Config file at " + parameterValue
							+ " does not exist.");
					System.exit(-1);
				}
				edurasServer.setStartSettings(settingsFile);
				break;
			}

			default:
				final String sClassName = S.class.getSimpleName();

				if (parameterName.startsWith(sClassName + ".")) {
					try {
						Field f = S.Server.class.getField(parameterName
								.substring(sClassName.length() + 1));
						Class<?> targetClass = f.getType();
						Object value = ReflectionTools.toPrimitive(targetClass,
								parameterValue);
						f.set(null, value);
						L.log(Level.INFO, "Set S." + f.getName() + " to "
								+ value);
					} catch (NoSuchFieldException | SecurityException
							| IllegalArgumentException | IllegalAccessException e) {
						L.log(Level.WARNING,
								"Failed to set S field from command line, argument: "
										+ parameterName + "=" + parameterValue,
								e);
					}
				} else {
					System.out.println("Unknown argument " + parameterName);
					System.exit(-1);
				}
			}
		}

		EduLog.setBasicLogLimit(logLimit);
		EduLog.setConsoleLogLimit(logLimit);

		if (S.Server.exit_on_sysout) {
			SysOutCatcher.startCatching();
		}

		try {
			edurasServer.runServer();
		} catch (NoSuchGameModeException e) {
			L.log(Level.WARNING, "The specified game mode doesn't exist.", e);
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

class MetaServerRegisterer extends Thread {

	private final static Logger L = EduLog
			.getLoggerFor(MetaServerRegisterer.class.getName());

	private final String name;
	private final String ip;
	private final int port;
	private final String version;
	private final ClientInterface metaServerClient;
	private final GameInformation gameInfo;

	public MetaServerRegisterer(String name, String ip, int port,
			GameInformation gameInfo) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.version = EdurasVersion.getVersion();
		this.gameInfo = gameInfo;

		metaServerClient = new Client();
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

					@Override
					public void onConnectionEstablished(int clientId) {
						synchronized (MetaServerRegisterer.this) {
							MetaServerRegisterer.this.notify();
						}
					}
				});
		metaServerClient.connect(ServerSearcher.METASERVER_ADDRESS,
				ServerDiscoveryListener.META_SERVER_PORT);

		if (!metaServerClient.isConnected()) {
			L.warning("Could not connect to MetaServer. This server won't be discoverable via Internet.");
		}
	}

	@Override
	public void run() {
		// wait till client is connected.
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				L.log(Level.WARNING,
						"Interrupted while waiting for notification.", e);
			}
		}

		while (!interrupted()) {
			registerAtMetaServer(name, ip, port, version);
			try {
				sleep(15000);
			} catch (InterruptedException e) {
				L.log(Level.WARNING, "Cannot sleep in MetaServerRegisterer.", e);
			}
		}
	}

	private void registerAtMetaServer(String nameOfServer, String ipOfServer,
			int portOfServer, String versionOfServer) {
		L.fine("Registering at meta server.");

		Event registerEvent = new Event(MetaServer.REGISTER_REQUEST);
		registerEvent.putArgument(metaServerClient.getClientId());
		registerEvent.putArgument(nameOfServer);
		registerEvent.putArgument(ipOfServer);
		registerEvent.putArgument(portOfServer);
		registerEvent.putArgument(versionOfServer);
		registerEvent.putArgument(gameInfo.getPlayers().size());
		registerEvent.putArgument(gameInfo.getGameSettings().getGameMode()
				.getName());
		registerEvent.putArgument(gameInfo.getMap().getName());
		try {
			metaServerClient.sendEvent(registerEvent);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.log(Level.WARNING, "Cannot register at metaserver.", e);
			return;
		}
	}
}
