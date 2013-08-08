package de.illonis.eduras.logic;

import java.util.logging.Level;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetPolygonDataEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.Deathmatch;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.NoGameMode;
import de.illonis.eduras.gameobjects.DynamicPolygonBlock;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Logic for client.
 * 
 * @author illonis
 * 
 */
public class ClientLogic implements GameLogicInterface {

	private final GameInformation gameInfo;
	private final ObjectFactory objectFactory;
	private final LogicGameWorker lgw;
	private final ListenerHolder<GameEventListener> listenerHolder;

	/**
	 * Create ClientLogic instant.
	 * 
	 * @param g
	 *            The game information.
	 * 
	 */
	public ClientLogic(GameInformation g) {
		this.gameInfo = g;
		objectFactory = new ObjectFactory(this);
		listenerHolder = new ListenerHolder<GameEventListener>();
		lgw = new LogicGameWorker(gameInfo, listenerHolder);
		Thread gameWorker = new Thread(lgw);
		gameWorker.setName("LogicGameWorker");
		gameWorker.start();
	}

	@Override
	public void onGameEventAppeared(GameEvent event) {
		EduLog.info("[LOGIC] A game event appeared: "
				+ event.getType().toString());

		if (event instanceof ObjectFactoryEvent) {
			objectFactory
					.onObjectFactoryEventAppeared((ObjectFactoryEvent) event);
		} else {

			switch (event.getType()) {

			case SET_POS:
				MovementEvent moveEvent = (MovementEvent) event;
				double newXPos = moveEvent.getNewXPos();
				double newYPos = moveEvent.getNewYPos();
				GameObject o = gameInfo.findObjectById(moveEvent.getObjectId());
				if (o == null)
					break;
				o.setYPosition(newYPos);
				o.setXPosition(newXPos);

				getListener().onNewObjectPosition(o);

				break;
			case SETHEALTH:
				SetIntegerGameObjectAttributeEvent healthEvent = (SetIntegerGameObjectAttributeEvent) event;
				GameObject obj = gameInfo.findObjectById(healthEvent
						.getObjectId());
				if (obj == null)
					break;
				Unit unit = (Unit) obj;
				unit.setHealth(healthEvent.getNewValue());

				getListener().onHealthChanged(healthEvent);

				break;
			case SET_POLYGON_DATA:
				SetPolygonDataEvent polyEvent = (SetPolygonDataEvent) event;
				GameObject gameObj = gameInfo.findObjectById(polyEvent
						.getObjectId());
				if (gameObj == null)
					break;
				if (gameObj instanceof DynamicPolygonBlock) {
					((DynamicPolygonBlock) gameObj)
							.setPolygonVertices(polyEvent.getVertices());
				} else {
					EduLog.warning("Given object id in SET_POLYGON_DATA event does not match a DynamicPolygonBlock, instead object is a "
							+ gameObj.getClass().getName());
				}
				break;
			case SETMAXHEALTH:
				SetIntegerGameObjectAttributeEvent mhealthEvent = (SetIntegerGameObjectAttributeEvent) event;
				GameObject gobj = gameInfo.findObjectById(mhealthEvent
						.getObjectId());
				if (gobj == null)
					break;
				Unit u = (Unit) gobj;
				u.setMaxHealth(mhealthEvent.getNewValue());

				getListener().onHealthChanged(mhealthEvent);

				break;

			case DEATH:
				DeathEvent de = (DeathEvent) event;
				GameObject killed = gameInfo.findObjectById(de.getKilled());
				if (killed.isUnit()) {
					Unit un = (Unit) killed;
					gameInfo.getGameSettings().getGameMode()
							.onDeath(un, de.getKillerOwner());
					getListener().onDeath(de);

				}

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
						+ " to name: " + e.getName() + "  playerid="
						+ p.getId() + " playerowner=" + p.getOwner());
				p.setName(e.getName());

				getListener().onClientRename(e);

				break;
			case ITEM_CD_START:
			case ITEM_CD_FINISHED:
			case ITEM_USE:
				ItemEvent itemEvent = (ItemEvent) event;
				handleItemEvent(itemEvent);
				break;
			case SET_COLLIDABLE:
			case SET_VISIBLE:
				handleObjectAttributeEvent((SetBooleanGameObjectAttributeEvent) event);
				break;
			case SET_ITEM_SLOT:
				SetItemSlotEvent slotEvent = (SetItemSlotEvent) event;
				try {
					gameInfo.getPlayerByOwnerId(slotEvent.getOwner())
							.getInventory()
							.setItemAt(
									slotEvent.getItemSlot(),
									(Item) gameInfo.getObjects().get(
											slotEvent.getObjectId()));
				} catch (ObjectNotFoundException e1) {
					EduLog.passException(e1);
				}
				getListener().onItemSlotChanged(slotEvent);

				break;
			case MATCH_END:
				getListener().onMatchEnd((MatchEndEvent) event);

				break;
			case SET_GAMEMODE:
				SetGameModeEvent modeChangeEvent = (SetGameModeEvent) event;
				GameMode newGameMode;
				String newMode = modeChangeEvent.getNewMode();
				switch (newMode) {
				case "Deathmatch":
					newGameMode = new Deathmatch(gameInfo);
					break;
				default:
					newGameMode = new NoGameMode(gameInfo);
					break;
				}

				gameInfo.getGameSettings().changeGameMode(newGameMode);

				getListener().onGameModeChanged(newGameMode);

				break;
			case SET_OWNER:
				SetOwnerEvent setownerEvent = (SetOwnerEvent) event;
				Item item = (Item) gameInfo.findObjectById(setownerEvent
						.getObjectId());
				item.setOwner(setownerEvent.getOwner());
				getListener().onOwnerChanged(setownerEvent);

				break;
			case SET_KILLS:
				SetIntegerGameObjectAttributeEvent setKillsEvent = (SetIntegerGameObjectAttributeEvent) event;
				int ownerId = setKillsEvent.getObjectId();
				int newCount = setKillsEvent.getNewValue();
				gameInfo.getGameSettings().getStats()
						.setKills(ownerId, newCount);
				break;
			case SET_DEATHS:
				SetIntegerGameObjectAttributeEvent setDeathsEvent = (SetIntegerGameObjectAttributeEvent) event;
				ownerId = setDeathsEvent.getObjectId();
				newCount = setDeathsEvent.getNewValue();
				gameInfo.getGameSettings().getStats()
						.setDeaths(ownerId, newCount);
				break;
			case SET_REMAININGTIME:
				SetRemainingTimeEvent remainingTimeEvent = (SetRemainingTimeEvent) event;
				long remainingTime = remainingTimeEvent.getRemainingTime();
				gameInfo.getGameSettings().changeTime(remainingTime);
				break;
			default:
				break;
			}
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
			EduLog.log(Level.WARNING, e.getMessage());
			return;
		}
		ItemEvent cooldownEvent = new ItemEvent(GameEventNumber.ITEM_CD_START,
				itemEvent.getOwner(), itemEvent.getSlotNum());

		switch (itemEvent.getType()) {
		case ITEM_USE:
		case ITEM_CD_START:
			if (item.isUsable())
				((Usable) item).startCooldown();
			getListener().onCooldownStarted(cooldownEvent);

			break;
		case ITEM_CD_FINISHED:
			if (item.isUsable())
				((Usable) item).resetCooldown();

			cooldownEvent.setType(GameEventNumber.ITEM_CD_FINISHED);
			getListener().onCooldownFinished(cooldownEvent);

			break;
		default:
			break;
		}

	}

	/**
	 * Handles an boolean object attribute event.
	 * 
	 * @param event
	 *            event to handle.
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

		getListener().onObjectStateChanged(event);

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

	@Override
	public void setGameEventListener(GameEventListener listener) {
		listenerHolder.setListener(listener);
	}

}
