package de.illonis.eduras.gameclient;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import de.eduras.eventingserver.Event;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.chat.ChatClientImpl;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameclient.gui.ClientFrame;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.networking.discover.ServerFoundListener;
import de.illonis.eduras.networking.discover.ServerSearcher;

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
	private ClientFrame frame;
	private ServerSearcher searcher;
	private HudNotifier hudNotifier;
	private final ClientData data;
	private boolean wantsExit = false;

	// private TooltipHandler tooltipHandler;

	private String clientName;
	private ClientRole role;

	/**
	 * Creates a new client and initializes all necessary components.
	 */
	public GameClient() {
		data = new ClientData();
		loadTools();
	}

	/**
	 * Tells gameclient which frame it should use to display game.
	 * 
	 * @param clientFrame
	 *            target client frame.
	 */
	void useFrame(ClientFrame clientFrame) {
		this.frame = clientFrame;
		clientFrame.setHudNotifier(hudNotifier);
	}

	/**
	 * Initializes and shows up gui.
	 */
	void startGui() {
		frame.setVisible(true);
	}

	private void loadTools() {
		initializer = EdurasInitializer.getInstance();
		eventSender = initializer.getEventSender();
		infoPro = initializer.getInformationProvider();
		eventHandler = new ClientNetworkEventListener(this);
		hudNotifier = new HudNotifier();
		ClientGameEventListener cge = new ClientGameEventListener(this,
				hudNotifier);
		infoPro.setGameEventListener(cge);
		nwm = initializer.getNetworkManager();
		nwm.setNetworkEventHandler(eventHandler);
	}

	private void initChat() {
		ChatClientImpl chat = new ChatClientImpl();
		ChatCache cache = new ChatCache();
		chat.setChatActivityListener(new ClientChatReceiver(frame
				.getGamePanel(), chat, clientName, cache));
		frame.getGamePanel().setChat(chat, cache);
		chat.connect(nwm.getServerAddress().getHostAddress(), nwm.getPort() + 1);
	}

	/**
	 * Handles connection of this client.
	 * 
	 * @param clientId
	 *            the connected client.
	 */
	public void onClientConnected(int clientId) {
		if (clientId != getOwnerID()) // only handle my connection
			return;
		wantsExit = false;
		initChat();
		L.info("Connection to server established. OwnerId: "
				+ infoPro.getOwnerID());
		nwm.ping();
		frame.onClientConnected(clientId); // pass to gui

		// FIXME: why this???
		try {
			sendEvent(new InitInformationEvent(role, clientName, clientId));
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending initinformation event", e);
		}
	}

	/**
	 * Handles disconnect of a client.
	 * 
	 * @param clientId
	 *            the client that lost connection.
	 */
	public void onClientConnectionLost(int clientId) {
		if (clientId == getOwnerID()) {
			if (wantsExit) {
				frame.onClientDisconnect(clientId, wantsExit);
			} else {
				L.warning("Connection lost");
				frame.onClientConnectionLost(clientId);
			}
		} else {
			// TODO: other client left
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
			frame.onClientConnectionLost(clientId);
			JOptionPane.showMessageDialog(frame,
					"You have been kicked from the server:\n" + reason,
					"Kicked from server", JOptionPane.ERROR_MESSAGE);
		} else {
			frame.notification("Client " + clientId
					+ " has been kicked. Reason: " + reason);
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
		frame.onClientDisconnect(clientId, wantsExit);
	}

	/**
	 * Indicates that user tries to exit, e.g. when he closes the frame.
	 */
	public void tryExit() {

		int result = JOptionPane.showConfirmDialog(frame,
				Localization.getString("Client.exitconfirm"), "Exiting",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			wantsExit = true;
			if (nwm.isConnected())
				nwm.disconnect();
			else
				frame.onExit();
		}
	}

	/**
	 * Handles a full server and returns to login screen.
	 */
	public void onServerIsFull() {
		frame.hideProgress();
		frame.showLogin();
		JOptionPane.showMessageDialog(frame,
				Localization.getString("Client.serverfull"), "Server full",
				JOptionPane.ERROR_MESSAGE);
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

	ClientFrame getFrame() {
		return frame;
	}

	/**
	 * Starts searching for servers in local network.
	 * 
	 * @param listener
	 *            the listener that retrieves found servers.
	 */
	public void startDiscovery(ServerFoundListener listener) {
		searcher = new ServerSearcher(listener);
		searcher.start();
	}

	/**
	 * Stops searching for servers.
	 */
	public void stopDiscovery() {
		if (searcher == null)
			return;
		searcher.interrupt();
	}

	/**
	 * @return the client data.
	 */
	public ClientData getData() {
		return data;
	}

	void setPing(long latency) {
		frame.getGamePanel().setPing(latency);
	}

}
