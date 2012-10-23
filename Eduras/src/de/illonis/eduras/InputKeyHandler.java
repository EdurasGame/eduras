package de.illonis.eduras;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.networking.Client;
import de.illonis.eduras.networking.NetworkMessageSerializer;

/**
 * This class handles user input and passes them to logic.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler implements KeyListener {

	private Game game;
	private Client client;

	private HashMap<Integer, Boolean> pressedButtons;
	private CopyOnWriteArraySet<Integer> handledButtons;

	public InputKeyHandler(Game g, Client client) {
		this.game = g;
		this.client = client;
		pressedButtons = new HashMap<Integer, Boolean>();

		handledButtons = new CopyOnWriteArraySet<Integer>();
		handledButtons.add(KeyEvent.VK_UP);
		handledButtons.add(KeyEvent.VK_LEFT);
		handledButtons.add(KeyEvent.VK_DOWN);
		handledButtons.add(KeyEvent.VK_RIGHT);

		for (int k : handledButtons)
			pressedButtons.put(k, false);
	}

	@Deprecated
	public InputKeyHandler(Game g) {
		this(g, null);
		/**
		 * this.game = g; pressedButtons = new HashMap<Integer, Boolean>();
		 * handledButtons = new CopyOnWriteArraySet<Integer>();
		 * handledButtons.add(KeyEvent.VK_UP);
		 * handledButtons.add(KeyEvent.VK_LEFT);
		 * handledButtons.add(KeyEvent.VK_DOWN);
		 * handledButtons.add(KeyEvent.VK_RIGHT);
		 * 
		 * for (int k : handledButtons) pressedButtons.put(k, false);
		 */
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("Key typed: " + e.getKeyCode() + " (\""
				+ e.getKeyChar() + "\")");
	}

	/**
	 * Serializes given event and sends it to server.
	 * 
	 * @param event
	 *            event to handle.
	 */
	private void serializeAndSend(GameEvent event) {

		String msg;
		try {
			msg = NetworkMessageSerializer.serialize(event);
			client.sendMessage(msg);
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// don't handle other keys
		if (!handledButtons.contains(e.getKeyCode()))
			return;

		// if already pressed, do not send a new event
		if (pressedButtons.get(e.getKeyCode()))
			return;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_UP_PRESSED, client.getUserId()));
			break;
		case KeyEvent.VK_A:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_PRESSED, client.getUserId()));
			break;
		case KeyEvent.VK_S:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_PRESSED, client.getUserId()));
			break;
		case KeyEvent.VK_D:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_PRESSED, client.getUserId()));
			break;
		default:
			break;
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

		pressedButtons.put(e.getKeyCode(), false);

		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_UP_RELEASED, client.getUserId()));
			break;
		case KeyEvent.VK_A:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_RELEASED, client.getUserId()));
			break;
		case KeyEvent.VK_S:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_RELEASED, client.getUserId()));
			break;
		case KeyEvent.VK_D:
			serializeAndSend(new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_RELEASED, client.getUserId()));
			break;
		default:
			break;
		}
	}
}
