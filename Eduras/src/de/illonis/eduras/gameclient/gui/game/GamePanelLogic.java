package de.illonis.eduras.gameclient.gui.game;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.chat.ChatClientImpl;
import de.illonis.eduras.chat.NotConnectedException;
import de.illonis.eduras.chat.UserNotInRoomException;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.gameclient.ChatCache;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.CameraMouseListener;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gameclient.gui.TimedTasksHolderGUI;
import de.illonis.eduras.gameclient.gui.hud.DragSelectionRectangle;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * A panel that represents the gameworld. All world objects and user interface
 * are displayed here.
 * 
 * @author illonis
 * 
 */
public class GamePanelLogic implements UserInputListener {

	private final static Logger L = EduLog.getLoggerFor(GamePanelLogic.class
			.getName());

	private GameRenderer renderer;
	private final GameCamera camera;
	private final CameraMouseListener cml;
	private GameContainer gui;
	private final GuiInternalEventListener reactor;
	private final InputKeyHandler keyHandler;
	private final ResizeMonitor resizeMonitor;
	private HudNotifier hudNotifier;
	private final GuiMouseHandler mouseHandler;
	private UserInterface userInterface;
	private final InformationProvider infoPro;
	private ClickState currentClickState;
	private final ClientData data;
	private ChatClientImpl chat;
	private ChatCache cache;

	/**
	 * The current click state of mouse. this is depending on interaction mode.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum ClickState {
		DEFAULT, ITEM_SELECTED, UNITSELECT_DRAGGING, SELECT_BASE_FOR_REZZ, SELECT_TARGET_FOR_HEAL, SELECT_POSITION_FOR_OBSERVERUNIT, SELECT_POSITION_FOR_SCOUT, SELECT_POSITION_FOR_ITEMSPAWN;
	}

	/**
	 * Creates a new gamePanel with black background.
	 * 
	 * @param listener
	 *            the listener.
	 * @param container
	 *            the game container.
	 */
	public GamePanelLogic(GuiInternalEventListener listener,
			GameContainer container) {
		this.data = EdurasInitializer.getInstance().getInformationProvider()
				.getClientData();
		this.gui = container;
		currentClickState = ClickState.DEFAULT;
		this.reactor = listener;
		infoPro = EdurasInitializer.getInstance().getInformationProvider();

		resizeMonitor = new ResizeMonitor();
		keyHandler = new InputKeyHandler(this, reactor);
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
				hudNotifier, reactor, cache);
		renderer = new GameRenderer(camera, userInterface, infoPro, data);
		userInterface.setRenderer(renderer);
		// renderer.setTarget(gui);
	}

	/**
	 * Returns current rendering scale.
	 * 
	 * @return current scale factor.
	 */
	float getCurrentScale() {
		return renderer.getCurrentScale();
	}

	public GameRenderer getRenderer() {
		return renderer;
	}

	public void start() {
		camera.reset();
		initUserInterface();
		doTimedTasks();
		notifyGuiSizeChanged();
	}

	private void doTimedTasks() {
		TimedTasksHolderGUI.getInstance().execute();
	}

	private void stopTimedTasks() {
		TimedTasksHolderGUI.getInstance().cancel();
	}

	public void stop() {
		gui.exit();
		cml.stop();
		stopTimedTasks();
	}

	/**
	 * @return the current gamecamera.
	 */
	public GameCamera getCamera() {
		return camera;
	}

	/**
	 * Notifies all ui objects that gui size has changed.
	 * 
	 */
	private void notifyGuiSizeChanged() {
		L.fine("[GUI] Size changed. New size: " + gui.getWidth() + ", "
				+ gui.getHeight());
		System.out.println("[GUI] Size changed. New size: " + gui.getWidth()
				+ ", " + gui.getHeight());
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

	public GameContainer getGui() {
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
	public Vector2f computeGuiPointToGameCoordinate(Vector2f v) {
		float scale = getCurrentScale();
		Vector2f vec = new Vector2f(v);
		vec.x += renderer.getViewport().getX();
		vec.y += renderer.getViewport().getY();
		// vec.x /= gui.getContainer().getWidth();
		// vec.y /= gui.getContainer().getHeight();
		vec.scale(1 / scale);
		return vec;
	}

	@Override
	public Vector2f getCurrentMousePos() {
		return computeGuiPointToGameCoordinate(new Vector2f(gui.getInput()
				.getMouseX(), gui.getInput().getMouseY()));
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

	DragSelectionRectangle getDragRect() {
		return userInterface.getDragRect();
	}

	/**
	 * Shows a notification to the user in gamepanel.
	 * 
	 * @param msg
	 *            the message.
	 */
	public void showNotification(String msg) {
		userInterface.showNotification(msg);
	}

	/**
	 * Assigns the chat to the logic.
	 * 
	 * @param chat
	 *            the chat implementation.
	 */
	public void setChat(ChatClientImpl chat) {
		this.chat = chat;
		this.cache = infoPro.getClientData().getChatCache();
	}

	/**
	 * Called when new information about current ping arrived.
	 * 
	 * @param latency
	 *            new ping
	 */
	public void setPing(long latency) {
		if (userInterface != null)
			userInterface.getPingListener().setPING(latency);
	}

	@Override
	public void onChatEnter() {
		if (cache.isWriting())
			sendChat();
		else
			cache.startWriting();
	}

	private void sendChat() {
		String message = cache.sendInput();
		if (!message.isEmpty())
			try {
				chat.postChatMessage(message, cache.getCurrentRoom());
			} catch (IllegalArgumentException | UserNotInRoomException
					| NotConnectedException e) {
				L.log(Level.SEVERE, "Error sending chat message", e);
			}
	}

	/**
	 * Returns the {@link ClientData}.
	 * 
	 * @return clientdata
	 */
	public ClientData getClientData() {
		return data;
	}

	@Override
	public void selectItem(int i) {
		data.setCurrentItemSelected(i);
		currentClickState = ClickState.ITEM_SELECTED;
	}

	/**
	 * Called when an RTS action fails. Displays an error message on the
	 * notification panel.
	 * 
	 * @param e
	 *            the related {@link ActionFailedException}.
	 */
	public void onActionFailed(ActionFailedException e) {
		showNotification(e.getMessage());
	}

	@Override
	public void resetCamera() {
		getCamera().reset();
	}

	@Override
	public boolean abortChat() {
		if (cache.isWriting()) {
			cache.stopWriting();
			return true;
		}
		return false;
	}

	public InputKeyHandler getKeyHandler() {
		return keyHandler;
	}

	public GuiMouseHandler getMouseHandler() {
		return mouseHandler;
	}

	@Override
	public void onGameQuit() {
		reactor.onGameQuit();
	}
}