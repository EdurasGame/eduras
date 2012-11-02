package de.illonis.eduras;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.networking.Client;

/**
 * This class handles user input and passes them to logic.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler implements KeyListener {

	private EventSender eventSender = null;

	private final HashMap<Integer, Boolean> pressedButtons;
	private final CopyOnWriteArraySet<Integer> handledButtons;
	private final int ownerId;

	public InputKeyHandler(GameInformation g, Client client, int ownerId) {
		pressedButtons = new HashMap<Integer, Boolean>();

		handledButtons = new CopyOnWriteArraySet<Integer>();
		handledButtons.add(KeyEvent.VK_UP);
		handledButtons.add(KeyEvent.VK_LEFT);
		handledButtons.add(KeyEvent.VK_DOWN);
		handledButtons.add(KeyEvent.VK_RIGHT);

		for (int k : handledButtons)
			pressedButtons.put(k, false);

		this.ownerId = ownerId;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("Key typed: " + e.getKeyCode() + " (\""
				+ e.getKeyChar() + "\")");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// don't handle other keys
		if (!handledButtons.contains(e.getKeyCode()))
			return;

		// if already pressed, do not send a new event
		if (pressedButtons.get(e.getKeyCode()))
			return;

		UserMovementEvent moveEvent = null;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_PRESSED,
					ownerId);
			break;
		case KeyEvent.VK_A:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_PRESSED, ownerId);
			break;
		case KeyEvent.VK_S:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_PRESSED, ownerId);
			break;
		case KeyEvent.VK_D:
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

		// ((YellowCircle) game.getObjects().get(0))
		// .startMoving(Direction.TOP);

		System.out.println("Key pressed: " + e.getKeyCode() + " (\""
				+ e.getKeyChar() + "\")");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// don't handle other keys
		if (!handledButtons.contains(e.getKeyCode()))
			return;

		UserMovementEvent moveEvent = null;

		pressedButtons.put(e.getKeyCode(), false);

		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_RELEASED,
					ownerId);
			break;
		case KeyEvent.VK_A:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_RELEASED, ownerId);
			break;
		case KeyEvent.VK_S:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_RELEASED, ownerId);
			break;
		case KeyEvent.VK_D:
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

	/**
	 * Returns the eventSender who is responsible for sending events triggered
	 * by key pressings.
	 * 
	 * @return the eventSender
	 */
	public EventSender getEventSender() {
		return eventSender;
	}

	/**
	 * Sets the EventSender who is responsible for sending events triggered by
	 * key pressings.
	 * 
	 * @param eventSender
	 *            the eventSender to set
	 */
	public void setEventSender(EventSender eventSender) {
		this.eventSender = eventSender;
	}
}
