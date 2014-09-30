package de.illonis.eduras.logic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.AoEDamageEvent;
import de.illonis.eduras.events.AreaConqueredEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ItemUseFailedEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.OwnerGameEvent;
import de.illonis.eduras.events.PlayerAndTeamEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.SendResourceEvent;
import de.illonis.eduras.events.SetAmmunitionEvent;
import de.illonis.eduras.events.SetAvailableBlinksEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetFloatGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetMapEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetPolygonDataEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.events.SetSettingPropertyEvent;
import de.illonis.eduras.events.SetSettingsEvent;
import de.illonis.eduras.events.SetSizeEvent;
import de.illonis.eduras.events.SetStatsEvent;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.events.SetTimeEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.NoSuchGameModeException;
import de.illonis.eduras.exceptions.NoSuchMapException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gamemodes.BasicGameMode;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.gameobjects.TriggerArea;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.NoSuchItemException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.persistence.InvalidDataException;
import de.illonis.eduras.maps.persistence.MapParser;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.settings.S.SettingType;
import de.illonis.eduras.units.Unit;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.ResourceManager.ResourceType;

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
				GameObject o;
				try {
					o = gameInfo.findObjectById(moveEvent.getObjectId());
				} catch (ObjectNotFoundException e3) {
					L.log(Level.FINE,
							"Object to set position of cannot be found! Probably because it has been regularily removed before.",
							e3);
					break;
				}
				o.setYPosition(newYPos);
				o.setXPosition(newXPos);
				getListener().onNewObjectPosition(o);
				break;
			case INFO_RESPAWN:
				getListener().onRespawn((RespawnEvent) event);
				break;
			case SET_HEALTH:
				SetIntegerGameObjectAttributeEvent healthEvent = (SetIntegerGameObjectAttributeEvent) event;
				GameObject obj;
				try {
					obj = gameInfo.findObjectById(healthEvent.getObjectId());
				} catch (ObjectNotFoundException e3) {
					L.log(Level.WARNING, "Object cannot be found!", e3);
					break;
				}
				Unit unit = (Unit) obj;
				int oldValue = unit.getHealth();
				unit.setHealth(healthEvent.getNewValue());

				getListener().onHealthChanged(unit, oldValue,
						healthEvent.getNewValue());

				break;
			case SET_POLYGON_DATA:
				SetPolygonDataEvent polyEvent = (SetPolygonDataEvent) event;
				GameObject gameObj;
				try {
					gameObj = gameInfo.findObjectById(polyEvent.getObjectId());
				} catch (ObjectNotFoundException e3) {
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
				GameObject weapon;
				try {
					weapon = gameInfo
							.findObjectById(setAmmuEvent.getObjectId());
				} catch (ObjectNotFoundException e3) {
					L.log(Level.WARNING, "Cannot find object.", e3);
					break;
				}
				if (!(weapon instanceof Weapon)) {
					L.warning("Object to set ammu of is of type "
							+ weapon.getType());
					break;
				}
				Weapon w = (Weapon) weapon;
				w.setCurrentAmmunition(setAmmuEvent.getNewValue());
				break;
			case SET_SIZE:
				SetSizeEvent setSizeEvent = (SetSizeEvent) event;
				try {
					GameObject sizeObject = gameInfo
							.findObjectById(setSizeEvent.getObjectId());
					if (sizeObject instanceof TriggerArea) {
						TriggerArea trigger = (TriggerArea) sizeObject;
						((Rectangle) trigger.getShape()).setSize(
								setSizeEvent.getWidth(),
								setSizeEvent.getHeight());
					} else {
						L.log(Level.SEVERE,
								"Tried to change size of an object that is no triggerarea.");
					}
				} catch (ObjectNotFoundException e4) {
					L.log(Level.WARNING,
							"Tried to change object size but object does not exist.",
							e4);
				}

				break;
			case SET_TEAM_RESOURCE:
				SetTeamResourceEvent setTeamResEvent = (SetTeamResourceEvent) event;
				Team t = gameInfo.findTeamById(setTeamResEvent.getTeamId());
				if (t == null)
					break;
				t.setResource(setTeamResEvent.getNewAmount());
				getListener().onTeamResourceChanged(setTeamResEvent);
				break;
			case SET_AVAILABLE_BLINKS: {
				SetAvailableBlinksEvent availableBlinksEvent = (SetAvailableBlinksEvent) event;
				Player player;
				try {
					player = gameInfo.getPlayerByOwnerId(availableBlinksEvent
							.getOwner());
				} catch (ObjectNotFoundException e1) {
					L.log(Level.WARNING, "Cant find player!", e1);
					break;
				}
				player.setBlinksAvailable(availableBlinksEvent.getCharges());
				break;
			}
			case SET_VISION_ANGLE:
				if (!(event instanceof SetFloatGameObjectAttributeEvent)) {
					break;
				}
				SetFloatGameObjectAttributeEvent setAngleEvent = (SetFloatGameObjectAttributeEvent) event;
				GameObject angleGameObject;
				try {
					angleGameObject = gameInfo.findObjectById(setAngleEvent
							.getObjectId());
				} catch (ObjectNotFoundException e3) {
					L.log(Level.WARNING, "Cannot find object!", e3);
					break;
				}
				angleGameObject.setVisionAngle(setAngleEvent.getNewValue());
				break;
			case SET_VISION_RANGE:
				if (!(event instanceof SetFloatGameObjectAttributeEvent)) {
					break;
				}
				SetFloatGameObjectAttributeEvent setRangeEvent = (SetFloatGameObjectAttributeEvent) event;
				GameObject rangeGameObject;
				try {
					rangeGameObject = gameInfo.findObjectById(setRangeEvent
							.getObjectId());
				} catch (ObjectNotFoundException e3) {
					L.log(Level.WARNING, "Cannot find object!", e3);
					break;
				}
				rangeGameObject.setVisionAngle(setRangeEvent.getNewValue());
				break;
			case INFO_PLAYER_JOIN:
				OwnerGameEvent joinEvent = (OwnerGameEvent) event;
				getListener().onPlayerJoined(joinEvent.getOwner());
				break;
			case INFO_PLAYER_LEFT:
				OwnerGameEvent leaveEvent = (OwnerGameEvent) event;
				getListener().onPlayerLeft(leaveEvent.getOwner());
				break;
			case SET_ROTATION:
				if (!(event instanceof SetFloatGameObjectAttributeEvent)) {
					break;
				}
				SetFloatGameObjectAttributeEvent setRotationEvent = (SetFloatGameObjectAttributeEvent) event;
				GameObject gameObject;
				try {
					gameObject = gameInfo.findObjectById(setRotationEvent
							.getObjectId());
				} catch (ObjectNotFoundException e3) {
					L.log(Level.FINE,
							"Cannot find object probably because it was just removed before and this is a UDP event.!",
							e3);
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
				GameObject gobj;
				try {
					gobj = gameInfo.findObjectById(mhealthEvent.getObjectId());
				} catch (ObjectNotFoundException e3) {
					L.log(Level.WARNING, "Cannot find object!", e3);
					break;
				}
				Unit u = (Unit) gobj;
				int oldVal = u.getMaxHealth();
				u.setMaxHealth(mhealthEvent.getNewValue());

				getListener().onHealthChanged(u, oldVal,
						mhealthEvent.getNewValue());

				break;
			case SET_TEAMS:
				SetTeamsEvent teamEvent = (SetTeamsEvent) event;
				gameInfo.clearTeams();
				for (Team team : teamEvent.getTeamList()) {
					gameInfo.addTeam(team);
				}
				getListener().onTeamsSet(teamEvent.getTeamList());
				break;
			case ADD_PLAYER_TO_TEAM:
				PlayerAndTeamEvent pteEvent = (PlayerAndTeamEvent) event;
				Team teamToAddPlayerTo = gameInfo.findTeamById(pteEvent
						.getTeam());
				if (teamToAddPlayerTo == null) {
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

				// remove from current team

				try {
					Team currentTeam;
					currentTeam = player.getTeam();
					currentTeam.removePlayer(player);
				} catch (PlayerHasNoTeamException e2) {
					// do nothing here
				}

				teamToAddPlayerTo.addPlayer(player);
				break;
			case DEATH:
				DeathEvent de = (DeathEvent) event;
				GameObject killed;
				try {
					killed = gameInfo.findObjectById(de.getKilled());
				} catch (ObjectNotFoundException e2) {
					L.log(Level.WARNING,
							"Cannot find object that was just killed!!", e2);
					break;
				}
				if (killed.isUnit()) {
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
			case ITEM_USE_FAILED:
				ItemUseFailedEvent itemFailedEvent = (ItemUseFailedEvent) event;
				getListener().onItemUseFailed(itemFailedEvent);
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
				GameObject vObject;
				try {
					vObject = gameInfo
							.findObjectById(visionEvent.getObjectId());
				} catch (ObjectNotFoundException e2) {
					L.log(Level.WARNING, "Cannot find object!", e2);
					break;
				}
				vObject.setVisible(visionEvent.getNewVisibility());
				getListener().onVisibilityChanged(visionEvent);
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
			case START_ROUND:
				getListener().onStartRound();
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
			case AOE_DAMAGE:
				getListener().onAoEDamage((AoEDamageEvent) event);
				break;
			case SET_MAP: {
				SetMapEvent setMapEvent = (SetMapEvent) event;
				try {
					gameInfo.setMap(Map.getMapByName(setMapEvent
							.getNameOfNewMap()));

					try {
						String hashOfMap = ResourceManager.getHashOfResource(
								ResourceType.MAP, setMapEvent.getNameOfNewMap()
										+ MapParser.FILE_EXTENSION);
						if (!(hashOfMap.equals(setMapEvent.getHashOfMap()))) {

							L.info("Hashs of map "
									+ setMapEvent.getNameOfNewMap()
									+ " differ. On server: "
									+ setMapEvent.getHashOfMap()
									+ ". On Client: " + hashOfMap);

							throw new NoSuchMapException(
									setMapEvent.getNameOfNewMap());
						}
					} catch (IOException e1) {
						L.log(Level.SEVERE,
								"Cannot find map."
										+ setMapEvent.getNameOfNewMap(), e1);
					}
				} catch (NoSuchMapException | InvalidDataException e1) {
					L.log(Level.SEVERE,
							"Cannot find or load map or have a different version of map "
									+ setMapEvent.getNameOfNewMap(), e1);

					getListener().onResourceRequired(
							GameEventNumber.REQUEST_MAP,
							setMapEvent.getNameOfNewMap());
				}

				L.info("Set map to " + setMapEvent.getNameOfNewMap());
				getListener().onMapChanged(setMapEvent);
				break;
			}
			case SET_OWNER:
				SetOwnerEvent setownerEvent = (SetOwnerEvent) event;
				Item item;
				try {
					item = (Item) gameInfo.findObjectById(setownerEvent
							.getObjectId());
				} catch (ObjectNotFoundException e2) {
					L.log(Level.WARNING, "Cannot find item object!", e2);
					break;
				}
				item.setOwner(setownerEvent.getOwner());
				getListener().onOwnerChanged(setownerEvent);

				break;
			case SET_KILLS:
				SetIntegerGameObjectAttributeEvent setKillsEvent = (SetIntegerGameObjectAttributeEvent) event;
				int ownerId = setKillsEvent.getObjectId();
				int newCount = setKillsEvent.getNewValue();
				Player killerPlayer;
				try {
					killerPlayer = gameInfo.getPlayerByOwnerId(ownerId);
					gameInfo.getGameSettings().getStats()
							.setKills(killerPlayer, newCount);
				} catch (ObjectNotFoundException ex) {
					L.log(Level.SEVERE,
							"Could not find player while setting kills.", ex);
				}
				break;
			case SET_DEATHS:
				SetIntegerGameObjectAttributeEvent setDeathsEvent = (SetIntegerGameObjectAttributeEvent) event;
				ownerId = setDeathsEvent.getObjectId();
				newCount = setDeathsEvent.getNewValue();
				Player deathPlayer;
				try {
					deathPlayer = gameInfo.getPlayerByOwnerId(ownerId);
					gameInfo.getGameSettings().getStats()
							.setDeaths(deathPlayer, newCount);
				} catch (ObjectNotFoundException ex) {
					L.log(Level.SEVERE,
							"Could not find player while setting deaths.", ex);
				}
				break;
			case SET_STATS:
				SetStatsEvent setStatsEvent = (SetStatsEvent) event;
				Player statPlayer;
				try {
					statPlayer = gameInfo.getPlayerByOwnerId(setStatsEvent
							.getPlayerId());
					gameInfo.getGameSettings()
							.getStats()
							.setStatsProperty(setStatsEvent.getProperty(),
									statPlayer, setStatsEvent.getNewCount());
				} catch (ObjectNotFoundException ex) {
					L.log(Level.SEVERE,
							"Could not find player while setting stat "
									+ setStatsEvent.getProperty(), ex);
				}

				break;
			case SET_REMAININGTIME:
				SetRemainingTimeEvent remainingTimeEvent = (SetRemainingTimeEvent) event;
				long remainingTime = remainingTimeEvent.getRemainingTime();
				gameInfo.getGameSettings().changeTime(remainingTime);
				break;
			case SET_RESPAWNTIME:
				EdurasInitializer
						.getInstance()
						.getInformationProvider()
						.setRespawnTime(
								((SetTimeEvent) event).getRemainingTime());
				break;
			case GAME_READY:
				getListener().onGameReady();
				break;
			case SEND_MAP:
				try {
					String nameOfReceivedMap = ((SendResourceEvent) event)
							.getResourceName();
					gameInfo.setMap(Map.getMapByName(nameOfReceivedMap));
					getListener()
							.onMapChanged(
									new SetMapEvent(
											nameOfReceivedMap,
											ResourceManager
													.getHashOfResource(
															ResourceType.MAP,
															nameOfReceivedMap
																	+ MapParser.FILE_EXTENSION)));
				} catch (NoSuchMapException | InvalidDataException
						| IOException e3) {
					L.log(Level.SEVERE,
							"Cannot switch to the map we just received.", e3);
				}
				break;
			case BASE_CONQUERED:
				AreaConqueredEvent baseConqueredEvent = (AreaConqueredEvent) event;

				Team conqueringTeam = gameInfo.findTeamById(baseConqueredEvent
						.getConqueringTeam());
				if (conqueringTeam == null) {
					L.severe("Cannot find team with id "
							+ baseConqueredEvent.getConqueringTeam());
				}

				NeutralArea conqueredArea;
				try {
					conqueredArea = (NeutralArea) gameInfo
							.findObjectById(baseConqueredEvent.getBaseId());
				} catch (ObjectNotFoundException e2) {
					L.severe("Cannot find base with id "
							+ baseConqueredEvent.getBaseId());
					break;
				}
				conqueredArea.setCurrentOwnerTeam(conqueringTeam);

				getListener().onBaseConquered((Base) conqueredArea,
						conqueringTeam);
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
			case SET_SETTINGS: {
				S.loadSettings(((SetSettingsEvent) event).getSettingsFile(),
						SettingType.SERVER);
				break;
			}
			case SET_SETTINGPROPERTY: {
				try {
					S.setServerSetting(
							((SetSettingPropertyEvent) event).getSettingName(),
							((SetSettingPropertyEvent) event).getSettingValue());
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException e1) {
					L.log(Level.WARNING,
							"An exception occured when trying to set a setting property!",
							e1);
				}
				break;
			}
			case SET_SPEEDVECTOR: {
				MovementEvent setSpeedVectorEvent = (MovementEvent) event;
				try {
					GameObject object = gameInfo
							.findObjectById(setSpeedVectorEvent.getObjectId());
					if (!(object instanceof MoveableGameObject)) {
						L.warning("Trying to set speed of an object that isn't a moveable one.");
						break;
					} else {
						((MoveableGameObject) object)
								.setSpeedVector(new Vector2f(
										setSpeedVectorEvent.getNewXPos(),
										setSpeedVectorEvent.getNewYPos()));
					}
				} catch (ObjectNotFoundException e1) {
					L.log(Level.WARNING,
							"Cannot find object to set the speed of!", e1);
					break;
				}
				break;
			}
			case SET_SPEED: {
				SetFloatGameObjectAttributeEvent setSpeedEvent = (SetFloatGameObjectAttributeEvent) event;
				try {
					GameObject object = gameInfo.findObjectById(setSpeedEvent
							.getObjectId());
					if (!(object instanceof MoveableGameObject)) {
						L.warning("Trying to set speed of an object that isn't a moveable one.");
						return;
					} else {
						((MoveableGameObject) object).setSpeed(setSpeedEvent
								.getNewValue());
					}
				} catch (ObjectNotFoundException e1) {
					L.log(Level.WARNING,
							"Cannot find object to set the speed of!", e1);
					break;
				}
				break;
			}
			default:
				L.warning("Received an event that we cannot handle: "
						+ event.getEventNumber());
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
			item = player.getInventory().getItemOfType(itemEvent.getItemType());
		} catch (NoSuchItemException e) {
			L.log(Level.WARNING,
					"Could not handle item event because item is not in inventory.",
					e);
			return;
		}
		ItemEvent cooldownEvent = new ItemEvent(GameEventNumber.ITEM_CD_START,
				itemEvent.getOwner(), itemEvent.getItemType());

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

		GameObject object;
		try {
			object = getGame().findObjectById(event.getObjectId());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object!!", e);
			return;
		}
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
			return new GameEventAdapter();
		return l;
	}

	@Override
	public void stopWorker() {
		// only need to stop the thread if we have created and started it before
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

	@Override
	public LogicGameWorker startWorker(boolean useInternal) {
		// we don't need to start the thread, if the user doesn't want to
		if (useInternal) {
			workerThread = new Thread(lgw);
			workerThread.start();
		}
		return lgw;
	}
}
