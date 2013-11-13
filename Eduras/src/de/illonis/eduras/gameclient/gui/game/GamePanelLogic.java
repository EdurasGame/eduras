package de.illonis.eduras.gameclient.gui.game;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.CameraMouseListener;
import de.illonis.eduras.gameclient.gui.ClientGuiStepLogic;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gameclient.gui.InputKeyHandler;
import de.illonis.eduras.gameclient.gui.hud.ClickableGuiElementInterface;
import de.illonis.eduras.gameclient.gui.hud.TooltipTriggerer;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.Vector2D;

/**
 * A panel that represents the gameworld. All world objects and user interface
 * are displayed here.
 * 
 * @author illonis
 * 
 */
public class GamePanelLogic extends ClientGuiStepLogic implements
		GuiClickReactor, TooltipTriggererNotifier, UserInputListener {

	private final static Logger L = EduLog.getLoggerFor(GamePanelLogic.class
			.getName());

	private GameRenderer renderer;
	private final GameCamera camera;
	private final CameraMouseListener cml;
	private final GamePanel gui;
	private final GuiInternalEventListener reactor;
	private InputKeyHandler keyHandler;
	private final ResizeMonitor resizeMonitor;
	private HudNotifier hudNotifier;

	private final LinkedList<ClickableGuiElementInterface> clickListeners;
	private final LinkedList<TooltipTriggerer> triggerers;
	private UserInterface userInterface;
	private final InformationProvider infoPro;
	private final ClickListener cl;

	private ClickState currentClickState;
	private int currentItemSelected = -1;

	private enum ClickState {
		DEFAULT, ITEM_SELECTED;
	}

	/**
	 * Creates a new gamePanel with black background.
	 * 
	 * @param listener
	 *            the listener.
	 */
	public GamePanelLogic(GuiInternalEventListener listener) {
		gui = new GamePanel();
		this.reactor = listener;

		infoPro = EdurasInitializer.getInstance().getInformationProvider();
		clickListeners = new LinkedList<ClickableGuiElementInterface>();
		triggerers = new LinkedList<TooltipTriggerer>();
		resizeMonitor = new ResizeMonitor();
		keyHandler = new InputKeyHandler(this, reactor);
		keyHandler.addUserInputListener(this);

		currentClickState = ClickState.DEFAULT;
		camera = new GameCamera();
		cml = new CameraMouseListener(camera);
		cl = new ClickListener();
	}

	private void initUserInterface() {
		userInterface = new UserInterface(infoPro, this, this, hudNotifier);
		renderer = new GameRenderer(camera, userInterface, infoPro);
		renderer.setTarget(gui);
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
			getTooltipHandler().hideTooltip();
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
					ClickableGuiElementInterface nextReactor = iterator.next();
					if (nextReactor.onClick(p))
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
	 * Returns current rendering scale.
	 * 
	 * @return current scale factor.
	 */
	double getCurrentScale() {
		return renderer.getCurrentScale();
	}

	/**
	 * Stopps rendering process.
	 */
	private void stopRendering() {
		if (renderer != null)
			renderer.stopRendering();
	}

	/**
	 * Starts rendering process.
	 */
	private void startRendering() {
		renderer.startRendering();
	}

	@Override
	public void onShown() {
		camera.reset();
		camera.startMoving();
		EdurasInitializer.getInstance().startLogicWorker();
		initUserInterface();
		gui.addComponentListener(resizeMonitor);
		gui.addMouseMotionListener(cml);
		gui.addMouseListener(cml);
		startRendering();
		notifyGuiSizeChanged();
		gui.addKeyListener(keyHandler);
		gui.requestFocus();
		gui.requestFocusInWindow();
		gui.addMouseListener(cl);
		gui.addMouseMotionListener(cl);
	}

	@Override
	public void onHidden() {
		EdurasInitializer.getInstance().stopLogicWorker();
		gui.removeMouseListener(cl);
		gui.removeMouseMotionListener(cl);
		gui.removeMouseMotionListener(cml);
		gui.removeMouseListener(cml);
		gui.removeComponentListener(resizeMonitor);
		gui.removeKeyListener(keyHandler);
		camera.stopMoving();
		cml.stop();
		stopRendering();
	}

	/**
	 * Notifies all ui objects that gui size has changed.
	 * 
	 */
	private void notifyGuiSizeChanged() {
		L.fine("[GUI] Size changed. New size: " + gui.getWidth() + ", "
				+ gui.getHeight());
		userInterface.onGuiSizeChanged(gui.getWidth(), gui.getHeight());
		camera.setSize(gui.getWidth(), gui.getHeight()); // maybe not?
	}

	/**
	 * Resizes camera on frame size change.
	 * 
	 * @author illonis
	 * 
	 */
	private class ResizeMonitor extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			notifyGuiSizeChanged();
		}
	}

	@Override
	public Component getGui() {
		return gui;
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

	@Override
	public void registerTooltipTriggerer(TooltipTriggerer elem) {
		triggerers.add(elem);
	}

	@Override
	public void removeTooltipTriggerer(TooltipTriggerer elem) {
		triggerers.remove(elem);
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
	 * Returns tooltip handler that handles tooltips.
	 * 
	 * @return tooltip handler.
	 */
	TooltipHandler getTooltipHandler() {
		return userInterface.getTooltipHandler();
	}

	@Override
	public void showStatWindow() {
		userInterface.showStatWindow();
	}

	@Override
	public void hideStatWindow() {
		userInterface.hideStatWindow();
	}

	/**
	 * Computes a point that is relative to gui into game coordinates.
	 * 
	 * @param v
	 *            point to convert.
	 * @return game-coordinate point.
	 */
	public Vector2D computeGuiPointToGameCoordinate(Vector2D v) {
		double scale = getCurrentScale();
		Vector2D vec = new Vector2D(v);
		vec.modifyX(camera.getX() * scale);
		vec.modifyY(camera.getY() * scale);
		vec.mult(1 / scale);
		return vec;
	}

	/**
	 * Calculates current mouse position in gamePanel
	 * 
	 * @return mouse position.
	 */
	private Point getCurrentMousePos() {
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point mp = pi.getLocation();
		Point pos = getLocationOnScreen();
		Point p = new Point(mp.x - pos.x, mp.y - pos.y);
		return p;
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
		reactor.onItemUse(i, computeGuiPointToGameCoordinate(target));
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

	private Point getLocationOnScreen() {
		if (gui.isVisible())
			return gui.getLocationOnScreen();
		return new Point();
	}

	/**
	 * Indicates that gamepanel's focus is lost and releases all keys.
	 */
	public void onFocusLost() {
		if (keyHandler != null)
			keyHandler.releaseAllKeys();
	}

	public void setHudNotifier(HudNotifier hudNotifier) {
		this.hudNotifier = hudNotifier;
	}
}