package de.illonis.eduras;

import java.util.ArrayList;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * A first (dummy) implementation of game logic.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Logic implements GameLogicInterface {

	Game currentGame;
	private ArrayList<GameEventListener> listenerList;

	public Logic(Game g) {

		this.currentGame = g;
		listenerList = new ArrayList<GameEventListener>();
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

		if (event instanceof MovementEvent) {
			System.out.println("[LOGIC] Game event is a MovementEvent.");

			int yPos;
			int xPos;

			MovementEvent moveEvent = (MovementEvent) event;

			GameObject player = currentGame.getPlayer1();

			switch (moveEvent.getType()) {

			case MOVE_DOWN:
				yPos = player.getYPosition();
				player.setYPosition(--yPos);
				break;
			case MOVE_LEFT:
				xPos = player.getXPosition();
				player.setXPosition(--xPos);
				break;
			case MOVE_RIGHT:
				xPos = player.getXPosition();
				player.setXPosition(++xPos);
				break;
			case MOVE_UP:
				yPos = player.getYPosition();
				player.setYPosition(++yPos);
				break;
			case MOVE_POS:
				int newXPos = moveEvent.getNewXPos();
				int newYPos = moveEvent.getNewYPos();
				player.setYPosition(newYPos);
				player.setXPosition(newXPos);
			default:
				break;
			}

		}
		fireMyEvent();
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
