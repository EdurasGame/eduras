package de.illonis.eduras.logic;

import java.util.ArrayList;
import java.util.logging.Level;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Server logic.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerLogic implements GameLogicInterface {

	private final GameInformation gameInfo;
	private final ObjectFactory objectFactory;
	private final LogicGameWorker lgw;
	private final ListenerHolder<GameEventListener> listenerHolder;

	/**
	 * Creates a new logic.
	 * 
	 * @param g
	 *            information to use.
	 */
	public ServerLogic(GameInformation g) {
		listenerHolder = new ListenerHolder<GameEventListener>();
		this.gameInfo = g;
		objectFactory = new ObjectFactory(this);
		lgw = new LogicGameWorker(gameInfo, listenerHolder);
		Thread gameWorker = new Thread(lgw);
		gameWorker.setName("ServerLogicGameWorker");
		gameWorker.start();
	}

	@Override
	public synchronized void onGameEventAppeared(GameEvent event) {
		EduLog.info(Localization.getStringF("Client.networking.event", event
				.getType().toString()));

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

		case INFORMATION_REQUEST:
			ArrayList<GameEvent> infos = gameInfo.getAllInfosAsEvent();

			getListener().onInformationRequested(infos,
					((GameInfoRequest) event).getRequester());

			break;
		case CLIENT_SETNAME:
			ClientRenameEvent e = (ClientRenameEvent) event;
			PlayerMainFigure p = null;
			try {
				p = gameInfo.getPlayerByOwnerId(e.getOwner());
			} catch (ObjectNotFoundException e1) {
				EduLog.warning("There is no such player with the id"
						+ e1.getObjectId() + "(yet)!");
				return;
			}

			EduLog.info("SETTING player found by owner " + e.getOwner()
					+ " to name: " + e.getName() + "  playerid=" + p.getId()
					+ " playerowner=" + p.getOwner());
			p.setName(e.getName());

			getListener().onClientRename(e);

			break;
		case ITEM_USE:
			ItemEvent itemEvent = (ItemEvent) event;
			handleItemEvent(itemEvent);
			break;
		default:
			EduLog.errorLF("Server.networking.illegalevent", event.getClass());
			System.exit(1);
			break;
		}
	}

	/**
	 * Handle an item event.
	 * 
	 * @param itemEvent
	 *            event to handle.
	 */
	private void handleItemEvent(ItemEvent itemEvent) {
		PlayerMainFigure player;

		try {
			player = gameInfo.getPlayerByOwnerId(itemEvent.getOwner());
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
			return;
		}

		Item item;

		try {
			item = player.getInventory().getItemBySlot(itemEvent.getSlotNum());
		} catch (ItemSlotIsEmptyException e) {
			EduLog.log(Level.INFO, e.getMessage());
			return;
		}
		ItemUseInformation useInfo = new ItemUseInformation(player,
				itemEvent.getTarget());

		switch (itemEvent.getType()) {
		case ITEM_USE:
			if (item.isUsable() && !((Usable) item).hasCooldown()) {
				((Usable) item).use(useInfo);
				ItemEvent cooldownEvent = new ItemEvent(
						GameEventNumber.ITEM_CD_START, itemEvent.getOwner(),
						itemEvent.getSlotNum());

				getListener().onCooldownStarted(cooldownEvent);

			}
			break;
		default:
			break;
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

		PlayerMainFigure player = null;
		try {
			player = gameInfo.getPlayerByOwnerId(event.getOwner());
		} catch (ObjectNotFoundException e) {
			EduLog.warningLF("Server.logic.playernotfound", e.getObjectId());
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

	@Override
	public void setGameEventListener(GameEventListener listener) {
		listenerHolder.setListener(listener);
	}

	@Override
	public GameInformation getGame() {
		return gameInfo;
	}

	@Override
	public GameEventListener getListener() {
		return listenerHolder.getListener();
	}

	@Override
	public void onShutdown() {
		lgw.stop();
	}

	@Override
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

}
