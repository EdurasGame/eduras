package de.illonis.eduras.gameclient;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.UDPHiEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameclient.gui.ClientFrame;
import de.illonis.eduras.gameclient.gui.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.InputKeyHandler;
import de.illonis.eduras.gameclient.gui.guielements.ClickableGuiElementInterface;
import de.illonis.eduras.gameclient.gui.guielements.TooltipTriggerer;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.networking.ServerClient.ClientRole;
import de.illonis.eduras.networking.discover.ServerFoundListener;
import de.illonis.eduras.networking.discover.ServerSearcher;
import de.illonis.eduras.settings.Settings;

/**
 * Represents a full game client that handles both, network management and gui.
 * 
 * @author illonis
 * 
 */
public class GameClient implements GuiClickReactor, NetworkEventReactor,
		TooltipTriggererNotifier {

	private final static Logger L = EduLog.getLoggerFor(GameClient.class
			.getName());

	private InformationProvider infoPro;
	private EventSender eventSender;
	private NetworkManager nwm;
	private EdurasInitializer initializer;
	private ClientEventHandler eventHandler;
	private Settings settings;
	private InputKeyHandler keyHandler;
	private ClientFrame frame;
	private ClickState currentClickState;
	private int currentItemSelected = -1;
	private final LinkedList<ClickableGuiElementInterface> clickListeners;
	private final LinkedList<TooltipTriggerer> triggerers;
	private ServerSearcher searcher;
	// private TooltipHandler tooltipHandler;

	private String clientName;
	private ClientRole role;

	private ConnectionState connectionState;

	private enum ClickState {
		DEFAULT, ITEM_SELECTED;
	}

	private enum ConnectionState {
		NOT_CONNECTED, CONNECTING, CONNECTION_ESTABLISHED, UDP_READY, GAME_READY;
	}

	/**
	 * Creates a new client and initializes all necessary components.
	 */
	public GameClient() {
		// new LoggerGui().setVisible(true);
		clickListeners = new LinkedList<ClickableGuiElementInterface>();
		triggerers = new LinkedList<TooltipTriggerer>();
		currentClickState = ClickState.DEFAULT;
		connectionState = ConnectionState.NOT_CONNECTED;
		loadTools();
		startup();
	}

	/**
	 * Tells gameclient which frame it should use to display game.
	 * 
	 * @param clientFrame
	 *            target client frame.
	 */
	void useFrame(ClientFrame clientFrame) {
		this.frame = clientFrame;
	}

	/**
	 * Initializes and shows up gui.
	 */
	void startGui() {
		frame.initUserInterface();
		frame.setVisible(true);
	}

	/**
	 * Adds mouse listeners to given component, usually the game panel.
	 * 
	 * @param c
	 *            listeners will be added to this component.
	 */
	public void addMouseListenersTo(Component c) {
		ClickListener cl = new ClickListener();
		c.addMouseListener(cl);
		c.addMouseMotionListener(cl);
	}

	/**
	 * Calculates current mouse position in gamePanel
	 * 
	 * @return mouse position.
	 */
	private Point getCurrentMousePos() {
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point mp = pi.getLocation();
		Point pos = frame.getLocationOnScreen();
		Point p = new Point(mp.x - pos.x, mp.y - pos.y);
		return p;
	}

	private void loadTools() {
		initializer = EdurasInitializer.getInstance();
		eventSender = initializer.getEventSender();
		infoPro = initializer.getInformationProvider();
		eventHandler = new ClientEventHandler(this);

		nwm = initializer.getNetworkManager();
		nwm.setNetworkEventListener(eventHandler);
	}

	/**
	 * Loads settings.
	 */
	private void startup() {
		settings = initializer.getSettings();
	}

	@Override
	public void onConnected(int clientId) {

		this.connectionState = ConnectionState.CONNECTION_ESTABLISHED;

		if (clientId != getOwnerID()) // only handle my connection
			return;
		L.info("Connection to server established. OwnerId: "
				+ infoPro.getOwnerID());
		keyHandler = new InputKeyHandler(this, eventSender, settings);
		keyHandler.addUserInputListener(frame);
		frame.onConnected(clientId); // pass to gui

		final int finalClientId = clientId;

		class UDPHiSender extends Thread {

			public UDPHiSender() {
				super("UDPHiSender");
			}

			@Override
			public void run() {
				while (connectionState != ConnectionState.GAME_READY) {
					try {
						sendEvent(new UDPHiEvent(finalClientId));
						sleep(50);
					} catch (WrongEventTypeException
							| MessageNotSupportedException
							| InterruptedException e) {
						L.log(Level.SEVERE, "Error sending UDP hi", e);
						break;
					}
				}
			}
		}

		new UDPHiSender().start();
	}

	@Override
	public void onConnectionLost(int client) {
		if (client == getOwnerID()) {
			L.warning("Connection lost");
			frame.onConnectionLost(client);
			connectionState = ConnectionState.NOT_CONNECTED;
		} else {
			// TODO: other client left
		}
	}

	@Override
	public void onDisconnect(int clientId) {
		L.info("Disconnected: " + clientId);
		frame.onDisconnect(clientId);
		connectionState = ConnectionState.NOT_CONNECTED;
	}

	/**
	 * Triggers item used events.
	 * 
	 * @param i
	 *            item slot.
	 */
	public void itemUsed(int i) {
		itemUsed(i, new Vector2D(getCurrentMousePos()));
	}

	/**
	 * Sends an item use event to server.
	 * 
	 * @param i
	 *            item slot.
	 * @param target
	 *            target position
	 */
	void itemUsed(int i, Vector2D target) {
		ItemEvent event = new ItemEvent(GameEventNumber.ITEM_USE,
				infoPro.getOwnerID(), i);
		event.setTarget(frame.computeGuiPointToGameCoordinate(target));

		try {
			sendEvent(event);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.WARNING, "error sending item used event", e);
		}
	}

	/**
	 * Listens for click on gui and passes them to gui elements. Additionally
	 * looks for mouse position to handle tooltips.
	 * 
	 * @author illonis
	 * 
	 */
	private class ClickListener extends MouseAdapter {

		@Override
		public void mouseMoved(MouseEvent e) {
			frame.getTooltipHandler().hideTooltip();
			Point p = e.getPoint();
			// TODO : notify only elements that are in area.

			for (TooltipTriggerer t : triggerers) {
				if (t.getTriggerArea().contains(p)) {
					t.onMouseOver(p);

					break;
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			L.info("Click at " + e.getX() + ", " + e.getY());
			Point p = e.getPoint();
			switch (currentClickState) {
			case ITEM_SELECTED:
				if (currentItemSelected != -1)
					itemUsed(currentItemSelected, new Vector2D(p));
			case DEFAULT:
				// TODO: Notify only elements that are really clicked.
				for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
						.iterator(); iterator.hasNext();) {
					ClickableGuiElementInterface reactor = iterator.next();
					if (reactor.onClick(p))
						return;
				}
				inGameClick(p);
				break;
			default:
				break;

			}
		}
	}

	/**
	 * Indicates a click into game world.<br>
	 * Note that position information is gui-relative and has to be computed to
	 * game coordinates.
	 * 
	 * @param p
	 *            click position in gui.
	 */
	private void inGameClick(Point p) {
		// TODO: implement
	}

	/**
	 * Indicates that user tried to exit, e.g. when he closes the frame.
	 */
	public void tryExit() {

		int result = JOptionPane.showConfirmDialog(frame,
				Localization.getString("Client.exitconfirm"), "Exiting",
				JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			nwm.notifyDisconnect();
			stopDiscovery();
			System.exit(0);
		}
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
	 * Returns information provider.
	 * 
	 * @return information provider.
	 */
	public InformationProvider getInformationProvider() {
		return infoPro;
	}

	@Override
	public void itemClicked(int i) {
		if (i >= 0 && i < Inventory.MAX_CAPACITY) {
			currentItemSelected = i;
			currentClickState = ClickState.ITEM_SELECTED;
		} else {
			currentClickState = ClickState.DEFAULT;
			currentItemSelected = -1;
		}
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

	@Override
	public void addClickableGuiElement(ClickableGuiElementInterface elem) {
		clickListeners.add(elem);
	}

	@Override
	public void removeClickableGuiElement(ClickableGuiElementInterface elem) {
		clickListeners.remove(elem);
	}

	/**
	 * Adds key handler to given component.
	 * 
	 * @param c
	 *            component to that key handler should be assigned.
	 */
	public void addKeyHandlerTo(Component c) {
		c.addKeyListener(keyHandler);
	}

	@Override
	public void onPlayerReceived() {
	}

	/**
	 * Returns owner id of client.
	 * 
	 * @return owner id.
	 */
	public int getOwnerID() {
		return infoPro.getOwnerID();
	}

	@Override
	public void registerTooltipTriggerer(TooltipTriggerer elem) {
		triggerers.add(elem);
	}

	@Override
	public void removeTooltipTriggerer(TooltipTriggerer elem) {
		triggerers.remove(elem);
	}

	@Override
	public void onGameReady() {
		connectionState = ConnectionState.GAME_READY;
		frame.onGameReady();
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
	 * Indicates that client frame's focus is lost.
	 */
	public void onFocusLost() {
		if (keyHandler != null)
			keyHandler.releaseAllKeys();
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

	@Override
	public void onUDPReady(int clientId) {
		connectionState = ConnectionState.UDP_READY;
		try {
			sendEvent(new InitInformationEvent(role, clientName, clientId));
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending initinformation event", e);
		}
	}
}
