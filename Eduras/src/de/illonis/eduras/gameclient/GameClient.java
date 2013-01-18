package de.illonis.eduras.gameclient;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameclient.gui.CameraMouseListener;
import de.illonis.eduras.gameclient.gui.ClientFrame;
import de.illonis.eduras.gameclient.gui.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.InputKeyHandler;
import de.illonis.eduras.gui.guielements.ClickableGuiElementInterface;
import de.illonis.eduras.gui.guielements.TooltipTriggerer;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.networking.ServerClient.ClientRole;
import de.illonis.eduras.settings.Settings;

/**
 * Represents a full game client that handles both, network management and gui.
 * 
 * @author illonis
 * 
 */
public class GameClient implements GuiClickReactor, NetworkEventReactor,
		TooltipHandler {

	private InformationProvider infoPro;
	private EventSender eventSender;
	private NetworkManager nwm;
	private EdurasInitializer initializer;
	private ClientEventHandler eventHandler;
	private Settings settings;
	private InputKeyHandler keyHandler;
	private final CameraMouseListener cml;
	private final GameCamera camera;
	private ClientFrame frame;
	private ClickState currentClickState;
	private int currentItemSelected = -1;
	private LinkedList<ClickableGuiElementInterface> clickListeners;
	private LinkedList<TooltipTriggerer> triggerers;

	private String clientName;

	private enum ClickState {
		DEFAULT, ITEM_SELECTED;
	}

	/**
	 * Creates a new client and initializes all necessary components.
	 */
	public GameClient() {
		clickListeners = new LinkedList<ClickableGuiElementInterface>();
		triggerers = new LinkedList<TooltipTriggerer>();
		currentClickState = ClickState.DEFAULT;
		camera = new GameCamera();
		cml = new CameraMouseListener(camera);

		loadTools();
		startup();
	}

	/**
	 * Initializes and shows up gui.
	 */
	void startGui() {
		frame = new ClientFrame(this);
		frame.setVisible(true);
		infoPro.addEventListener(new GameEventReactor(this));
	}

	public ClientFrame getFrame() {
		return frame;
	}

	/**
	 * Adds mouse listeners to given component, usually the game panel.
	 * 
	 * @param c
	 *            listeners will be added to this component.
	 */
	public void addMouseListenersTo(Component c) {
		c.addMouseMotionListener(cml);
		c.addMouseListener(cml);
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
	public void onConnected() {
		EduLog.info("Connection to server established. OwnerId: "
				+ infoPro.getOwnerID());
		keyHandler = new InputKeyHandler(this, eventSender, settings);
		frame.setTitle(frame.getTitle() + " #" + infoPro.getOwnerID() + " ("
				+ clientName + ")");

		// test routine for item display in gui:
		/*
		 * try { Thread.sleep(1000); infoPro.getPlayer().getInventory()
		 * .loot(new ExampleWeapon(new GameInformation())); } catch
		 * (InventoryIsFullException e1) { e1.printStackTrace(); } catch
		 * (ObjectNotFoundException e1) { e1.printStackTrace(); } catch
		 * (InterruptedException e) { e.printStackTrace(); }
		 */
		frame.onConnected();
		try {
			sendEvent(new InitInformationEvent(ClientRole.PLAYER, clientName));
		} catch (WrongEventTypeException e) {
			EduLog.passException(e);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		}

	}

	@Override
	public void onConnectionLost() {
		EduLog.info("Connection to server lost.");
		frame.onConnectionLost();
	}

	@Override
	public void onDisconnect() {
		frame.onDisconnect();
		cml.stop();
		initializer.shutdown();
		nwm.disconnect();
	}

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
		event.setTarget(computeGuiPointToGameCoordinate(target));
		try {
			sendEvent(event);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			EduLog.passException(e);
		}
	}

	/**
	 * Computes a point that is relative to gui into game coordinates.
	 * 
	 * @param v
	 *            point to convert.
	 * @return game-coordinate point.
	 */
	public Vector2D computeGuiPointToGameCoordinate(Vector2D v) {
		Vector2D vec = new Vector2D(v);
		vec.modifyX(camera.getX());
		vec.modifyY(camera.getY());
		return vec;
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
			for (TooltipTriggerer t : triggerers) {
				t.onMouseAt(e.getPoint());
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			EduLog.info("Click at " + e.getX() + ", " + e.getY());
			Point p = e.getPoint();
			switch (currentClickState) {
			case DEFAULT:
				for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
						.iterator(); iterator.hasNext();) {
					ClickableGuiElementInterface reactor = iterator.next();
					if (reactor.onClick(p))
						return;
				}
				inGameClick(p);
				break;
			case ITEM_SELECTED:
				itemUsed(currentItemSelected, new Vector2D(p));
				currentClickState = ClickState.DEFAULT;
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
	 * Returns game camera.
	 * 
	 * @return game camera.
	 */
	public GameCamera getCamera() {
		return camera;
	}

	/**
	 * Indicates that user tried to exit, e.g. when he closes the frame.
	 */
	public void tryExit() {

		int result = JOptionPane.showConfirmDialog(frame,
				Localization.getString("Client.exitconfirm"), "Exiting",
				JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			onDisconnect();
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
		frame.onPlayerReceived();
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
	public void showItemTooltip(Point p, Item data) {
		frame.getRenderer().showItemTooltip(p, data);
	}

	@Override
	public void showTooltip(Point p, String text) {
		frame.getRenderer().showTooltip(p, text);
	}

	@Override
	public void hideTooltip() {
		frame.getRenderer().hideTooltip();
	}
}