package de.illonis.eduras.logic;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.AddPlayerToTeamEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
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
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.exceptions.GameModeNotSupportedByMapException;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.DynamicPolygonBlock;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.InventoryIsFullException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.networking.Buffer;
import de.illonis.eduras.networking.NetworkMessageSerializer;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Server Event Triggerer
 * 
 * @author illonis
 * 
 */
public class ServerEventTriggerer implements EventTriggerer {

	private static int lastGameObjectId = 0;

	private final GameLogicInterface logic;
	private GameInformation gameInfo;

	private Buffer outputBuffer;

	/**
	 * Initializes a new {@link ServerEventTriggerer}.
	 * 
	 * @param logic
	 *            the logic used.
	 */
	public ServerEventTriggerer(GameLogicInterface logic) {
		this.logic = logic;
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
			Vector2D position, Vector2D speedVector) {

		int missileId = createObjectAt(missileType, position, owner);
		Missile o = (Missile) gameInfo.findObjectById(missileId);

		o.setSpeedVector(speedVector);
		MovementEvent me = new MovementEvent(GameEventNumber.SETSPEEDVECTOR,
				missileId);
		me.setNewXPos(speedVector.getX());
		me.setNewYPos(speedVector.getY());

		sendEvents(me);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.logic.EventTriggerer#removeObject(int)
	 */
	@Override
	public void removeObject(int objectId) {
		ObjectFactoryEvent event = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_REMOVE, ObjectType.NO_OBJECT);
		event.setId(objectId);
		logic.getObjectFactory().onObjectFactoryEventAppeared(event);
	}

	@Override
	public int createObject(ObjectType object, int owner) {
		ObjectFactoryEvent newObjectEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, object);
		newObjectEvent.setId(getNextId());
		newObjectEvent.setOwner(owner);
		logic.getObjectFactory().onObjectFactoryEventAppeared(newObjectEvent);

		return newObjectEvent.getId();
	}

	@Override
	public int createObjectAt(ObjectType object, Vector2D position, int owner) {

		ObjectFactoryEvent newObjectEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, object);
		int id = getNextId();
		newObjectEvent.setId(id);
		newObjectEvent.setOwner(owner);

		logic.getObjectFactory().onObjectFactoryEventAppeared(newObjectEvent);

		GameObject o = gameInfo.findObjectById(id);
		o.setXPosition(position.getX());
		o.setYPosition(position.getY());
		MovementEvent setPos = new MovementEvent(GameEventNumber.SET_POS, id);
		setPos.setNewXPos(position.getX());
		setPos.setNewYPos(position.getY());

		sendEvents(newObjectEvent, setPos);
		return newObjectEvent.getId();

	}

	@Override
	public void lootItem(int objectId, int playerId) {

		Item i = (Item) gameInfo.findObjectById(objectId);

		SetBooleanGameObjectAttributeEvent bo = new SetBooleanGameObjectAttributeEvent(
				GameEventNumber.SET_COLLIDABLE, objectId, false);

		i.setCollidable(false);

		SetBooleanGameObjectAttributeEvent bov = new SetBooleanGameObjectAttributeEvent(
				GameEventNumber.SET_VISIBLE, objectId, false);
		i.setVisible(false);
		sendEvents(bo, bov);

		if (i instanceof Lootable)
			((Lootable) i).loot();
		try {
			if (i.isUnique()
					&& gameInfo.getPlayerByObjectId(playerId).getInventory()
							.hasItemOfType(i.getType())) {
				return;
			}
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		int newObjId = createObject(i.getType(), playerId);
		int itemSlot;
		try {
			PlayerMainFigure player = gameInfo.getPlayerByObjectId(playerId);
			Item item = (Item) gameInfo.findObjectById(newObjId);

			itemSlot = player.getInventory().loot(item);
			item.setOwner(player.getOwner());

			item.setCollidable(false);
			item.setVisible(false);

			SetBooleanGameObjectAttributeEvent visEvent = new SetBooleanGameObjectAttributeEvent(
					GameEventNumber.SET_VISIBLE, item.getId(), false);
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
	public void setPositionOfObject(int objectId, Vector2D newPosition) {
		MovementEvent e = new MovementEvent(GameEventNumber.SET_POS, objectId);
		e.setNewXPos(newPosition.getX());
		e.setNewYPos(newPosition.getY());

		GameObject o = gameInfo.findObjectById(objectId);
		o.setXPosition(newPosition.getX());
		o.setYPosition(newPosition.getY());
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

	/**
	 * Sets the outputbuffer to write the messages to.
	 * 
	 * @param buf
	 *            The outputbuffer to set.
	 */
	public void setOutputBuffer(Buffer buf) {
		outputBuffer = buf;
	}

	@Override
	public void setHealth(int id, int newHealth) {
		GameObject object = gameInfo.findObjectById(id);

		if (object instanceof Unit) {
			Unit unit = (Unit) object;

			unit.setHealth(newHealth);

			// announce to network
			SetIntegerGameObjectAttributeEvent event = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SETHEALTH, id, newHealth);
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

		Vector2D spawnPosition = null;
		try {
			spawnPosition = gameInfo.getSpawnPointFor(player);
		} catch (GameModeNotSupportedByMapException e) {
			e.printStackTrace();
		}

		setPositionOfObject(player.getId(), spawnPosition);
	}

	@Override
	public void renamePlayer(int ownerId, String newName) {

		try {
			ClientRenameEvent renameEvent = new ClientRenameEvent(ownerId,
					newName);
			// TODO: check if this call causes recursion due to repeated sending
			// by logic.
			logic.onGameEventAppeared(renameEvent);
			sendEvents(renameEvent);
		} catch (InvalidNameException e) {
			EduLog.passException(e);
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

		sendEvents(event);
	}

	@Override
	public void changeMap(Map map) {

		gameInfo.setMap(map);

		removeAllNonPlayers();

		for (InitialObjectData initialObject : map.getInitialObjects()) {
			createObjectAt(initialObject.getType(),
					initialObject.getPosition(), -1);
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
			EduLog.passException(ex);
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
		int playerId = player.getOwner();

		gameInfo.getGameSettings().getStats().setDeaths(playerId, 0);
		gameInfo.getGameSettings().getStats().setKills(playerId, 0);

		SetIntegerGameObjectAttributeEvent setdeaths = new SetIntegerGameObjectAttributeEvent(
				GameEventNumber.SET_DEATHS, playerId, 0);
		SetIntegerGameObjectAttributeEvent setkills = new SetIntegerGameObjectAttributeEvent(
				GameEventNumber.SET_KILLS, playerId, 0);

		sendEvents(setdeaths, setkills);
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
		String estr = "";
		for (GameEvent gameEvent : events) {
			try {
				estr += NetworkMessageSerializer.serialize(gameEvent);
			} catch (MessageNotSupportedException e) {
				EduLog.passException(e);
			}
		}
		outputBuffer.append(estr);
	}

	@Override
	public void createDynamicPolygonAt(Vector2D[] polygonVertices,
			Vector2D position, int owner) {
		int objId = createObjectAt(ObjectType.DYNAMIC_POLYGON, position, owner);
		setPolygonData(objId, polygonVertices);
	}

	@Override
	public void setPolygonData(int objectId, Vector2D[] polygonVertices) {
		GameObject object = gameInfo.findObjectById(objectId);
		if (object instanceof DynamicPolygonBlock) {
			DynamicPolygonBlock block = (DynamicPolygonBlock) object;
			block.setPolygonVertices(polygonVertices);
			SetPolygonDataEvent event = new SetPolygonDataEvent(objectId,
					polygonVertices);
			sendEvents(event);
		}
	}

	@Override
	public void setTeams(Team... teams) {
		gameInfo.clearTeams();
		SetTeamsEvent event = new SetTeamsEvent();
		for (Team team : teams) {
			gameInfo.addTeam(team);
			event.addTeam(team.getColor(), team.getName());
		}
		sendEvents(event);
	}

	@Override
	public void addPlayerToTeam(int ownerId, Team team) {

		PlayerMainFigure newPlayer;
		try {
			newPlayer = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
			return;
		}

		if (newPlayer.getTeam() != null)
			newPlayer.getTeam().removePlayer(newPlayer);
		team.addPlayer(newPlayer);

		AddPlayerToTeamEvent event = new AddPlayerToTeamEvent(ownerId,
				team.getColor());
		sendEvents(event);
	}

}
