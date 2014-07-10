package de.illonis.eduras.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import de.illonis.eduras.events.AddPlayerToTeamEvent;
import de.illonis.eduras.events.AreaConqueredEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameReadyEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.OwnerGameEvent;
import de.illonis.eduras.events.RespawnEvent;
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
import de.illonis.eduras.events.SetSettingsEvent;
import de.illonis.eduras.events.SetStatsEvent;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.GameModeNotSupportedByMapException;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.NoSpawnAvailableException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.InventoryIsFullException;
import de.illonis.eduras.inventory.NoSuchItemException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.settings.S.SettingType;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

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
	public void createMissile(ObjectType missileType, int owner,
			Vector2f position, Vector2f speedVector) {

		int missileId = createObjectAt(missileType, position, owner);
		Missile o = (Missile) gameInfo.findObjectById(missileId);

		o.setSpeedVector(speedVector);
		MovementEvent me = new MovementEvent(GameEventNumber.SET_SPEEDVECTOR,
				missileId);
		me.setNewXPos(speedVector.getX());
		me.setNewYPos(speedVector.getY());

		sendEvents(me);

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

		return newObjectEvent.getId();
	}

	@Override
	public int createObjectAt(ObjectType object, Vector2f position, int owner) {

		ObjectFactoryEvent newObjectEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, object, owner);
		int id = getNextId();
		newObjectEvent.setId(id);

		logic.getObjectFactory().onObjectFactoryEventAppeared(newObjectEvent);

		GameObject o = gameInfo.findObjectById(id);
		o.setXPosition(position.getX());
		o.setYPosition(position.getY());
		MovementEvent setPos = new MovementEvent(GameEventNumber.SET_POS_TCP,
				id);
		setPos.setNewXPos(position.getX());
		setPos.setNewYPos(position.getY());

		sendEvents(newObjectEvent, setPos);
		return newObjectEvent.getId();

	}

	@Override
	public void setVisibility(int objectId, Visibility newVal) {
		GameObject object = gameInfo.findObjectById(objectId);
		SetVisibilityEvent setVisibleEvent = new SetVisibilityEvent(objectId,
				newVal);
		object.setVisible(newVal);
		sendEventToAll(setVisibleEvent);
	}

	@Override
	public void lootItem(int objectId, int playerId) {

		Item i = (Item) gameInfo.findObjectById(objectId);

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
	public void guaranteeSetPositionOfObject(int objectId, Vector2df newPosition) {
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

		GameObject o = gameInfo.findObjectById(objectId);
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

		changeMap(logic.getGame().getMap());

	}

	@Override
	public void setHealth(int id, int newHealth) {
		GameObject object = gameInfo.findObjectById(id);

		if (object instanceof Unit) {
			Unit unit = (Unit) object;

			unit.setHealth(newHealth);

			// announce to network
			SetIntegerGameObjectAttributeEvent event = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SET_HEALTH, id, newHealth);
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

		sendEventToAll(new RespawnEvent(player.getPlayerId()));
		int playerMainFigureId = createObject(ObjectType.PLAYER,
				player.getPlayerId());
		gameInfo.getGameSettings().getGameMode().onPlayerSpawn(player);
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
	public void onMatchEnd() {
		MatchEndEvent matchEndEvent = new MatchEndEvent(gameInfo
				.getGameSettings().getStats().findPlayerWithMostFrags());

		sendEvents(matchEndEvent);

		gameInfo.getGameSettings().getGameMode().onGameEnd();
		restartRound();
	}

	@Override
	public void restartRound() {

		for (Player player : gameInfo.getPlayers()) {
			resetStats(player);
		}
		changeMap(gameInfo.getMap());
		resetSettings();
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
		gameInfo.getGameSettings().changeGameMode(newMode);
		SetGameModeEvent event = new SetGameModeEvent(newMode.getName());

		restartRound();

		sendEvents(event);
	}

	@Override
	public void changeMap(Map map) {

		gameInfo.setMap(map);
		removeAllObjects();

		// notify client
		SetMapEvent setMapEvent = new SetMapEvent(map.getName());
		sendEvents(setMapEvent);

		// send objects to client
		for (InitialObjectData initialObject : map.getInitialObjects()) {

			if (initialObject.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK
					|| initialObject.getType() == ObjectType.MAPBOUNDS) {
				createDynamicPolygonObjectAt(initialObject.getType(),
						initialObject.getPolygonVector2dfs(),
						initialObject.getPosition(), -1);
			} else {
				createObjectAt(initialObject.getType(),
						initialObject.getPosition(), -1);
			}
		}

		gameInfo.getGameSettings().getGameMode().onGameStart();
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
		setStats(StatsProperty.KILLS, player.getPlayerId(), 0);
		setStats(StatsProperty.DEATHS, player.getPlayerId(), 0);
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
	public void createDynamicPolygonObjectAt(ObjectType type,
			Vector2f[] polygonVector2fs, Vector2f position, int owner) {
		int objId = createObjectAt(type, position, owner);
		setPolygonData(objId, polygonVector2fs);
	}

	@Override
	public void setPolygonData(int objectId, Vector2f[] polygonVertices) {
		GameObject object = gameInfo.findObjectById(objectId);
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
				sendEvents(new AddPlayerToTeamEvent(player.getPlayerId(),
						team.getTeamId()));
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

		if (newPlayer.getTeam() != null) {
			newPlayer.getTeam().removePlayer(newPlayer);
		}
		team.addPlayer(newPlayer);

		AddPlayerToTeamEvent event = new AddPlayerToTeamEvent(ownerId,
				team.getTeamId());
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
			player.getTeam().removePlayer(player);

			int objectId = -1;
			PlayerMainFigure mainFigure;
			ObjectFactoryEvent gonePlayerEvent = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_REMOVE, ObjectType.PLAYER, 0);

			mainFigure = player.getPlayerMainFigure();
			objectId = mainFigure.getId();
			gonePlayerEvent.setId(objectId);
			logic.getObjectFactory().onObjectFactoryEventAppeared(
					gonePlayerEvent);

			OwnerGameEvent playerLeftEvent = new OwnerGameEvent(
					GameEventNumber.PLAYER_LEFT, ownerId);
			OwnerGameEvent playerLeftEventInfo = new OwnerGameEvent(
					GameEventNumber.INFO_PLAYER_LEFT, ownerId);
			sendEvents(gonePlayerEvent, playerLeftEvent, playerLeftEventInfo);
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
		SetInteractModeEvent event = new SetInteractModeEvent(ownerId, newMode);
		sendEventToClient(event, ownerId);
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
		GameObject object = gameInfo.findObjectById(objectId);

		SetBooleanGameObjectAttributeEvent setCollidableEvent = new SetBooleanGameObjectAttributeEvent(
				GameEventNumber.SET_COLLIDABLE, objectId, newVal);
		object.setCollidable(newVal);
		sendEvents(setCollidableEvent);
	}

	@Override
	public void setStats(StatsProperty property, int ownerId, int valueToSet) {
		Statistic stats = gameInfo.getGameSettings().getStats();

		synchronized (stats) {
			stats.setStatsProperty(property, ownerId, valueToSet);

			SetStatsEvent setStatsEvent = new SetStatsEvent(property, ownerId,
					valueToSet);
			sendEvents(setStatsEvent);
		}
	}

	@Override
	public void changeStatOfPlayerByAmount(StatsProperty prop,
			PlayerMainFigure player, int i) {
		Statistic stats = gameInfo.getGameSettings().getStats();

		synchronized (stats) {
			int newVal = stats.getStatsProperty(prop, player.getOwner()) + i;
			stats.setStatsProperty(prop, player.getOwner(), newVal);
			SetStatsEvent setStatsEvent = new SetStatsEvent(prop,
					player.getOwner(), newVal);
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
			setHealth(
					unitToHeal.getId(),
					Math.max(unitToHeal.getMaxHealth(), unitToHeal.getHealth()
							+ healAmount));
		}

	}

	@Override
	public void clearInventoryOfPlayer(Player player) {
		for (int i = 0; i < 6; i++) {
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
		Item item = (Item) gameInfo.findObjectById(idOfItem);

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

}
