package de.illonis.eduras.bot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.ClientNetworkEventHandler;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.ClientRole;

/**
 * This class is an Eduras? client that works automatically without any user
 * input. After the connection to the server is established, it starts a thread
 * running {@link EdurasBotWorker}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EdurasBotClient {
	private final static Logger L = EduLog.getLoggerFor(EdurasBotClient.class
			.getName());
	private static int PORT = 4386;

	private EventSender eventSender;
	private NetworkManager networkManager;
	private InformationProvider infoProvider;
	private EdurasBotWorker botWorker;
	private String name;
	private int clientId;

	/**
	 * Creates a new bot.
	 * 
	 * @param name
	 *            The bot's name to be displayed in game.
	 * @param botWorker
	 *            The thread to run when the connection has been established
	 */
	public EdurasBotClient(String name, EdurasBotWorker botWorker) {
		eventSender = EdurasInitializer.getInstance().getEventSender();
		networkManager = EdurasInitializer.getInstance().getNetworkManager();
		infoProvider = EdurasInitializer.getInstance().getInformationProvider();
		this.botWorker = botWorker;
		this.name = name;
		botWorker.setEventSender(eventSender);
		botWorker.setInformationProvider(infoProvider);

		initLogic();
	}

	private void initLogic() {
		// ///////////
		// NetworkEventHandler
		// ///////////
		networkManager.setNetworkEventHandler(new ClientNetworkEventHandler() {

			@Override
			public void onClientDisconnected(int id) {
				// we don't care
			}

			@Override
			public void onClientConnected(int id) {
				// we don't care
			}

			@Override
			public void onServerIsFull() {
				// we don't care
			}

			@Override
			public void onPingReceived(long latency) {
				// we don't care
			}

			@Override
			public void onDisconnected() {
				// we don't care
			}

			@Override
			public void onConnectionLost() {
				// we don't care
			}

			@Override
			public void onConnectionEstablished(int id) {
				clientId = id;
				try {
					eventSender.sendEvent(new InitInformationEvent(
							ClientRole.PLAYER, name, clientId));
				} catch (WrongEventTypeException | MessageNotSupportedException e) {
					L.log(Level.SEVERE, "Cannot send event!", e);
					return;
				}
			}

			@Override
			public void onClientKicked(int id, String reason) {
				// we don't care
			}
		});

		// //////////////////
		// GameEventListener
		// //////////////////
		infoProvider.setGameEventListener(new GameEventListener() {

			@Override
			public void onVisibilityChanged(SetVisibilityEvent event) {
			}

			@Override
			public void onTeamResourceChanged(
					SetTeamResourceEvent setTeamResourceEvent) {
			}

			@Override
			public void onRespawn(RespawnEvent event) {
			}

			@Override
			public void onPlayerLeft(int ownerId) {
			}

			@Override
			public void onPlayerJoined(int ownerId) {
			}

			@Override
			public void onOwnerChanged(SetOwnerEvent event) {
			}

			@Override
			public void onObjectStateChanged(
					SetGameObjectAttributeEvent<?> event) {
			}

			@Override
			public void onObjectRemove(ObjectFactoryEvent event) {
			}

			@Override
			public void onObjectCreation(ObjectFactoryEvent event) {
			}

			@Override
			public void onNewObjectPosition(GameObject object) {
			}

			@Override
			public void onMaxHealthChanged(
					SetIntegerGameObjectAttributeEvent event) {
			}

			@Override
			public void onMatchEnd(MatchEndEvent event) {
			}

			@Override
			public void onItemSlotChanged(SetItemSlotEvent event) {
			}

			@Override
			public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
			}

			@Override
			public void onInformationRequested(ArrayList<GameEvent> infos,
					int targetOwner) {
			}

			@Override
			public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
			}

			@Override
			public void onGameReady() {
				EdurasInitializer.getInstance().startLogicWorker(true);
				new Thread(botWorker).start();
			}

			@Override
			public void onGameModeChanged(GameMode newGameMode) {
			}

			@Override
			public void onDeath(DeathEvent event) {
			}

			@Override
			public void onCooldownStarted(ItemEvent event) {
			}

			@Override
			public void onCooldownFinished(ItemEvent event) {
			}

			@Override
			public void onClientRename(ClientRenameEvent event) {
			}
		});
	}

	/**
	 * Starts a {@link RandomBotWorker}-bot and connects it to a server.
	 * 
	 * @param args
	 *            Arguments passed from console. Put them in the form
	 *            "parametername1=parametervalue1 parametername2=parametervalue2 ..."
	 *            where parameternameX is one of the following
	 *            <ul>
	 *            <li><b>port:</b> Port of the Eduras? bot client.</li>
	 *            <li><b>loglimit:</b> Determines the finest log level to
	 *            capture.</li>
	 *            <li><b>serveraddress:</b> The address of the server to connect
	 *            to.</li>
	 *            <li><b>serverport:</b> Port of the server to connect to.</li>
	 *            </ul>
	 */
	public static void main(String[] args) {
		SimpleDateFormat simpleDate = new SimpleDateFormat("y-M-d-H-m-s");

		try {
			EduLog.init(simpleDate.format(new Date()) + "-botclient.log",
					2097152);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Level logLimit = Level.INFO;
		String serverAddress = "";
		int remotePort = 0;
		String botName = "ferde";

		// arguments are of form <parametername>=<parametervalue>
		String[][] parametersWithValues = new String[args.length][2];

		for (int i = 0; i < args.length; i++) {
			parametersWithValues[i] = args[i].split("=");
		}

		for (int i = 0; i < args.length; i++) {

			String parameterName = parametersWithValues[i][0];
			String parameterValue = parametersWithValues[i][1];

			if (parameterName.equalsIgnoreCase("loglimit")) {
				logLimit = Level.parse(parameterValue);
			}

			if (parameterName.equalsIgnoreCase("port")) {
				try {
					PORT = Integer.parseInt(parameterValue);
					if (PORT < 1024 || PORT > 49151) {
						throw new Exception();
					}
				} catch (Exception e) {
					L.severe("Given port is not a valid value!");
					return;
				}
			}

			if (parameterName.equalsIgnoreCase("serveraddress")) {
				serverAddress = parameterValue;
			}

			if (parameterName.equalsIgnoreCase("serverport")) {
				remotePort = Integer.parseInt(parameterValue);
			}

			if (parameterName.equalsIgnoreCase("botname")) {
				botName = parameterValue;
			}
		}

		EduLog.setBasicLogLimit(logLimit);
		EduLog.setConsoleLogLimit(logLimit);
		EduLog.setFileLogLimit(logLimit);

		if (serverAddress.isEmpty() || remotePort == 0) {
			L.severe("Did not specify serveraddress or port!");
			return;
		}

		EdurasBotClient botClient = new EdurasBotClient(botName,
				new RandomBotWorker());
		try {
			botClient.connect(serverAddress, remotePort);
		} catch (IOException e) {
			L.log(Level.SEVERE, "Cannot connect to server!", e);
		}
	}

	/**
	 * Connects the bot to the specified server.
	 * 
	 * @param serverAddress
	 * @param port
	 * @throws UnknownHostException
	 *             Thrown if the server address cannot be found.
	 * @throws IOException
	 *             Thrown if something goes wrong during connection.
	 */
	public void connect(String serverAddress, int port)
			throws UnknownHostException, IOException {
		networkManager.connect(InetAddress.getByName(serverAddress), port);
	}
}
