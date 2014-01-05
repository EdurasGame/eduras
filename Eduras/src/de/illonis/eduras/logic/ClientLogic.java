package de.illonis.eduras.logic;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.Team;
import de.illonis.eduras.Team.TeamColor;
import de.illonis.eduras.events.AddPlayerToTeamEvent;
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
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetPolygonDataEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.Deathmatch;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.NoGameMode;
import de.illonis.eduras.gamemodes.TeamDeathmatch;
import de.illonis.eduras.gameobjects.DynamicPolygonBlock;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Logic for client.
 * 
 * @author illonis
 * 
 */
public class ClientLogic implements GameLogicInterface {

	private final static Logger L = EduLog.getLoggerFor(ClientLogic.class
			.getName());

	private final GameInformation gameInfo;
	private final ObjectFactory objectFactory;
	private LogicGameWorker lgw;
	private final ListenerHolder<GameEventListener> listenerHolder;
	private Thread workerThread;

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

	}

	@Override
	public void onGameEventAppeared(GameEvent event) {
		L.fine("[LOGIC] A game event appeared: " + event.getType().toString());

		if (event instanceof ObjectFactoryEvent) {
			objectFactory
					.onObjectFactoryEventAppeared((ObjectFactoryEvent) event);
		} else {

			switch (event.getType()) {
			case SET_POS_TCP:
			case SET_POS_UDP:
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
			case SET_HEALTH:
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
					L.warning("Given object id in SET_POLYGON_DATA event does not match a DynamicPolygonBlock, instead object is a "
							+ gameObj.getClass().getName());
				}
				break;
			case SET_ROTATION:
				if (!(event instanceof SetGameObjectAttributeEvent<?>)) {
					break;
				}
				SetGameObjectAttributeEvent<Double> setRotationEvent = (SetGameObjectAttributeEvent<Double>) event;
				GameObject gameObject = gameInfo
						.findObjectById(setRotationEvent.getObjectId());
				if (gameObject == null) {
					break;
				}
				gameObject.setRotation(setRotationEvent.getNewValue());
				break;
			case SET_INTERACTMODE:
				SetInteractModeEvent interactEvent = (SetInteractModeEvent) event;
				try {
					PlayerMainFigure mf = gameInfo
							.getPlayerByOwnerId(interactEvent.getOwner());

					mf.setMode(interactEvent.getNewMode());
					getListener().onInteractModeChanged(interactEvent);
				} catch (ObjectNotFoundException e2) {
					L.log(Level.SEVERE,
							"Got an interaction mode set for a player that does not exist on this client.",
							e2);
				}
				break;
			case SET_MAXHEALTH:
				SetIntegerGameObjectAttributeEvent mhealthEvent = (SetIntegerGameObjectAttributeEvent) event;
				GameObject gobj = gameInfo.findObjectById(mhealthEvent
						.getObjectId());
				if (gobj == null)
					break;
				Unit u = (Unit) gobj;
				u.setMaxHealth(mhealthEvent.getNewValue());

				getListener().onHealthChanged(mhealthEvent);

				break;
			case SET_TEAMS:
				SetTeamsEvent teamEvent = (SetTeamsEvent) event;
				gameInfo.clearTeams();
				for (TeamColor color : teamEvent.getTeamList().keySet()) {
					String name = teamEvent.getTeamList().get(color);
					Team t = new Team(name, color);
					gameInfo.addTeam(t);
				}
				break;
			case ADD_PLAYER_TO_TEAM:
				AddPlayerToTeamEvent pteEvent = (AddPlayerToTeamEvent) event;
				for (Team t : gameInfo.getTeams()) {
					if (t.getColor() == pteEvent.getTeamColor()) {
						PlayerMainFigure player;
						try {
							player = gameInfo.getPlayerByOwnerId(pteEvent
									.getOwner());
						} catch (ObjectNotFoundException e1) {
							return;
						}
						if (player.getTeam() != null)
							player.getTeam().removePlayer(player);
						t.addPlayer(player);
					}
				}
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
					L.warning("There is no such player with the id"
							+ e1.getObjectId() + "(yet)!");
					return;
				}

				L.info("SETTING player found by owner " + e.getOwner()
						+ " to name: " + e.getName() + "  playerid="
						+ p.getId() + " playerowner=" + p.getOwner());
				p.setName(e.getName());

				getListener().onClientRename(e);

				break;
			case ITEM_CD_START:
			case ITEM_CD_FINISHED:
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
					L.log(Level.SEVERE, "player not found", e1);
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
				case "Team-Deathmatch":
					newGameMode = new TeamDeathmatch(gameInfo);
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
			case GAME_READY:
				getListener().onGameReady();
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
			L.log(Level.SEVERE, "player not found", e);
			return;
		}

		Item item;

		try {
			item = player.getInventory().getItemBySlot(itemEvent.getSlotNum());
		} catch (ItemSlotIsEmptyException e) {
			L.warning(e.getMessage());
			return;
		}
		ItemEvent cooldownEvent = new ItemEvent(GameEventNumber.ITEM_CD_START,
				itemEvent.getOwner(), itemEvent.getSlotNum());

		switch (itemEvent.getType()) {
		case ITEM_CD_START:
			if (item.isUsable())
				((Usable) item).startCooldown();
			if (item instanceof Weapon) {
				((Weapon) item).reduceAmmo();
			}
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
		if (object == null)
			return;
		// FIXME: fix null objects.

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
		GameEventListener l = listenerHolder.getListener();
		if (l == null)
			return new GameEventListener() {

				@Override
				public void onOwnerChanged(SetOwnerEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onObjectStateChanged(
						SetGameObjectAttributeEvent<?> event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onObjectRemove(ObjectFactoryEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onObjectCreation(ObjectFactoryEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onNewObjectPosition(GameObject object) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onMaxHealthChanged(
						SetIntegerGameObjectAttributeEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onMatchEnd(MatchEndEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onItemSlotChanged(SetItemSlotEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onInteractModeChanged(
						SetInteractModeEvent setModeEvent) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onInformationRequested(ArrayList<GameEvent> infos,
						int targetOwner) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onHealthChanged(
						SetIntegerGameObjectAttributeEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onGameModeChanged(GameMode newGameMode) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onDeath(DeathEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onCooldownStarted(ItemEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onCooldownFinished(ItemEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onClientRename(ClientRenameEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onGameReady() {
					// TODO Auto-generated method stub

				}
			};
		return l;
	}

	@Override
	public void stopWorker() {
		if (null == lgw)
			return;
		lgw.stop();
		try {
			workerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	@Override
	public void setGameEventListener(GameEventListener listener) {
		listenerHolder.setListener(listener);
	}

	@Override
	public void startWorker() {
		lgw = new LogicGameWorker(gameInfo, listenerHolder);
		workerThread = new Thread(lgw);
		workerThread.setName("ClientLogicGameWorker");
		workerThread.start();
	}

}
