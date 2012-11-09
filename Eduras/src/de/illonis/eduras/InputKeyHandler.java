package de.illonis.eduras;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.settings.KeyBindings.KeyBinding;
import de.illonis.eduras.settings.Settings;

/**
 * This class handles user's key input and passes them to logic.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler implements KeyListener {

	private static final long KEY_INTERVAL = 20;

	private EventSender eventSender;

	/**
	 * Used to support linux os. If a key is hold down on a linux system, it
	 * sends repeatedly pressed- and released-events. So we need a timeout.
	 */
	private final HashMap<Integer, Boolean> pressedButtons;
	private long lastTimePressed;

	private final int ownerId;
	private Settings settings;

	public InputKeyHandler(int ownerId, EventSender sender, Settings settings) {

		this.settings = settings;
		pressedButtons = new HashMap<Integer, Boolean>();

		this.eventSender = sender;

		lastTimePressed = System.currentTimeMillis();

		this.ownerId = ownerId;
	}

	@Override
	public void keyTyped(KeyEvent e) {
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
	 * Indicates a key release internally. That means key's state will be set to
	 * non-pressed.
	 * 
	 * @param key
	 *            released key.
	 */
	private void keyRelease(int key) {
		pressedButtons.put(key, false);
	}

	@Override
	public void keyPressed(KeyEvent e) {

		// don't handle other keys
		if (!settings.getKeyBindings().isBound(e.getKeyCode()))
			return;

		// if already pressed, do not send a new event
		if (justPressed(e.getKeyCode()))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;

		UserMovementEvent moveEvent = null;

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(e.getKeyCode());
		} catch (KeyNotBoundException ex) {
			return;
		}

		switch (binding) {
		case MOVE_UP:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_PRESSED,
					ownerId);
			break;
		case MOVE_LEFT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_PRESSED, ownerId);
			break;
		case MOVE_DOWN:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_PRESSED, ownerId);
			break;
		case MOVE_RIGHT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_PRESSED, ownerId);
			break;
		default:
			break;
		}

		try {
			eventSender.sendEvent(moveEvent);
		} catch (WrongEventTypeException e1) {
			e1.printStackTrace();
		} catch (MessageNotSupportedException e1) {
			e1.printStackTrace();
		}

		lastTimePressed = System.currentTimeMillis();
		System.out.println("Key pressed: " + e.getKeyCode() + " (\""
				+ e.getKeyChar() + "\")");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// don't handle other keys
		if (!settings.getKeyBindings().isBound(e.getKeyCode()))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;
		UserMovementEvent moveEvent = null;

		keyRelease(e.getKeyCode());

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(e.getKeyCode());
		} catch (KeyNotBoundException ex) {
			return;
		}

		switch (binding) {
		case MOVE_UP:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_RELEASED,
					ownerId);
			break;
		case MOVE_LEFT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_RELEASED, ownerId);
			break;
		case MOVE_DOWN:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_RELEASED, ownerId);
			break;
		case MOVE_RIGHT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_RELEASED, ownerId);
			break;
		default:
			break;
		}

		try {
			eventSender.sendEvent(moveEvent);
		} catch (WrongEventTypeException e1) {
			e1.printStackTrace();
		} catch (MessageNotSupportedException e1) {
			e1.printStackTrace();
		}
	}
}
