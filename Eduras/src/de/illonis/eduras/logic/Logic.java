package de.illonis.eduras.logic;

import java.util.ArrayList;
import java.util.logging.Level;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.MoveableGameObject.Direction;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.LootItemEvent;
import de.illonis.eduras.events.MissileLaunchEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.InventoryIsFullException;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.items.weapons.SimpleMissile;
import de.illonis.eduras.logger.EduLog;

/**
 * A first (dummy) implementation of game logic.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Logic implements GameLogicInterface {

	private final GameInformation currentGame;
	private final ObjectFactory objectFactory;
	private final LogicGameWorker lgw;
	private final ArrayList<GameEventListener> listenerList;

	public Logic(GameInformation g) {

		this.currentGame = g;
		listenerList = new ArrayList<GameEventListener>();
		objectFactory = new ObjectFactory(this);
		lgw = new LogicGameWorker(currentGame, listenerList);
		Thread gameWorker = new Thread(lgw);
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
		EduLog.info("[LOGIC] A game event appeared: "
				+ event.getType().toString());

		if (event instanceof ObjectFactoryEvent) {
			objectFactory
					.onObjectFactoryEventAppeared((ObjectFactoryEvent) event);
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
				Player p = null;
				try {
					p = currentGame.getPlayerByOwnerId(e.getOwner());
				} catch (ObjectNotFoundException e1) {
					EduLog.warning("There is no such player with the id"
							+ e1.getObjectId() + "(yet)!");
					return;
				}

				EduLog.info("SETTING player found by owner " + e.getOwner()
						+ " to name: " + e.getName() + "  playerid="
						+ p.getId() + " playerowner=" + p.getOwner());
				p.setName(e.getName());

				for (GameEventListener listener : listenerList) {
					listener.onClientRename(e);
				}
				break;
			case ITEM_USE:
				ItemEvent itemEvent = (ItemEvent) event;
				handleItemEvent(itemEvent);
				break;
			case SET_COLLIDABLE:
			case SET_VISIBLE:
				handleObjectAttributeEvent((SetBooleanGameObjectAttributeEvent) event);
				break;
			case MISSILE_LAUNCH:
				MissileLaunchEvent missileLaunchEvent = (MissileLaunchEvent) event;

				Missile missile;
				switch (missileLaunchEvent.getObjectType()) {
				case SIMPLEMISSILE:
					missile = new SimpleMissile(currentGame);
					break;
				default:
					return;
				}

				missile.setPosition(missileLaunchEvent.getPosition().getX(),
						missileLaunchEvent.getPosition().getY());
				missile.setOwner(missileLaunchEvent.getOwner());
				missile.setSpeedVector(missileLaunchEvent.getSpeedVector());
				// TODO: inform gameeventlisteners!

				// inform listeners

				ObjectFactoryEvent objectFactoryEvent = new ObjectFactoryEvent(
						GameEventNumber.OBJECT_CREATE,
						missileLaunchEvent.getObjectType());
				objectFactoryEvent.setId(missile.getId());
				objectFactoryEvent.setOwner(missile.getOwner());

				for (GameEventListener gel : listenerList) {
					gel.onObjectCreation(objectFactoryEvent);
					gel.onNewObjectPosition(missile);
					// announce speedvector
				}
				break;
			case LOOT_ITEM_EVENT:
				LootItemEvent lootItemEvent = (LootItemEvent) event;
				Player player;
				int itemSlot;
				try {
					player = currentGame.getPlayerByObjectId(lootItemEvent
							.getPlayerId());
					Item item = (Item) currentGame.findObjectById(lootItemEvent
							.getObjectId());
					itemSlot = player.getInventory().loot(item);

					item.setCollidable(false);
					item.setVisible(false);

					SetBooleanGameObjectAttributeEvent visEvent = new SetBooleanGameObjectAttributeEvent(
							GameEventNumber.SET_VISIBLE, item.getId(), false);
					SetBooleanGameObjectAttributeEvent colEvent = new SetBooleanGameObjectAttributeEvent(
							GameEventNumber.SET_COLLIDABLE, item.getId(), false);

					for (GameEventListener gel : listenerList) {
						gel.onItemSlotChanged(new SetItemSlotEvent(
								lootItemEvent.getObjectId(), player.getOwner(),
								itemSlot));
						gel.onObjectStateChanged(visEvent);
						gel.onObjectStateChanged(colEvent);
					}

				} catch (ObjectNotFoundException e1) {
					return;
				} catch (InventoryIsFullException e1) {
					return;
				}

				break;
			case SET_ITEM_SLOT:
				SetItemSlotEvent slotEvent = (SetItemSlotEvent) event;
				try {
					currentGame
							.getPlayerByOwnerId(slotEvent.getOwner())
							.getInventory()
							.setItemAt(
									slotEvent.getItemSlot(),
									(Item) currentGame.getObjects().get(
											slotEvent.getObjectId()));
				} catch (ObjectNotFoundException e1) {
					EduLog.passException(e1);
				}
				for (GameEventListener listener : listenerList) {
					listener.onItemSlotChanged(slotEvent);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @param event
	 */
	private void handleObjectAttributeEvent(
			SetBooleanGameObjectAttributeEvent event) {

		GameObject object = getGame().findObjectById(event.getObjectId());

		switch (event.getType()) {
		case SET_VISIBLE:
			object.setVisible(event.getNewValue());
			break;
		case SET_COLLIDABLE:
			object.setCollidable(event.getNewValue());
			break;
		default:
		}

	}

	/**
	 * Handle an item event.
	 * 
	 * @param itemEvent
	 */
	private void handleItemEvent(ItemEvent itemEvent) {

		GameInformation gameInfo = getGame();
		Player player = null;

		try {
			player = gameInfo.getPlayerByOwnerId(itemEvent.getOwner());
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
			return;
		}

		Item item = null;

		try {
			item = player.getInventory().getItemBySlot(itemEvent.getSlotNum());
		} catch (ItemSlotIsEmptyException e) {
			EduLog.passException(e);
			return;
		}

		ItemUseInformation useInfo = new ItemUseInformation(player,
				itemEvent.getTarget());

		if (item.isUsable()) {
			((Usable) item).use(useInfo);
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

		Player player = null;
		try {
			player = currentGame.getPlayerByOwnerId(event.getOwner());
		} catch (ObjectNotFoundException e) {
			EduLog.log(Level.WARNING,
					"The player with the id " + e.getObjectId()
							+ "could not be found!");
			return;
		}

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

	@Override
	public void onShutdown() {
		lgw.stop();
	}

	/**
	 * @return
	 */
	@Override
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

}
