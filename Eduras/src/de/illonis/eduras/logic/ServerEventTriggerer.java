package de.illonis.eduras.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.Event.PacketType;
import de.eduras.eventingserver.ServerInterface;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.eduras.eventingserver.test.NoSuchClientException;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.ai.movement.MotionAIControllable;
import de.illonis.eduras.ai.movement.MovingUnitAI;
import de.illonis.eduras.ai.movement.UnitNotControllableException;
import de.illonis.eduras.events.AoEDamageEvent;
import de.illonis.eduras.events.AreaConqueredEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameReadyEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ItemUseFailedEvent;
import de.illonis.eduras.events.ItemUseFailedEvent.Reason;
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
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
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
import de.illonis.eduras.events.StartRoundEvent;
import de.illonis.eduras.exceptions.GameModeNotSupportedByMapException;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.NoSpawnAvailableException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.gameobjects.OneTimeTimedEventHandler;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.gameobjects.TriggerArea;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.InventoryIsFullException;
import de.illonis.eduras.inventory.NoSuchItemException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.persistence.MapParser;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.settings.S.SettingType;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.ResourceManager.ResourceType;

/**
 * Server Event Triggerer
 * 
 * @author illonis
 * 
 */
public class ServerEventTriggerer implements EventTriggerer {

	private final static Logger L = EduLog
			.getLoggerFor(ServerEventTriggerer.class.getName());

	private static int lastGameObjectId = 0;

	private final GameLogicInterface logic;
	private final GameInformation gameInfo;
	private final ServerInterface server;

	/**
	 * Initializes a new {@link ServerEventTriggerer}.
	 * 
	 * @param logic
	 *            the logic used.
	 * @param server
	 *            The server that will be used to send events.
	 */
	public ServerEventTriggerer(GameLogicInterface logic, ServerInterface server) {
		this.logic = logic;
		this.server = server;
		this.gameInfo = logic.getGame();
	}

	/**
	 * Returns the game information used.
	 * 
	 * @return the information.
	 * 
	 * @author illonis
	 */
	public GameInformation getGameInfo() {
		return gameInfo;
	}

	@Override
	public int createMissile(ObjectType missileType, int owner,
			Vector2f position, Vector2f speedVector) {

		int missileId = createObjectWithCenterAt(missileType, position, owner);
		Missile o;
		try {
			o = (Missile) gameInfo.findObjectById(missileId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find missile object!", e);
			return -1;
		}

		o.setSpeedVector(speedVector);
		MovementEvent me = new MovementEvent(GameEventNumber.SET_SPEEDVECTOR,
				missileId);
		me.setNewXPos(speedVector.getX());
		me.setNewYPos(speedVector.getY());

		sendEventToAll(me);

		return missileId;
	}

	private void sendEventToAll(Event event) {
		try {
			server.sendEventToAll(event);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.warning("ServerEventTriggerer: " + e.getMessage());
		}
	}

	private void sendEventToClient(Event event, int client) {
		try {
			server.sendEventToClient(event, client);
		} catch (IllegalArgumentException | NoSuchClientException
				| TooFewArgumentsExceptions e) {
			L.warning("ServerEventTriggerer: " + e.getMessage());
		}
	}

	@Override
	public void sendUnit(int objectId, Vector2f target)
			throws ObjectNotFoundException, UnitNotControllableException {
		GameObject gameObject = gameInfo.findObjectById(objectId);
		if (gameObject == null) {
			throw new ObjectNotFoundException(objectId);
		} else if (gameObject instanceof MotionAIControllable) {
			MotionAIControllable movingObject = (MotionAIControllable) gameObject;
			MovingUnitAI ai = (MovingUnitAI) movingObject.getAI();
			ai.moveTo(target);
		} else {
			throw new UnitNotControllableException(objectId);
		}
	}

	@Override
	public void removeObject(int objectId) {
		ObjectFactoryEvent event = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_REMOVE, ObjectType.NO_OBJECT, 0);
		event.setId(objectId);
		logic.getObjectFactory().onObjectFactoryEventAppeared(event);
		sendEvents(event);
	}

	@Override
	public int createObject(ObjectType object, int owner) {
		ObjectFactoryEvent newObjectEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, object, owner);
		newObjectEvent.setId(getNextId());
		logic.getObjectFactory().onObjectFactoryEventAppeared(newObjectEvent);
		GameObject o;
		try {
			o = gameInfo.findObjectById(newObjectEvent.getId());
			if (o instanceof TriggerArea) {
				setTriggerAreaSize(newObjectEvent.getId(), o.getShape()
						.getWidth(), o.getShape().getHeight());
			}
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Cannot find object that was just created!", e);
			return -1;
		}
		return newObjectEvent.getId();
	}

	@Override
	public int createObjectAt(ObjectType object, Vector2f position, int owner) {

		int id = createObject(object, owner);

		GameObject o;
		try {
			o = gameInfo.findObjectById(id);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object that was just created!", e);
			return -1;
		}
		o.setXPosition(position.getX());
		o.setYPosition(position.getY());
		MovementEvent setPos = new MovementEvent(GameEventNumber.SET_POS_TCP,
				id);
		setPos.setNewXPos(position.getX());
		setPos.setNewYPos(position.getY());

		sendEventToAll(setPos);
		return id;
	}

	@Override
	public void setVisibility(int objectId, Visibility newVal) {
		GameObject object;
		try {
			object = gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e) {
			L.warning("This object doesn't exist anymore. You might want to check whether this is okay.");
			return;
		}

		SetVisibilityEvent setVisibleEvent = new SetVisibilityEvent(objectId,
				newVal);
		object.setVisible(newVal);
		sendEventToAll(setVisibleEvent);
	}

	@Override
	public void lootItem(int objectId, int playerId) {

		Item i;
		try {
			i = (Item) gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e2) {
			L.log(Level.WARNING,
					"Cannot find find item that is about to be looted!", e2);
			return;
		}

		SetBooleanGameObjectAttributeEvent bo = new SetBooleanGameObjectAttributeEvent(
				GameEventNumber.SET_COLLIDABLE, objectId, false);

		i.setCollidable(false);

		SetVisibilityEvent bov = new SetVisibilityEvent(objectId,
				Visibility.INVISIBLE);
		i.setVisible(Visibility.INVISIBLE);
		sendEvents(bo, bov);

		if (i instanceof Lootable)
			((Lootable) i).loot();
		try {
			if (i.isUnique()
					&& gameInfo.getPlayerByObjectId(playerId).getPlayer()
							.getInventory().hasItemOfType(i.getType())) {
				Item item;
				try {
					item = gameInfo.getPlayerByObjectId(playerId).getPlayer()
							.getInventory().getItemOfType(i.getType());
				} catch (NoSuchItemException e) {
					// doesn't occur because it was checked before
					item = null;
				}
				if (item instanceof Weapon) {
					Weapon weapon = (Weapon) item;
					weapon.refill();
					SetAmmunitionEvent ammuEvent = new SetAmmunitionEvent(
							weapon.getId(), weapon.getCurrentAmmunition());
					sendEvents(ammuEvent);
				}
				return;
			}
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Could not find looted object.", e);
		}
		int newObjId = createObject(i.getType(), playerId);
		int itemSlot;
		try {
			PlayerMainFigure player = gameInfo.getPlayerByObjectId(playerId);
			Item item = (Item) gameInfo.findObjectById(newObjId);

			itemSlot = player.getPlayer().getInventory().loot(item);
			item.setOwner(player.getOwner());

			item.setCollidable(false);
			item.setVisible(Visibility.INVISIBLE);

			SetVisibilityEvent visEvent = new SetVisibilityEvent(item.getId(),
					Visibility.INVISIBLE);
			SetBooleanGameObjectAttributeEvent colEvent = new SetBooleanGameObjectAttributeEvent(
					GameEventNumber.SET_COLLIDABLE, item.getId(), false);
			SetOwnerEvent soEvent = new SetOwnerEvent(player.getId(),
					item.getId());

			SetItemSlotEvent sis = new SetItemSlotEvent(newObjId,
					player.getOwner(), itemSlot);

			sendEvents(visEvent, colEvent, soEvent, sis);

		} catch (ObjectNotFoundException e1) {
			return;
		} catch (InventoryIsFullException e1) {
			return;
		}

	}

	@Override
	public void maybeSetPositionOfObject(int objectId, Vector2df newPosition) {
		setPositionOfObject(objectId, newPosition, PacketType.UDP);
	}

	@Override
	public void guaranteeSetPositionOfObject(int objectId, Vector2f newPosition) {
		setPositionOfObject(objectId, newPosition, PacketType.TCP);
	}

	private void setPositionOfObject(int objectId, Vector2f newPosition,
			PacketType type) {
		GameEventNumber eventNumber;
		if (type == PacketType.TCP)
			eventNumber = GameEventNumber.SET_POS_TCP;
		else
			eventNumber = GameEventNumber.SET_POS_UDP;

		MovementEvent e = new MovementEvent(eventNumber, objectId);
		e.setNewXPos(newPosition.x);
		e.setNewYPos(newPosition.y);

		GameObject o;
		try {
			o = gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e1) {
			L.warning("Couldn't find object to change position of!");
			return;
		}
		o.setPosition(newPosition);
		sendEvents(e);
	}

	/**
	 * A synchronized wrapper function to receive the next free objectId.
	 * 
	 * @return Returns a free id.
	 */
	private synchronized int getNextId() {
		lastGameObjectId++;
		int nextId = lastGameObjectId;
		return nextId;
	}

	@Override
	public void init() {

		restartRound();

	}

	@Override
	public void setHealth(int id, int newHealth) {
		GameObject object;
		try {
			object = gameInfo.findObjectById(id);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object!", e);
			return;
		}

		if (object instanceof Unit) {
			Unit unit = (Unit) object;

			unit.setHealth(newHealth);

			// announce to network
			SetIntegerGameObjectAttributeEvent event = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SET_HEALTH, id, unit.getHealth());
			sendEvents(event);
		}
	}

	@Override
	public void respawnPlayerAtRandomSpawnpoint(Player player) {
		int idOfRespawnedPlayer = respawnPlayer(player);

		Vector2df spawnPosition = null;
		try {
			spawnPosition = gameInfo.getSpawnPointFor(player);
		} catch (GameModeNotSupportedByMapException e) {
			L.log(Level.SEVERE,
					"Cannot respawn because map doesn't support the game mode.",
					e);
			return;
		} catch (NoSpawnAvailableException e) {
			L.log(Level.SEVERE,
					"Cannot find a spawn for the player. Check the map's .erm file to see if all spawn areas are occupied.",
					e);
			return;
		}

		guaranteeSetPositionOfObject(idOfRespawnedPlayer, spawnPosition);
	}

	/**
	 * Respawns the given player. If the player still has a
	 * {@link PlayerMainFigure} object, it's removed and a new one is created.
	 * When this method returns, the {@link GameMode#onPlayerSpawn(Player)}
	 * -method has already been called.
	 * 
	 * @param player
	 *            player to respawn
	 * @return the object id of the playermainfigure that was respawned.
	 */
	private int respawnPlayer(Player player) {
		if (player.getPlayerMainFigure() != null) {
			removeObject(player.getPlayerMainFigure().getId());
		}

		int playerMainFigureId = createObject(ObjectType.PLAYER,
				player.getPlayerId());
		gameInfo.getGameSettings().getGameMode().onPlayerSpawn(player);
		sendEventToAll(new RespawnEvent(player.getPlayerId()));
		return playerMainFigureId;
	}

	@Override
	public void renamePlayer(int ownerId, String newName) {

		try {
			ClientRenameEvent renameEvent = new ClientRenameEvent(ownerId,
					newName);
			sendEventToAll(renameEvent);
		} catch (InvalidNameException e) {
			L.log(Level.WARNING, "invalid user name", e);
			return;
		}
	}

	@Override
	public void onMatchEnd(int winner) {
		MatchEndEvent matchEndEvent = new MatchEndEvent(winner);
		sendEvents(matchEndEvent);
		restartRound();
	}

	@Override
	public void restartGame() {
		gameInfo.getGameSettings().getGameMode().onGameEnd();
		changeMap(gameInfo.getMap());
		gameInfo.getGameSettings().getGameMode().onGameStart();
	}

	@Override
	public void restartRound() {

		gameInfo.getGameSettings().getGameMode().onRoundEnds();
		for (Player player : gameInfo.getPlayers()) {
			resetStats(player);
		}
		try {
			gameInfo.getTimingSource().clear();
		} catch (NoSuchElementException e) {
			// first start, do nothing
		}
		reloadMap(gameInfo.getMap());
		resetSettings();
		gameInfo.getGameSettings().getGameMode().onRoundStarts();
		sendEvents(new StartRoundEvent());
	}

	@Override
	public void setRemainingTime(long remainingTime) {
		gameInfo.getGameSettings().changeTime(remainingTime);

		SetRemainingTimeEvent setTimeEvent = new SetRemainingTimeEvent(
				remainingTime);

		sendEvents(setTimeEvent);
	}

	@Override
	public void changeGameMode(GameMode newMode) {
		gameInfo.getGameSettings().getGameMode().onGameEnd();

		gameInfo.getGameSettings().changeGameMode(newMode);
		SetGameModeEvent event = new SetGameModeEvent(newMode.getName());

		reloadMap(gameInfo.getMap());
		gameInfo.getGameSettings().getGameMode().onGameStart();

		sendEvents(event);
	}

	@Override
	public void changeMap(Map map) {

		gameInfo.setMap(map);

		// notify client
		SetMapEvent setMapEvent;
		try {
			setMapEvent = new SetMapEvent(map.getName(),
					ResourceManager.getHashOfResource(ResourceType.MAP,
							map.getName() + MapParser.FILE_EXTENSION));
		} catch (IOException e1) {
			L.log(Level.SEVERE, "Cannot calculate Hash of map!", e1);
			setMapEvent = new SetMapEvent(map.getName(), "");
		}
		sendEvents(setMapEvent);

		reloadMap(map);

	}

	private void reloadMap(Map map) {
		removeAllObjects();

		LinkedList<InitialObjectData> portalData = new LinkedList<InitialObjectData>();
		// send objects to client
		for (InitialObjectData initialObject : map.getInitialObjects()) {

			int objectId;

			if (initialObject.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK
					|| initialObject.getType() == ObjectType.MAPBOUNDS) {
				objectId = createDynamicPolygonObjectAt(
						initialObject.getType(),
						initialObject.getPolygonVector2dfs(),
						initialObject.getPosition(), -1);
			} else {
				objectId = createObjectAt(initialObject.getType(),
						initialObject.getPosition(), -1);
			}
			try {
				GameObject o = gameInfo.findObjectById(objectId);
				o.setRefName(initialObject.getRefName());
				if (initialObject.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK) {
					((DynamicPolygonObject) o).setColor(initialObject
							.getColor());
				}
				if (o instanceof TriggerArea && initialObject.getWidth() > 0) {
					setTriggerAreaSize(o.getId(), initialObject.getWidth(),
							initialObject.getHeight());
				}
			} catch (ObjectNotFoundException e) {
				L.log(Level.SEVERE,
						"Cannot find object that was just created from map file!",
						e);
			}
			if (initialObject.getType() == ObjectType.PORTAL) {
				portalData.add(initialObject);
			}
		}

		for (int i = 0; i < portalData.size(); i++) {
			InitialObjectData portal = portalData.get(i);
			Portal portalOne;
			try {
				portalOne = (Portal) gameInfo.findObjectByReference(portal
						.getRefName());
			} catch (ObjectNotFoundException e) {
				L.log(Level.SEVERE, "Could not find recently created portal.",
						e);
				continue;
			}

			Portal portalTwo;
			try {
				portalTwo = (Portal) gameInfo.findObjectByReference(portal
						.getReference(Portal.OTHER_PORTAL_REFERENCE));
			} catch (ObjectNotFoundException e) {
				L.log(Level.SEVERE,
						"Could not find referenced portal of created portal.",
						e);
				continue;
			}
			portalOne.setPartnerPortal(portalTwo);
		}

	}

	private void removeAllObjects() {
		for (GameObject oldObject : gameInfo.getObjects().values()) {
			removeObject(oldObject.getId());
		}
	}

	/**
	 * Removes all non-player objects from gameobject list.
	 * 
	 * @author illonis
	 */
	public void removeAllNonPlayers() {
		for (GameObject oldObject : gameInfo.getObjects().values()) {
			if (!(oldObject instanceof PlayerMainFigure))
				removeObject(oldObject.getId());
		}
	}

	@Override
	public void changeItemSlot(int slot, int player, Item newItem) {
		int objectId;
		if (newItem == null)
			objectId = -1;
		else
			objectId = newItem.getId();
		SetItemSlotEvent e = new SetItemSlotEvent(objectId, player, slot);
		try {
			gameInfo.getPlayerByOwnerId(e.getOwner())
					.getInventory()
					.setItemAt(e.getItemSlot(),
							(Item) gameInfo.getObjects().get(e.getObjectId()));
		} catch (ObjectNotFoundException ex) {
			L.log(Level.SEVERE, "player not found", ex);
		}
		sendEvents(e);
	}

	@Override
	public void remaxHealth(Unit unit) {
		unit.resetHealth();
		setHealth(unit.getId(), unit.getHealth());
	}

	@Override
	public void notifyDeath(Unit unit, int killer) {
		DeathEvent event = new DeathEvent(unit.getId(), killer);

		sendEvents(event);
	}

	/**
	 * Sets all stats of the given player to zero.
	 * 
	 * @param player
	 */
	private void resetStats(Player player) {
		getGameInfo().getGameSettings().getStats().resetStatsFor(player);
	}

	private void resetSettings() {

		gameInfo.getGameSettings().resetRemainingTime();

		SetRemainingTimeEvent setTimeEvent = new SetRemainingTimeEvent(gameInfo
				.getGameSettings().getRemainingTime());

		sendEvents(setTimeEvent);
	}

	/**
	 * Serializes all given events and sends them to output buffer.
	 * 
	 * @param events
	 *            events to send.
	 * 
	 * @author illonis
	 */
	private void sendEvents(GameEvent... events) {
		for (GameEvent gameEvent : events) {
			sendEventToAll(gameEvent);
		}
	}

	@Override
	public int createDynamicPolygonObjectAt(ObjectType type,
			Vector2f[] polygonVector2fs, Vector2f position, int owner) {
		int objId = createObjectAt(type, position, owner);
		setPolygonData(objId, polygonVector2fs);
		return objId;
	}

	@Override
	public void setPolygonData(int objectId, Vector2f[] polygonVertices) {
		GameObject object;
		try {
			object = gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object to set polygon data of!",
					e);
			return;
		}
		if (object instanceof DynamicPolygonObject) {
			DynamicPolygonObject block = (DynamicPolygonObject) object;
			block.setPolygonVertices(polygonVertices);
			SetPolygonDataEvent event = new SetPolygonDataEvent(objectId,
					polygonVertices);
			sendEvents(event);
		}
	}

	@Override
	public void setTeams(Collection<Team> teams) {
		gameInfo.clearTeams();
		SetTeamsEvent event = new SetTeamsEvent();
		for (Team team : teams) {
			gameInfo.addTeam(team);
			event.addTeam(team);
		}
		sendEvents(event);

		for (Team team : teams) {
			for (Player player : team.getPlayers()) {
				sendEvents(new PlayerAndTeamEvent(
						GameEventNumber.ADD_PLAYER_TO_TEAM,
						player.getPlayerId(), team.getTeamId()));
			}
		}
	}

	@Override
	public void addPlayerToTeam(int ownerId, Team team) {

		Player newPlayer;
		try {
			newPlayer = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
			return;
		}

		Team previousTeamOfNewPlayer;
		try {
			previousTeamOfNewPlayer = newPlayer.getTeam();
			previousTeamOfNewPlayer.removePlayer(newPlayer);
		} catch (PlayerHasNoTeamException e) {
			// do nothing here
		}
		team.addPlayer(newPlayer);

		PlayerAndTeamEvent event = new PlayerAndTeamEvent(
				GameEventNumber.ADD_PLAYER_TO_TEAM, ownerId, team.getTeamId());
		sendEvents(event);
	}

	// TODO: This is a dirty hack. Once we decided about whether and how to
	// change the networking, we should consider putting a limit on how many
	// players can join the server into the network part.
	@Override
	public void kickPlayer(int ownerId) {

		removePlayer(ownerId);
		server.kickClient(ownerId);
	}

	@Override
	public void removePlayer(int ownerId) {
		try {
			Player player = gameInfo.getPlayerByOwnerId(ownerId);
			try {
				player.getTeam().removePlayer(player);
			} catch (PlayerHasNoTeamException e) {
				L.log(Level.SEVERE, "Player should have a team at this point!",
						e);
			}

			int objectId = -1;
			PlayerMainFigure mainFigure;
			mainFigure = player.getPlayerMainFigure();
			if (mainFigure != null) {
				ObjectFactoryEvent gonePlayerEvent = new ObjectFactoryEvent(
						GameEventNumber.OBJECT_REMOVE, ObjectType.PLAYER, 0);
				objectId = mainFigure.getId();
				gonePlayerEvent.setId(objectId);
				logic.getObjectFactory().onObjectFactoryEventAppeared(
						gonePlayerEvent);
				sendEvents(gonePlayerEvent);
			}

			gameInfo.removePlayer(ownerId);
			OwnerGameEvent playerLeftEvent = new OwnerGameEvent(
					GameEventNumber.PLAYER_LEFT, ownerId);
			OwnerGameEvent playerLeftEventInfo = new OwnerGameEvent(
					GameEventNumber.INFO_PLAYER_LEFT, ownerId);
			sendEvents(playerLeftEvent, playerLeftEventInfo);
		} catch (ObjectNotFoundException e) {
			// if there is no mainfigure, this function is used to prevent
			// someone to join the server
		}
	}

	@Override
	public void sendRequestedInfos(ArrayList<GameEvent> infos, int owner) {
		try {
			for (GameEvent event : infos) {
				sendEventToClient(event, owner);
			}
		} catch (IllegalArgumentException e) {
			L.log(Level.SEVERE,
					"Something went wrong when sending the requested infos.", e);
		}
	}

	@Override
	public void notifyCooldownStarted(ItemEvent event) {
		sendEventToAll(event);
	}

	@Override
	public void notifyGameObjectStateChanged(
			SetGameObjectAttributeEvent<?> event) {
		sendEventToAll(event);

	}

	@Override
	public void notifyObjectCreated(ObjectFactoryEvent event) {
		sendEventToAll(event);
	}

	@Override
	public void notifyNewObjectPosition(GameObject o) {

		MovementEvent moveEvent;

		moveEvent = new MovementEvent(GameEventNumber.SET_POS_UDP, o.getId());
		moveEvent.setNewXPos(o.getXPosition());
		moveEvent.setNewYPos(o.getYPosition());

		sendEventToAll(moveEvent);
	}

	@Override
	public void changeInteractMode(int ownerId, InteractMode newMode) {
		Player playerToChangeModeOf;
		try {
			playerToChangeModeOf = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Cannot find player!", e);
			return;
		}
		playerToChangeModeOf.setMode(newMode);

		SetInteractModeEvent event = new SetInteractModeEvent(ownerId, newMode);
		sendEvents(event);
	}

	@Override
	public void setRotation(GameObject gameObject) {

		Event setRotationEvent = new SetFloatGameObjectAttributeEvent(
				GameEventNumber.SET_ROTATION, gameObject.getId(),
				gameObject.getRotation()) {
		};
		sendEventToAllExcept(setRotationEvent, gameObject.getOwner());
	}

	// TODO: This might be useful later too, extract it to the eventingserver!
	private void sendEventToAllExcept(Event setRotationEvent, Integer... ids) {
		LinkedList<Integer> clients = server.getClients();

		for (Integer anId : ids) {
			if (clients.contains(anId)) {
				clients.remove(clients.indexOf(anId));
			}
		}

		for (Integer anId : clients) {
			try {
				server.sendEventToClient(setRotationEvent, anId);
			} catch (IllegalArgumentException | NoSuchClientException
					| TooFewArgumentsExceptions e) {
				L.log(Level.SEVERE,
						"An error occured trying to send a setRotate event.", e);
				continue;
			}
		}
	}

	@Override
	public void setCollidability(int objectId, boolean newVal) {
		GameObject object;
		try {
			object = gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object!", e);
			return;
		}

		SetBooleanGameObjectAttributeEvent setCollidableEvent = new SetBooleanGameObjectAttributeEvent(
				GameEventNumber.SET_COLLIDABLE, objectId, newVal);
		object.setCollidable(newVal);
		sendEvents(setCollidableEvent);
	}

	@Override
	public void setStats(StatsProperty property, Player player, int valueToSet) {
		Statistic stats = gameInfo.getGameSettings().getStats();

		synchronized (stats) {
			stats.setStatsProperty(property, player, valueToSet);

			SetStatsEvent setStatsEvent = new SetStatsEvent(property,
					player.getPlayerId(), valueToSet);
			sendEvents(setStatsEvent);
		}
	}

	@Override
	public void changeStatOfPlayerByAmount(StatsProperty prop, Player player,
			int i) {
		Statistic stats = gameInfo.getGameSettings().getStats();

		synchronized (stats) {
			int newVal = stats.getStatsProperty(prop, player) + i;
			stats.setStatsProperty(prop, player, newVal);
			SetStatsEvent setStatsEvent = new SetStatsEvent(prop,
					player.getPlayerId(), newVal);
			sendEvents(setStatsEvent);
		}
	}

	@Override
	public void notifyAreaConquered(NeutralArea neutralArea, Team occupyingTeam) {
		AreaConqueredEvent baseConqueredEvent = new AreaConqueredEvent(
				neutralArea.getId(), occupyingTeam.getTeamId());
		sendEvents(baseConqueredEvent);
	}

	@Override
	public void notifyGameObjectVisibilityChanged(SetVisibilityEvent event) {
		sendEventToAll(event);
	}

	@Override
	public void changeResourcesOfTeamByAmount(Team team, int amount) {
		synchronized (team) {
			int currentCount = team.getResource();
			int newCount = Math.max(0, currentCount + amount);

			team.setResource(newCount);
		}

		SetTeamResourceEvent resourcesEvent = new SetTeamResourceEvent(
				team.getTeamId(), team.getResource());
		sendEvents(resourcesEvent);
	}

	@Override
	public void respawnPlayerAtPosition(Player player, Vector2df position) {
		respawnPlayer(player);

		guaranteeSetPositionOfObject(player.getPlayerMainFigure().getId(),
				position);
	}

	@Override
	public void changeHealthByAmount(Unit unitToHeal, int healAmount) {
		synchronized (unitToHeal) {
			setHealth(unitToHeal.getId(), unitToHeal.getHealth() + healAmount);
		}

	}

	@Override
	public void clearInventoryOfPlayer(Player player) {
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			changeItemSlot(i, player.getPlayerId(), null);
		}
	}

	@Override
	public void onPlayerJoined(Player newPlayer) {
		sendEvents(new OwnerGameEvent(GameEventNumber.PLAYER_JOINED,
				newPlayer.getPlayerId()));
	}

	@Override
	public void notifyPlayerJoined(int ownerId) {
		sendEventToAll(new OwnerGameEvent(GameEventNumber.INFO_PLAYER_JOIN,
				ownerId));
	}

	@Override
	public void notifyPlayerLeft(int ownerId) {
		sendEventToAll(new OwnerGameEvent(GameEventNumber.INFO_PLAYER_LEFT,
				ownerId));
	}

	@Override
	public void notifyGameReady(int clientId) {
		sendEventToClient(new GameReadyEvent(), clientId);
	}

	@Override
	public void loadSettings(File settingsFile) {
		S.loadSettings(settingsFile, SettingType.SERVER);
		try {
			SetSettingsEvent setSettingsEvent = new SetSettingsEvent(
					settingsFile);
			sendEvents(setSettingsEvent);
		} catch (IOException e) {
			L.log(Level.WARNING,
					"IOException occured when trying to load settings", e);
			return;
		}
	}

	@Override
	public void notifyCooldownFinished(int idOfItem) {
		Item item;
		try {
			item = (Item) gameInfo.findObjectById(idOfItem);
		} catch (ObjectNotFoundException e1) {
			L.log(Level.WARNING, "Cannot find item object!", e1);
			return;
		}

		Player player;
		try {
			player = gameInfo.getPlayerByOwnerId(item.getOwner());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Couldn't find player of item with id "
					+ idOfItem, e);
			return;
		}

		int itemSlot;
		try {
			itemSlot = player.getInventory().findItemSlotOfType(item.getType());
		} catch (NoSuchItemException e) {
			// the weapon was lost before the cooldown finished
			return;
		}
		ItemEvent event = new ItemEvent(GameEventNumber.ITEM_CD_FINISHED,
				item.getOwner());
		event.setSlotNum(itemSlot);

		sendEventToClient(event, item.getOwner());
	}

	@Override
	public void giveNewItem(Player player, ObjectType itemType)
			throws WrongObjectTypeException {
		if (!itemType.isItem()) {
			throw new WrongObjectTypeException(itemType);
		}

		int itemId = createObject(itemType, GameObject.OWNER_WORLD);
		lootItem(itemId, player.getPlayerMainFigure().getId());
	}

	@Override
	public int createObjectWithCenterAt(ObjectType objectType,
			Vector2f position, int owner) {
		int objectId = createObjectAt(objectType, position, owner);

		GameObject object;
		try {
			object = gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e) {
			L.severe("Cannot find the object that was just created!");
			return -1;
		}

		object.getShape().setCenterX(position.x);
		object.getShape().setCenterY(position.y);
		guaranteeSetPositionOfObject(objectId, new Vector2df(object.getShape()
				.getX(), object.getShape().getY()));

		return objectId;
	}

	@Override
	public void notifyWeaponAmmoEmpty(int clientId, int slotNum) {
		ItemUseFailedEvent event = new ItemUseFailedEvent(clientId, slotNum,
				Reason.AMMO_EMPTY);
		sendEventToClient(event, clientId);
	}

	@Override
	public int createObjectIn(ObjectType object, Shape shape, int owner) {
		int id = createObjectAt(object,
				new Vector2f(shape.getX(), shape.getY()), owner);

		GameObject o;
		try {
			o = gameInfo.findObjectById(id);
		} catch (ObjectNotFoundException e) {
			L.severe("Cannot find the object that was just created!");
			return -1;
		}
		setVisibility(id, Visibility.INVISIBLE);
		List<GameObject> gameobjects = gameInfo.findObjectsInDistance(
				new Vector2f(shape.getCenterX(), shape.getCenterY()),
				shape.getBoundingCircleRadius() * 2);
		List<GameObject> remove = new LinkedList<GameObject>();

		for (int i = 0; i < gameobjects.size(); i++) {
			GameObject current = gameobjects.get(i);
			if (!GameObject.canCollideWithEachOther(current, o)) {
				remove.add(current);
			}
		}
		for (int i = 0; i < remove.size(); i++) {
			gameobjects.remove(remove.get(i));
		}
		Random r = new Random();
		int maxTries = 10000;
		do {
			float x = r.nextFloat() * shape.getWidth() + shape.getMinX();
			float y = r.nextFloat() * shape.getHeight() + shape.getMinY();
			if (shape.contains(x, y)) {
				o.setPosition(x, y);
			}
			maxTries--;
		} while (maxTries >= 0
				&& !getGameInfo()
						.doesAnyOfOtherObjectsIntersect(o, gameobjects)
						.isEmpty());
		if (maxTries <= 0) {
			ObjectFactoryEvent event = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_REMOVE, null, owner);
			event.setId(id);
			System.out.println("no point found");
			logic.getObjectFactory().onObjectFactoryEventAppeared(event);
			return -1;
		}
		setVisibility(id, Visibility.ALL);
		MovementEvent setPos = new MovementEvent(GameEventNumber.SET_POS_TCP,
				id);
		setPos.setNewXPos(o.getXPosition());
		setPos.setNewYPos(o.getYPosition());
		sendEvents(setPos);
		return id;
	}

	@Override
	public void setSetting(String settingName, String settingValue)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		S.setServerSetting(settingName, settingValue);

		sendEventToAll(new SetSettingPropertyEvent(settingName, settingValue));
	}

	@Override
	public void notififyRespawnTime(long respawnTime) {
		sendEventToAll(new SetTimeEvent(GameEventNumber.SET_RESPAWNTIME,
				respawnTime));
	}

	@Override
	public int createObjectAtBase(ObjectType objectType, Base base, int owner) {
		int objectId = createObject(objectType, owner);

		GameObject object;
		try {
			object = gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e1) {
			L.severe("Can't find the object I just created! ObjectId: "
					+ objectId);
			return -1;
		}

		SpawnPosition spawnPosition = new SpawnPosition(
				(Rectangle) base.getShape(), SpawnPosition.SpawnType.ANY);
		Collection<GameObject> allObjectsExceptBaseAndThisObject = new LinkedList<GameObject>(
				gameInfo.getObjects().values());
		allObjectsExceptBaseAndThisObject.remove(object);
		allObjectsExceptBaseAndThisObject.remove(base);

		try {
			Vector2df positionToSpawnAt = GameInformation
					.findFreePointWithinSpawnPositionForShape(spawnPosition,
							object.getShape(),
							allObjectsExceptBaseAndThisObject,
							GameInformation.ATTEMPT_PER_SPAWNPOINT);
			guaranteeSetPositionOfObject(objectId, positionToSpawnAt);
		} catch (NoSpawnAvailableException e) {
			L.log(Level.SEVERE, "Cannot find a place to spawn this object!", e);
		}
		return objectId;
	}

	@Override
	public int respawnPlayerAtBase(Player player, Base base) {
		// TODO: grmpflll... if we ever switch to Java 8, we should consider
		// doing making the exact method to create an object a parameter, as
		// this method differs to respawnPlayer only in the method used for
		// object creation x)
		if (player.getPlayerMainFigure() != null) {
			removeObject(player.getPlayerMainFigure().getId());
		}

		int playerMainFigureId = createObjectAtBase(ObjectType.PLAYER, base,
				player.getPlayerId());
		gameInfo.getGameSettings().getGameMode().onPlayerSpawn(player);
		sendEventToAll(new RespawnEvent(player.getPlayerId()));
		return playerMainFigureId;
	}

	@Override
	public void changeSpeedBy(MoveableGameObject objectToChangeSpeedOf,
			float amount) {
		synchronized (objectToChangeSpeedOf) {
			setSpeed(objectToChangeSpeedOf, objectToChangeSpeedOf.getSpeed()
					+ amount);
		}

	}

	@Override
	public void setSpeed(MoveableGameObject object, float newValue) {
		synchronized (object) {
			object.setSpeed(Math.max(0f, newValue));
		}

		sendEventToAll(new SetFloatGameObjectAttributeEvent(
				GameEventNumber.SET_SPEED, object.getId(), object.getSpeed()));

	}

	@Override
	public void speedUpObjectForSomeTime(
			final MoveableGameObject objectToSpeedUp,
			final long timeInMiliseconds, final float speedUpValue) {

		final float actualSpeedUp;
		if (objectToSpeedUp.getMaxSpeed() == MoveableGameObject.INFINITE_SPEED) {
			actualSpeedUp = speedUpValue;
		} else {
			actualSpeedUp = Math.min(speedUpValue,
					objectToSpeedUp.getMaxSpeed() - objectToSpeedUp.getSpeed());
		}

		changeSpeedBy(objectToSpeedUp, actualSpeedUp);

		new OneTimeTimedEventHandler(objectToSpeedUp.getTimingSource()) {

			@Override
			public long getInterval() {
				return timeInMiliseconds;
			}

			@Override
			public void intervalElapsed() {
				changeSpeedBy(objectToSpeedUp, actualSpeedUp * -1);
			}
		};

	}

	@Override
	public void makeInvisibleForSomeTime(
			final GameObject objectToMakeInvisible, final long timeInMiliseconds) {
		setVisibility(objectToMakeInvisible.getId(), Visibility.OWNER_TEAM);

		new OneTimeTimedEventHandler(objectToMakeInvisible.getTimingSource()) {

			@Override
			public long getInterval() {
				return timeInMiliseconds;
			}

			@Override
			public void intervalElapsed() {
				setVisibility(objectToMakeInvisible.getId(), Visibility.ALL);
			}
		};
	}

	@Override
	public void setTriggerAreaSize(int objectId, float newWidth, float newHeight)
			throws ObjectNotFoundException {
		GameObject o = gameInfo.findObjectById(objectId);
		if (o instanceof TriggerArea) {
			((Rectangle) o.getShape()).setSize(newWidth, newHeight);
			SetSizeEvent sizeEvent = new SetSizeEvent(objectId, newWidth,
					newHeight);
			sendEventToAll(sizeEvent);
		} else
			throw new ObjectNotFoundException(objectId);
	}

	@Override
	public void guaranteeSetPositionOfObjectAtCenter(int objectId,
			Vector2f newPosition) {
		GameObject object;
		try {
			object = gameInfo.findObjectById(objectId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object", e);
			return;
		}

		object.getShape().setCenterX(newPosition.x);
		object.getShape().setCenterY(newPosition.y);

		guaranteeSetPositionOfObject(objectId, new Vector2df(object.getShape()
				.getX(), object.getShape().getY()));
	}

	@Override
	public void changeBlinkChargesBy(Player player, int charges) {
		synchronized (player) {
			player.setBlinksAvailable(Math.max(player.getBlinksAvailable()
					+ charges, 0));
		}

		sendEvents(new SetAvailableBlinksEvent(player.getPlayerId(),
				player.getBlinksAvailable()));
	}

	@Override
	public void sendResource(GameEventNumber type, int owner, String mapName,
			Path file) {

		try {
			sendEventToClient(new SendResourceEvent(type, mapName, file), owner);
		} catch (IOException e) {
			L.log(Level.SEVERE, "Error sending resource: message", e);
		}

	}

	@Override
	public void notifyAoEDamage(ObjectType type, Vector2f centerPosition) {
		sendEventToAll(new AoEDamageEvent(type, centerPosition));
	}
}
