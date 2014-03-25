package de.illonis.eduras.gameclient.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Input;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;
import de.illonis.eduras.gameclient.gui.game.UserInputListener;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * This class handles user's key input and passes them to logic.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler {

	private final static Logger L = EduLog.getLoggerFor(InputKeyHandler.class
			.getName());

	private static final long KEY_INTERVAL = 20;
	private final LinkedList<UserInputListener> listeners;
	private final ListenerPromoter promoter;

	private final GamePanelReactor reactor;

	/**
	 * Used to support linux os. If a key is hold down on a linux system, it
	 * sends repeatedly pressed- and released-events. So we need a timeout.
	 */
	private final HashMap<Integer, Boolean> pressedButtons;
	private long lastTimePressed;

	private final Settings settings;
	private final GamePanelLogic client;
	private int currentKey = Input.KEY_UNLABELED;

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
				if (!settings.getKeyBindings().isBound(button.getKey()))
					return;
				// release button
				pressedButtons.put(button.getKey(), false);
			}
		}
	}

	/**
	 * Indicates that a key was pressed.
	 * 
	 * @param key
	 *            the key pressed (see {@link Input}).
	 * @param c
	 *            the key char.
	 */
	public void keyPressed(int key, char c) {
		boolean consumed = false;
		if (key != settings.getKeyBindings().getKey(KeyBinding.CHAT)
				&& key != settings.getKeyBindings().getKey(
						KeyBinding.EXIT_CLIENT))
			consumed = client.onKeyType(key, c);

		if (consumed) {
			currentKey = key;
			return;
		}

		int keyCode = key;
		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode);
		} catch (KeyNotBoundException ex) {
			return;
		}

		// if already pressed, do not send a new event
		if (justPressed(keyCode))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;

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
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(0);
			} else {
				reactor.onItemUse(0, client.getCurrentMousePos());
			}
			break;
		case ITEM_2:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(1);
			} else {
				reactor.onItemUse(1, client.getCurrentMousePos());
			}
			break;
		case ITEM_3:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(2);
			} else {
				reactor.onItemUse(2, client.getCurrentMousePos());
			}
			break;
		case ITEM_4:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(3);
			} else {
				reactor.onItemUse(3, client.getCurrentMousePos());
			}
			break;
		case ITEM_5:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(4);
			} else {
				reactor.onItemUse(4, client.getCurrentMousePos());
			}
			break;
		case ITEM_6:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(5);
			} else {
				reactor.onItemUse(5, client.getCurrentMousePos());
			}
			break;
		case SHOW_STATS:
			promoter.showStatWindow();
			break;
		case SWITCH_MODE:
			reactor.onModeSwitch();
			break;
		case CHAT:
			client.onChatEnter();
			break;
		case EXIT_CLIENT:
			if (!client.abortChat())
				reactor.onGameQuit();
			break;

		default:
			break;
		}

		lastTimePressed = System.currentTimeMillis();
		L.fine("Bound key pressed: " + keyCode);
	}

	/**
	 * Indicates a key release.
	 * 
	 * @param key
	 *            the key code. See fields of {@link Input}.
	 * @param c
	 *            the key char.
	 */
	public void keyReleased(int key, char c) {

		if (key == currentKey) {
			currentKey = Input.KEY_UNLABELED;
			return;
		}
		// don't handle other keys
		if (!settings.getKeyBindings().isBound(key))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;

		// release button
		pressedButtons.put(key, false);

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(key);
		} catch (KeyNotBoundException ex) {
			L.log(Level.SEVERE, "Key is bound but receiving binding failed: "
					+ key, ex);
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

		@Override
		public void onChatEnter() {
			for (UserInputListener listener : listeners) {
				listener.onChatEnter();
			}
		}

		@Override
		public boolean abortChat() {
			for (UserInputListener listener : listeners) {
				if (listener.abortChat())
					return true;
			}
			return false;
		}
	}
}