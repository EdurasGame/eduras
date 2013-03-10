package de.illonis.eduras.gameclient.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameclient.GameClient;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.settings.KeyBindings.KeyBinding;
import de.illonis.eduras.settings.Settings;

/**
 * This class handles user's key input and passes them to logic.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler extends KeyAdapter {

	private static final long KEY_INTERVAL = 20;

	private EventSender eventSender;

	/**
	 * Used to support linux os. If a key is hold down on a linux system, it
	 * sends repeatedly pressed- and released-events. So we need a timeout.
	 */
	private final HashMap<Integer, Boolean> pressedButtons;
	private long lastTimePressed;

	private Settings settings;
	private final GameClient client;

	/**
	 * Creates a new input key handler.
	 * 
	 * @param client
	 *            associated client.
	 * @param sender
	 *            associated event sender.
	 * @param settings
	 *            associated user settings.
	 */
	public InputKeyHandler(GameClient client, EventSender sender,
			Settings settings) {

		this.settings = settings;
		pressedButtons = new HashMap<Integer, Boolean>();

		this.eventSender = sender;

		lastTimePressed = System.currentTimeMillis();

		this.client = client;
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

		UserMovementEvent moveEvent = null;

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode);
		} catch (KeyNotBoundException ex) {
			return;
		}
		pressedButtons.put(keyCode, true);

		switch (binding) {
		case MOVE_UP:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_PRESSED,
					client.getOwnerID());
			break;
		case MOVE_LEFT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_PRESSED, client.getOwnerID());
			break;
		case MOVE_DOWN:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_PRESSED, client.getOwnerID());
			break;
		case MOVE_RIGHT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_PRESSED, client.getOwnerID());
			break;
		case ITEM_1:
			client.itemUsed(0);
			return;
		case ITEM_2:
			client.itemUsed(1);
			return;
		case ITEM_3:
			client.itemUsed(2);
			return;
		case ITEM_4:
			client.itemUsed(3);
			return;
		case ITEM_5:
			client.itemUsed(4);
			return;
		case ITEM_6:
			client.itemUsed(5);
			return;
		case EXIT_CLIENT:
			client.onDisconnect();
			return;

		default:
			return;
		}

		try {
			eventSender.sendEvent(moveEvent);
		} catch (WrongEventTypeException e1) {
			EduLog.passException(e1);
		} catch (MessageNotSupportedException e1) {
			EduLog.passException(e1);
		}

		lastTimePressed = System.currentTimeMillis();
		EduLog.fine("Bound key pressed: " + keyCode);
	}

	private void keyReleased(int keyCode) {
		// don't handle other keys
		if (!settings.getKeyBindings().isBound(keyCode))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;
		UserMovementEvent moveEvent = null;

		// release button
		pressedButtons.put(keyCode, false);

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode);
		} catch (KeyNotBoundException ex) {
			return;
		}

		switch (binding) {
		case MOVE_UP:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_RELEASED,
					client.getOwnerID());
			break;
		case MOVE_LEFT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_RELEASED, client.getOwnerID());
			break;
		case MOVE_DOWN:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_RELEASED, client.getOwnerID());
			break;
		case MOVE_RIGHT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_RELEASED, client.getOwnerID());
			break;
		default:
			return;
		}

		try {
			eventSender.sendEvent(moveEvent);
		} catch (WrongEventTypeException e1) {
			EduLog.passException(e1);
		} catch (MessageNotSupportedException e1) {
			EduLog.passException(e1);
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
}
