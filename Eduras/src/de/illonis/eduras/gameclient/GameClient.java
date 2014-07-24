package de.illonis.eduras.gameclient;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;

import de.eduras.eventingserver.Event;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.chat.ChatClientImpl;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameclient.gui.GameManager;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gameclient.gui.animation.ClientEffectHandler;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.ClientRole;

/**
 * Represents a full game client that handles both, network management and gui.
 * 
 * @author illonis
 * 
 */
public class GameClient {

	private final static Logger L = EduLog.getLoggerFor(GameClient.class
			.getName());

	private InformationProvider infoPro;
	private EventSender eventSender;
	private NetworkManager nwm;
	private EdurasInitializer initializer;
	private ClientNetworkEventListener eventHandler;
	private final GameManager container;
	private HudNotifier hudNotifier;
	private boolean wantsExit = false;
	private GamePanelLogic logic;

	// private TooltipHandler tooltipHandler;

	private String clientName;
	private ClientRole role;

	GameClient(GameManager clientFrame, GameContainer container) {
		this.container = clientFrame;
		loadTools(container);
	}

	private void loadTools(GameContainer gameContainer) {

		initializer = EdurasInitializer.getInstance();
		initializer.initGame();
		eventSender = initializer.getEventSender();
		infoPro = initializer.getInformationProvider();
		eventHandler = new ClientNetworkEventListener(this);
		hudNotifier = new HudNotifier();
		GuiInternalEventListener guiEventListener = new GuiInternalEventListener(
				this);
		logic = new GamePanelLogic(guiEventListener, gameContainer);
		logic.setHudNotifier(hudNotifier);
		ClientGameEventListener cge = new ClientGameEventListener(this,
				hudNotifier, new ClientEffectHandler());
		infoPro.setGameEventListener(cge);
		nwm = initializer.getNetworkManager();
		nwm.setNetworkEventHandler(eventHandler);
	}

	private void initChat() {
		ChatClientImpl chat = new ChatClientImpl();
		chat.setChatActivityListener(new ClientChatReceiver(chat, clientName));
		logic.setChat(chat);
		chat.connect(nwm.getServerAddress().getHostAddress(), nwm.getPort() + 1);
	}

	/**
	 * Handles disconnect of a client.
	 * 
	 * @param clientId
	 *            the client that lost connection.
	 */
	public void onClientConnectionLost(int clientId) {
		if (clientId == getOwnerID()) {
			container.onDisconnect(wantsExit, null);
		}
	}

	/**
	 * @param clientId
	 *            the client that has been kicked.
	 * @param reason
	 *            the reason.
	 */
	public void onClientKicked(int clientId, String reason) {
		if (clientId == getOwnerID()) {
			container.onDisconnect(true,
					"You have been kicked from the server:\n" + reason);
		} else {
			// container.notification("Client " + clientId
			// + " has been kicked. Reason: " + reason);
		}
	}

	/**
	 * Handles disconnect of a client.
	 * 
	 * @param clientId
	 *            the client that disconnected.
	 */
	public void onClientDisconnect(int clientId) {
		L.info("Client disconnected: " + clientId);
		if (clientId == getOwnerID()) {
			container.onDisconnect(wantsExit, null);
		}
	}

	/**
	 * Indicates that user tries to exit, e.g. when he closes the frame.
	 */
	public void tryExit() {
		wantsExit = true;
		if (nwm.isConnected())
			nwm.disconnect();
	}

	/**
	 * Handles a full server and returns to login screen.
	 */
	public void onServerIsFull() {
		container.onDisconnect(false,
				Localization.getString("Client.serverfull"));
	}

	/**
	 * Returns network manager.
	 * 
	 * @return network manager.
	 */
	public NetworkManager getNetworkManager() {
		return nwm;
	}

	/**
	 * Sends an event to server.
	 * 
	 * @param event
	 *            event that should be sent.
	 * @throws WrongEventTypeException
	 *             if event type does not fit event.
	 * @throws MessageNotSupportedException
	 *             if given event is not supported by logic.
	 */
	public void sendEvent(Event event) throws WrongEventTypeException,
			MessageNotSupportedException {
		eventSender.sendEvent(event);
	}

	/**
	 * Sets client name to given name.
	 * 
	 * @param clientName
	 *            new client name.
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * Returns owner id of client.
	 * 
	 * @return owner id.
	 */
	public int getOwnerID() {
		return infoPro.getOwnerID();
	}

	/**
	 * Returns the username of this client.
	 * 
	 * @return this client's name.
	 * 
	 * @author illonis
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Sets the role of the client.
	 * 
	 * @param role
	 *            new role.
	 * 
	 * @author illonis
	 */
	public void setRole(ClientRole role) {
		this.role = role;
		// TODO set role for gui.
	}

	/**
	 * @return the client data.
	 */
	public ClientData getData() {
		return infoPro.getClientData();
	}

	void setPing(long latency) {
		logic.setPing(latency);
	}

	/**
	 * Handles connection of this client.
	 */
	public void onConnectionEstablished() {
		wantsExit = false;
		initChat();
		logic.start();
		L.info("Connection to server established. OwnerId: "
				+ infoPro.getOwnerID());
		nwm.ping();

	}

	GamePanelLogic getLogic() {
		return logic;
	}

	void sendInitInformation() {
		int clientId = infoPro.getOwnerID();
		try {
			sendEvent(new InitInformationEvent(role, clientName, clientId));
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending initinformation event", e);
		}

	}

}
