package de.illonis.eduras;

import java.util.ArrayList;

import de.illonis.eduras.MoveableGameObject.Direction;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
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

	GameInformation currentGame;
	ObjectFactory objectFactory;
	private final ArrayList<GameEventListener> listenerList;

	public Logic(GameInformation g) {

		this.currentGame = g;
		listenerList = new ArrayList<GameEventListener>();
		objectFactory = new ObjectFactory(this);

		Thread gameWorker = new Thread(new LogicGameWorker(currentGame,
				listenerList));
		gameWorker.start();
	}

	/**
	 * Handles an incoming event. See {@link GameEventNumber} for detailed
	 * description.
	 * 
	 * @param event
	 */
	@Override
	public synchronized void onGameEventAppeared(GameEvent event) {
		System.out.println("[LOGIC] A game event appeared: " + event.getType());

		if (event instanceof ObjectFactoryEvent) {
			objectFactory.onGameEventAppeared(event);
		} else {

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
				double newXPos = moveEvent.getNewXPos();
				double newYPos = moveEvent.getNewYPos();
				GameObject o = currentGame.findObjectById(moveEvent
						.getObjectId());
				if (o == null)
					break;
				o.setYPosition(newYPos);
				o.setXPosition(newXPos);

				for (GameEventListener gameEventListener : listenerList) {
					gameEventListener.onNewObjectPosition(o);
				}
				break;
			case INFORMATION_REQUEST:
				ArrayList<GameEvent> infos = currentGame.getAllInfosAsEvent();
				for (GameEventListener listener : listenerList) {
					listener.onInformationRequested(infos,
							((GameInfoRequest) event).getRequester());
				}
				break;
			case CLIENT_SETNAME:
				ClientRenameEvent e = (ClientRenameEvent) event;
				Player p = currentGame.getPlayerByOwnerId(e.getOwner());
				p.setName(e.getName());
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Handles a player move. These events are only received on serverside.
	 * 
	 * @author illonis
	 * @param event
	 *            event.
	 */
	private void handlePlayerMove(UserMovementEvent event) {

		Player player = currentGame.getPlayerByOwnerId(event.getOwner());
		System.out.println("player: " + player);
		System.out.println("eo: " + event.getOwner());

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
	 * Registers given listener for world-change events.
	 * 
	 * @param listener
	 *            Listener to register.
	 */
	@Override
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

	@Override
	public GameInformation getGame() {
		return currentGame;
	}

	@Override
	public ArrayList<GameEventListener> getListenerList() {
		return listenerList;
	}
}
