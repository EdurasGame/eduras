package de.illonis.eduras;

import java.util.ArrayList;

import de.illonis.eduras.MoveableGameObject.Direction;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;

/**
 * A first (dummy) implementation of game logic.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Logic implements GameLogicInterface {

	Game currentGame;
	ObjectFactory objectFactory;
	private final ArrayList<GameEventListener> listenerList;

	public Logic(Game g) {

		this.currentGame = g;
		listenerList = new ArrayList<GameEventListener>();
		objectFactory = new ObjectFactory(currentGame);
	}

	/**
	 * Handles an incoming event. See {@link GameEventNumber} for detailed
	 * description.
	 * 
	 * @param event
	 */
	@Override
	public synchronized void onGameEventAppeared(GameEvent event) {
		System.out.println("[LOGIC] A game event appeared.");

		if (event instanceof ObjectFactoryEvent) {
			objectFactory.onGameEventAppeared(event);
		} else {

			Player player = currentGame.getPlayer1();

			switch (event.getType()) {
			case MOVE_DOWN_PRESSED:
			case MOVE_DOWN_RELEASED:
			case MOVE_UP_PRESSED:
			case MOVE_UP_RELEASED:
			case MOVE_LEFT_PRESSED:
			case MOVE_LEFT_RELEASED:
			case MOVE_RIGHT_PRESSED:
			case MOVE_RIGHT_RELEASED:
				handlePlayerMove((UserMovementEvent) event);
				break;
			case SET_POS:
				MovementEvent moveEvent = (MovementEvent) event;
				int newXPos = moveEvent.getNewXPos();
				int newYPos = moveEvent.getNewYPos();
				GameObject o = currentGame.findObjectById(moveEvent
						.getObjectId());
				o.setYPosition(newYPos);
				o.setXPosition(newXPos);
			default:
				break;
			}
		}
		fireMyEvent();
	}

	/**
	 * Handles a player move. These events are only received on serverside.
	 * 
	 * @author illonis
	 * @param event
	 *            event.
	 */
	private void handlePlayerMove(UserMovementEvent event) {

		// TODO: find player
		Player player = null;

		switch (event.getType()) {
		case MOVE_DOWN_PRESSED:
			player.startMoving(Direction.BOTTOM);
			break;
		case MOVE_DOWN_RELEASED:
			player.stopMoving(Direction.BOTTOM);
			break;
		case MOVE_UP_PRESSED:
			player.startMoving(Direction.TOP);
			break;
		case MOVE_UP_RELEASED:
			player.stopMoving(Direction.TOP);
			break;
		case MOVE_LEFT_PRESSED:
			player.startMoving(Direction.LEFT);
			break;
		case MOVE_LEFT_RELEASED:
			player.stopMoving(Direction.LEFT);
			break;
		case MOVE_RIGHT_PRESSED:
			player.startMoving(Direction.RIGHT);
			break;
		case MOVE_RIGHT_RELEASED:
			player.stopMoving(Direction.RIGHT);
			break;
		default:
			break;
		}
	}

	/**
	 * Fires a world-change event to all listeners
	 */
	private void fireMyEvent() {

		for (GameEventListener evl : listenerList)
			evl.onWorldChanged();
	}

	/**
	 * Registers given listener for world-change events.
	 * 
	 * @param listener
	 *            Listener to register.
	 */
	public void addGameEventListener(GameEventListener listener) {
		listenerList.add(listener);
	}

	/**
	 * Unregisters given listener for world-change events.
	 * 
	 * @param listener
	 *            Listener to unregister.
	 */
	public void removeGameEventListener(GameEventListener listener) {
		listenerList.remove(listener);
	}

}
