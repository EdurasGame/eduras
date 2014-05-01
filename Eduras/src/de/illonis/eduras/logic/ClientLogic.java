package de.illonis.eduras.logic;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.AddPlayerToTeamEvent;
import de.illonis.eduras.events.AreaConqueredEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.OwnerGameEvent;
import de.illonis.eduras.events.SetAmmunitionEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetFloatGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetMapEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetPolygonDataEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.events.SetStatsEvent;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.NoSuchGameModeException;
import de.illonis.eduras.exceptions.NoSuchMapException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.BasicGameMode;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.maps.Map;
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
	private ObjectFactory objectFactory;
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
		listenerHolder = new ListenerHolder<GameEventListener>();
		lgw = new ClientLogicGameWorker(gameInfo, listenerHolder);
		objectFactory = new ObjectFactory(this, lgw);
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
				float newXPos = moveEvent.getNewXPos();
				float newYPos = moveEvent.getNewYPos();
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
				if (gameObj == null) {
					L.warning("Received polygon data for object with id "
							+ polyEvent.getObjectId()
							+ " which couldn't be found.");
					break;
				}
				if (gameObj instanceof DynamicPolygonObject) {
					((DynamicPolygonObject) gameObj)
							.setPolygonVertices(polyEvent.getVertices());
				} else {
					L.warning("Given object id in SET_POLYGON_DATA event does not match a DynamicPolygonBlock, instead object is a "
							+ gameObj.getClass().getName());
				}
				break;
			case SET_AMMU:
				SetAmmunitionEvent setAmmuEvent = (SetAmmunitionEvent) event;
				GameObject weapon = gameInfo.findObjectById(setAmmuEvent
						.getObjectId());
				if (weapon == null || !(weapon instanceof Weapon)) {
					break;
				}
				Weapon w = (Weapon) weapon;
				w.setCurrentAmmunition(setAmmuEvent.getNewValue());
				break;
			case SET_TEAM_RESOURCE:
				SetTeamResourceEvent setTeamResEvent = (SetTeamResourceEvent) event;
				Team t = gameInfo.findTeamById(setTeamResEvent.getTeamId());
				if (t == null)
					break;
				t.setResource(setTeamResEvent.getNewAmount());
				getListener().onTeamResourceChanged(setTeamResEvent);
				break;
			case SET_VISION_ANGLE:
				if (!(event instanceof SetFloatGameObjectAttributeEvent)) {
					break;
				}
				SetFloatGameObjectAttributeEvent setAngleEvent = (SetFloatGameObjectAttributeEvent) event;
				GameObject angleGameObject = gameInfo
						.findObjectById(setAngleEvent.getObjectId());
				if (angleGameObject == null) {
					break;
				}
				angleGameObject.setVisionAngle(setAngleEvent.getNewValue());
				break;
			case SET_VISION_RANGE:
				if (!(event instanceof SetFloatGameObjectAttributeEvent)) {
					break;
				}
				SetFloatGameObjectAttributeEvent setRangeEvent = (SetFloatGameObjectAttributeEvent) event;
				GameObject rangeGameObject = gameInfo
						.findObjectById(setRangeEvent.getObjectId());
				if (rangeGameObject == null) {
					break;
				}
				rangeGameObject.setVisionAngle(setRangeEvent.getNewValue());
				break;
			case SET_ROTATION:
				if (!(event instanceof SetFloatGameObjectAttributeEvent)) {
					break;
				}
				SetFloatGameObjectAttributeEvent setRotationEvent = (SetFloatGameObjectAttributeEvent) event;
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
					Player mf = gameInfo.getPlayerByOwnerId(interactEvent
							.getOwner());

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
				for (Team team : teamEvent.getTeamList()) {
					gameInfo.addTeam(team);
				}
				break;
			case ADD_PLAYER_TO_TEAM:
				AddPlayerToTeamEvent pteEvent = (AddPlayerToTeamEvent) event;
				Team team = gameInfo.findTeamById(pteEvent.getTeam());
				if (team == null) {
					L.severe("Could not find a team with id "
							+ pteEvent.getTeam()
							+ " while adding player with ownerid "
							+ pteEvent.getOwner() + ".");
					break;
				}
				Player player;
				try {
					player = gameInfo.getPlayerByOwnerId(pteEvent.getOwner());
				} catch (ObjectNotFoundException e) {
					L.log(Level.SEVERE, "Could not find player with ownerid "
							+ pteEvent.getOwner(), e);
					return;
				}
				if (player.getTeam() != null)
					player.getTeam().removePlayer(player);
				team.addPlayer(player);
				break;
			case DEATH:
				DeathEvent de = (DeathEvent) event;
				GameObject killed = gameInfo.findObjectById(de.getKilled());
				if (killed.isUnit()) {
					Unit un = (Unit) killed;
					getListener().onDeath(de);
				}

				break;
			case CLIENT_SETNAME:
				ClientRenameEvent e = (ClientRenameEvent) event;
				Player p = null;
				try {
					p = gameInfo.getPlayerByOwnerId(e.getOwner());
				} catch (ObjectNotFoundException e1) {
					L.warning("There is no such player with the id"
							+ e1.getObjectId() + "(yet)!");
					return;
				}

				L.info("SETTING player found by owner " + e.getOwner()
						+ " to name: " + e.getName() + "  playerid="
						+ " playerowner=" + p.getPlayerId());
				p.setName(e.getName());

				getListener().onClientRename(e);

				break;
			case ITEM_CD_START:
			case ITEM_CD_FINISHED:
				ItemEvent itemEvent = (ItemEvent) event;
				handleItemEvent(itemEvent);
				break;
			case SET_VISION_BLOCKING:
			case SET_COLLIDABLE:
				handleObjectAttributeEvent((SetBooleanGameObjectAttributeEvent) event);
				break;
			case SET_VISIBLE:
				SetVisibilityEvent visionEvent = (SetVisibilityEvent) event;
				GameObject vObject = gameInfo.findObjectById(visionEvent
						.getObjectId());
				if (vObject != null) {
					vObject.setVisible(visionEvent.getNewVisibility());
					getListener().onVisibilityChanged(visionEvent);
				}
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
				String newModeString = modeChangeEvent.getNewMode();

				GameMode newMode = null;
				try {
					newMode = BasicGameMode.getGameModeByName(newModeString,
							getGame());
				} catch (NoSuchGameModeException e1) {
					L.log(Level.SEVERE, "Got unknown game mode", e1);
					return;
				}

				gameInfo.getGameSettings().changeGameMode(newMode);

				getListener().onGameModeChanged(newMode);

				break;
			case SET_MAP: {
				SetMapEvent setMapEvent = (SetMapEvent) event;
				try {
					gameInfo.setMap(Map.getMapByName(setMapEvent
							.getNameOfNewMap()));
					L.info("Set map to " + setMapEvent.getNameOfNewMap());
				} catch (NoSuchMapException e1) {
					L.log(Level.SEVERE,
							"Cannot find map " + setMapEvent.getNameOfNewMap(),
							e1);
				}
				break;
			}
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
			case SET_STATS:
				SetStatsEvent setStatsEvent = (SetStatsEvent) event;
				gameInfo.getGameSettings()
						.getStats()
						.setStatsProperty(setStatsEvent.getProperty(),
								setStatsEvent.getPlayerId(),
								setStatsEvent.getNewCount());
				break;
			case SET_REMAININGTIME:
				SetRemainingTimeEvent remainingTimeEvent = (SetRemainingTimeEvent) event;
				long remainingTime = remainingTimeEvent.getRemainingTime();
				gameInfo.getGameSettings().changeTime(remainingTime);
				break;
			case GAME_READY:
				getListener().onGameReady();
				break;
			case BASE_CONQUERED:
				AreaConqueredEvent baseConqueredEvent = (AreaConqueredEvent) event;

				Team conqueringTeam = gameInfo.findTeamById(baseConqueredEvent
						.getConqueringTeam());
				if (conqueringTeam == null) {
					L.severe("Cannot find team with id "
							+ baseConqueredEvent.getConqueringTeam());
				}

				NeutralArea conqueredArea = (NeutralArea) gameInfo
						.findObjectById(baseConqueredEvent.getBaseId());
				if (conqueredArea == null) {
					L.severe("Cannot find base with id "
							+ baseConqueredEvent.getBaseId());
					break;
				}
				conqueredArea.setCurrentOwnerTeam(conqueringTeam);

				break;
			case PLAYER_JOINED: {
				OwnerGameEvent ownerEvent = (OwnerGameEvent) event;
				gameInfo.addPlayer(new Player(ownerEvent.getOwner(), "unknown"));
				break;
			}
			case PLAYER_LEFT: {
				OwnerGameEvent ownerEvent = (OwnerGameEvent) event;
				gameInfo.removePlayer(ownerEvent.getOwner());
				break;
			}
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
		Player player;

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
			L.info(e.getMessage());
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
		case SET_COLLIDABLE:
			object.setCollidable(event.getNewValue());
			break;
		case SET_VISION_BLOCKING:
			object.setVisionBlocking(event.getNewValue());
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
		// return a dummy listener if no listener is attached to prevent errors.
		if (l == null)
			return new GameEventListener() {

				@Override
				public void onOwnerChanged(SetOwnerEvent event) {
				}

				@Override
				public void onObjectStateChanged(
						SetGameObjectAttributeEvent<?> event) {
				}

				@Override
				public void onObjectRemove(ObjectFactoryEvent event) {
				}

				@Override
				public void onObjectCreation(ObjectFactoryEvent event) {
				}

				@Override
				public void onNewObjectPosition(GameObject object) {
				}

				@Override
				public void onMaxHealthChanged(
						SetIntegerGameObjectAttributeEvent event) {
				}

				@Override
				public void onMatchEnd(MatchEndEvent event) {
				}

				@Override
				public void onItemSlotChanged(SetItemSlotEvent event) {
				}

				@Override
				public void onInteractModeChanged(
						SetInteractModeEvent setModeEvent) {
				}

				@Override
				public void onInformationRequested(ArrayList<GameEvent> infos,
						int targetOwner) {
				}

				@Override
				public void onHealthChanged(
						SetIntegerGameObjectAttributeEvent event) {
				}

				@Override
				public void onGameModeChanged(GameMode newGameMode) {
				}

				@Override
				public void onDeath(DeathEvent event) {
				}

				@Override
				public void onCooldownStarted(ItemEvent event) {
				}

				@Override
				public void onCooldownFinished(ItemEvent event) {
				}

				@Override
				public void onClientRename(ClientRenameEvent event) {
				}

				@Override
				public void onGameReady() {
				}

				@Override
				public void onVisibilityChanged(SetVisibilityEvent event) {
				}

				@Override
				public void onTeamResourceChanged(
						SetTeamResourceEvent setTeamResourceEvent) {
				}
			};
		return l;
	}

	@Override
	public void stopWorker() {
		if (null == lgw)
			return;
		lgw.stop();
		if (null == workerThread)
			return;
		try {
			workerThread.join();
		} catch (InterruptedException e) {
			L.log(Level.SEVERE, "Got interrupted waiting for worker thread.", e);
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
	public LogicGameWorker startWorker() {
		workerThread = new Thread(lgw);
		workerThread.setName("ClientLogicGameWorker");
		workerThread.start();
		return lgw;
	}

}
