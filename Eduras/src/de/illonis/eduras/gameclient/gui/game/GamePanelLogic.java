package de.illonis.eduras.gameclient.gui.game;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.CameraMouseListener;
import de.illonis.eduras.gameclient.gui.ClientGuiStepLogic;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gameclient.gui.InputKeyHandler;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;
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
		UserInputListener {

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
	private final GuiMouseHandler mouseHandler;
	private UserInterface userInterface;
	private final InformationProvider infoPro;
	private ClickState currentClickState;
	private final ClientData data;

	/**
	 * The current click state of mouse. this is depending on interaction mode.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum ClickState {
		DEFAULT, ITEM_SELECTED, UNITSELECT_DRAGGING;
	}

	/**
	 * Creates a new gamePanel with black background.
	 * 
	 * @param listener
	 *            the listener.
	 * @param clientData
	 *            client data object.
	 */
	public GamePanelLogic(GuiInternalEventListener listener,
			ClientData clientData) {
		this.data = clientData;
		gui = new GamePanel();
		currentClickState = ClickState.DEFAULT;
		this.reactor = listener;
		infoPro = EdurasInitializer.getInstance().getInformationProvider();
		resizeMonitor = new ResizeMonitor();
		keyHandler = new InputKeyHandler(this, reactor);
		keyHandler.addUserInputListener(this);
		camera = new GameCamera();
		mouseHandler = new GuiMouseHandler(this, reactor);
		cml = new CameraMouseListener(camera);
	}

	/**
	 * @return the current click state.
	 */
	public ClickState getClickState() {
		return currentClickState;
	}

	/**
	 * @param state
	 *            the new click state.
	 */
	public void setClickState(ClickState state) {
		currentClickState = state;
	}

	private void initUserInterface() {
		userInterface = new UserInterface(infoPro, mouseHandler, mouseHandler,
				hudNotifier);
		renderer = new GameRenderer(camera, userInterface, infoPro, data);
		renderer.setTarget(gui);
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
		gui.addMouseListener(mouseHandler);
		gui.addMouseMotionListener(mouseHandler);
		gui.addMouseMotionListener(cml);
		gui.addMouseListener(cml);
		startRendering();
		notifyGuiSizeChanged();
		gui.addKeyListener(keyHandler);
		gui.requestFocus();
		gui.requestFocusInWindow();
		hudNotifier.onGameReady();
	}

	@Override
	public void onHidden() {
		EdurasInitializer.getInstance().stopLogicWorker();
		gui.removeMouseMotionListener(cml);
		gui.removeMouseListener(cml);
		gui.removeMouseListener(mouseHandler);
		gui.removeMouseMotionListener(mouseHandler);
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

	/**
	 * Returns tooltip handler that handles tooltips.
	 * 
	 * @return tooltip handler.
	 */
	public TooltipHandler getTooltipHandler() {
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
	 * @return mouse position computed to ingame point.
	 */
	public Vector2D getCurrentMousePos() {
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point mp = pi.getLocation();
		Point pos = getLocationOnScreen();
		Vector2D p = new Vector2D(mp.x - pos.x, mp.y - pos.y);
		return computeGuiPointToGameCoordinate(p);
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

	/**
	 * Sets the hud notifier that notifies hud.
	 * 
	 * @param hudNotifier
	 *            the hud notifier.
	 */
	public void setHudNotifier(HudNotifier hudNotifier) {
		this.hudNotifier = hudNotifier;
	}
}