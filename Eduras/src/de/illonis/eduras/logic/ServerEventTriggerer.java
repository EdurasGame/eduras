package de.illonis.eduras.logic;

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
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.GameModeNotSupportedByMapException;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.InventoryIsFullException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;
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
			Vector2df position, Vector2df speedVector) {

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
	public int createObjectAt(ObjectType object, Vector2df position, int owner) {

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
		sendEvents(setVisibleEvent);
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
					&& gameInfo.getPlayerByObjectId(playerId).getInventory()
							.hasItemOfType(i.getType())) {
				Item item = gameInfo.getPlayerByObjectId(playerId)
						.getInventory().getItemOfType(i.getType());
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

			itemSlot = player.getInventory().loot(item);
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
	public void respawnPlayer(PlayerMainFigure player) {
		// TODO: This should be dependend on game mode:
		for (int i = 0; i < 6; i++)
			changeItemSlot(i, player.getOwner(), null);

		// TODO: Fire a respawn event to client.
		remaxHealth(player);

		Vector2df spawnPosition = null;
		try {
			spawnPosition = gameInfo.getSpawnPointFor(player);
		} catch (GameModeNotSupportedByMapException e) {
			L.log(Level.SEVERE,
					"Cannot respawn because map doesn't support the game mode.",
					e);
		}

		guaranteeSetPositionOfObject(player.getId(), spawnPosition);
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
		restartRound();
	}

	@Override
	public void restartRound() {

		for (PlayerMainFigure player : gameInfo.getPlayers()) {
			resetStats(player);
		}

		changeMap(gameInfo.getMap());

		resetSettings();

	}

	@Override
	public void setRemainingTime(long remainingTime) {
		// TODO Auto-generated method stub
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
		removeAllNonPlayers();

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

		// find bounds map object and fail if you cannot find it
		boolean mapBoundsFound = false;
		for (GameObject o : gameInfo.getObjects().values()) {
			if (o.getType() == ObjectType.MAPBOUNDS) {
				mapBoundsFound = true;
				gameInfo.getMap().setBoundsObject(o);
				setVisibility(o.getId(), Visibility.INVISIBLE);
				break;
			}
		}
		if (!mapBoundsFound) {
			L.severe("Cannot find the map bounds!");
		}

		gameInfo.getGameSettings().getGameMode().onGameStart();

		for (PlayerMainFigure player : gameInfo.getPlayers()) {
			respawnPlayer(player);
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
	public void onDeath(Unit unit, int killer) {
		DeathEvent event = new DeathEvent(unit.getId(), killer);
		GameObject killed = gameInfo.findObjectById(event.getKilled());
		if (killed.isUnit()) {
			Unit un = (Unit) killed;
			gameInfo.getGameSettings().getGameMode()
					.onDeath(un, event.getKillerOwner());
		}

		sendEvents(event);
	}

	/**
	 * Sets all stats of the given player to zero.
	 * 
	 * @param player
	 */
	private void resetStats(PlayerMainFigure player) {
		setStats(StatsProperty.KILLS, player.getOwner(), 0);
		setStats(StatsProperty.DEATHS, player.getOwner(), 0);
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
			Vector2df[] polygonVector2dfs, Vector2df position, int owner) {
		int objId = createObjectAt(type, position, owner);
		setPolygonData(objId, polygonVector2dfs);
	}

	@Override
	public void setPolygonData(int objectId, Vector2df[] polygonVertices) {
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
			for (PlayerMainFigure player : team.getPlayers()) {
				addPlayerToTeam(player.getOwner(), team);
			}
		}
	}

	@Override
	public void addPlayerToTeam(int ownerId, Team team) {

		PlayerMainFigure newPlayer;
		try {
			newPlayer = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
			return;
		}

		if (newPlayer.getTeam() != null)
			newPlayer.getTeam().removePlayer(newPlayer);
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
		int objectId = -1;
		PlayerMainFigure mainFigure;
		ObjectFactoryEvent gonePlayerEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_REMOVE, ObjectType.PLAYER, 0);

		try {
			mainFigure = gameInfo.getPlayerByOwnerId(ownerId);
			objectId = mainFigure.getId();
			gonePlayerEvent.setId(objectId);
			logic.getObjectFactory().onObjectFactoryEventAppeared(
					gonePlayerEvent);
			sendEvents(gonePlayerEvent);
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
			sendEventToClient(new GameReadyEvent(), owner);
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
}
