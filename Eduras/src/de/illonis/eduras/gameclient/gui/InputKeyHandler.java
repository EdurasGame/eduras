package de.illonis.eduras.gameclient.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;
import de.illonis.eduras.gameclient.gui.game.UserInputListener;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.settings.KeyBindings.KeyBinding;
import de.illonis.eduras.settings.Settings;

/**
 * This class handles user's key input and passes them to logic.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler extends KeyAdapter {

	private final static Logger L = EduLog.getLoggerFor(InputKeyHandler.class
			.getName());

	private static final long KEY_INTERVAL = 20;
	private LinkedList<UserInputListener> listeners;
	private ListenerPromoter promoter;

	private GamePanelReactor reactor;

	/**
	 * Used to support linux os. If a key is hold down on a linux system, it
	 * sends repeatedly pressed- and released-events. So we need a timeout.
	 */
	private final HashMap<Integer, Boolean> pressedButtons;
	private long lastTimePressed;

	private Settings settings;
	private final GamePanelLogic client;

	/**
	 * Creates a new input key handler.
	 * 
	 * @param client
	 *            associated client.
	 * @param reactor
	 *            the reactor that passes actions to server.
	 */
	public InputKeyHandler(GamePanelLogic client, GamePanelReactor reactor) {
		promoter = new ListenerPromoter();
		listeners = new LinkedList<UserInputListener>();
		this.settings = EdurasInitializer.getInstance().getSettings();
		pressedButtons = new HashMap<Integer, Boolean>();

		this.reactor = reactor;

		lastTimePressed = System.currentTimeMillis();

		this.client = client;
	}

	/**
	 * Adds a {@link UserInputListener}.
	 * 
	 * @param listener
	 *            the new listener.
	 * 
	 * @author illonis
	 */
	public void addUserInputListener(UserInputListener listener) {
		listeners.add(listener);
	}

	/**
	 * Checks if given key was pressed recently. If given key is not assigned,
	 * false will be returned.
	 * 
	 * @param key
	 *            key to check.
	 * @return true if key was pressed recently, false otherwise.
	 */
	private boolean justPressed(int key) {
		if (pressedButtons.containsKey(key))
			return pressedButtons.get(key);
		else
			return false;
	}

	/**
	 * Releases all pressed keys and handles keyRelease events.
	 */
	public void releaseAllKeys() {
		for (java.util.Map.Entry<Integer, Boolean> button : pressedButtons
				.entrySet()) {
			if (button.getValue()) {
				keyReleased(button.getKey());
			}
		}
	}

	/**
	 * Handles key press of given key. This includes changing press state and
	 * sending event to server.
	 * 
	 * @param keyCode
	 *            key code of pressed key.
	 */
	private void keyPressed(int keyCode) {
		// don't handle other keys
		if (!settings.getKeyBindings().isBound(keyCode))
			return;

		// if already pressed, do not send a new event
		if (justPressed(keyCode))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode);
		} catch (KeyNotBoundException ex) {
			return;
		}
		pressedButtons.put(keyCode, true);

		switch (binding) {
		case MOVE_UP:
			reactor.onStartMovement(Direction.TOP);
			break;
		case MOVE_LEFT:
			reactor.onStartMovement(Direction.LEFT);
			break;
		case MOVE_DOWN:
			reactor.onStartMovement(Direction.BOTTOM);
			break;
		case MOVE_RIGHT:
			reactor.onStartMovement(Direction.RIGHT);
			break;
		case ITEM_1:
			reactor.onItemUse(0, client.getCurrentMousePos());
			break;
		case ITEM_2:
			reactor.onItemUse(1, client.getCurrentMousePos());
			break;
		case ITEM_3:
			reactor.onItemUse(2, client.getCurrentMousePos());
			break;
		case ITEM_4:
			reactor.onItemUse(3, client.getCurrentMousePos());
			break;
		case ITEM_5:
			reactor.onItemUse(4, client.getCurrentMousePos());
			break;
		case ITEM_6:
			reactor.onItemUse(5, client.getCurrentMousePos());
			break;
		case SHOW_STATS:
			promoter.showStatWindow();
			break;
		case SWITCH_MODE:
			reactor.onModeSwitch();
			break;
		case EXIT_CLIENT:
			reactor.onGameQuit();
			break;

		default:
			break;
		}

		lastTimePressed = System.currentTimeMillis();
		L.fine("Bound key pressed: " + keyCode);
	}

	private void keyReleased(int keyCode) {
		// don't handle other keys
		if (!settings.getKeyBindings().isBound(keyCode))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;

		// release button
		pressedButtons.put(keyCode, false);

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode);
		} catch (KeyNotBoundException ex) {
			L.log(Level.SEVERE, "Key is bound but receiving binding failed: "
					+ keyCode, ex);
			return;
		}

		switch (binding) {
		case MOVE_UP:
			reactor.onStopMovement(Direction.TOP);
			break;
		case MOVE_LEFT:
			reactor.onStopMovement(Direction.LEFT);
			break;
		case MOVE_DOWN:
			reactor.onStopMovement(Direction.BOTTOM);
			break;
		case MOVE_RIGHT:
			reactor.onStopMovement(Direction.RIGHT);
			break;
		case SHOW_STATS:
			promoter.hideStatWindow();
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyReleased(e.getKeyCode());
	}

	private class ListenerPromoter implements UserInputListener {

		@Override
		public void showStatWindow() {
			for (UserInputListener listener : listeners) {
				listener.showStatWindow();
			}
		}

		@Override
		public void hideStatWindow() {
			for (UserInputListener listener : listeners) {
				listener.hideStatWindow();
			}
		}
	}
}