package de.illonis.eduras.logic;

import java.awt.Rectangle;
import java.util.Random;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.LootItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MissileLaunchEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.networking.Buffer;
import de.illonis.eduras.networking.NetworkMessageSerializer;
import de.illonis.eduras.units.Player;
import de.illonis.eduras.units.Unit;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.logic.EventTriggerer#createMissile(de.illonis.eduras
	 * .ObjectFactory.ObjectType, int, de.illonis.eduras.math.Vector2D,
	 * de.illonis.eduras.math.Vector2D)
	 */
	@Override
	public void createMissile(ObjectType missileType, int owner,
			Vector2D position, Vector2D speedVector) {

		MissileLaunchEvent event = new MissileLaunchEvent(missileType,
				position, speedVector, owner, getNextId());

		logic.onGameEventAppeared(event);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.logic.EventTriggerer#createObjectAt(de.illonis.eduras
	 * .ObjectFactory.ObjectType, de.illonis.eduras.math.Vector2D, int)
	 */
	@Override
	public void createObjectAt(ObjectType object, Vector2D position, int owner) {

		ObjectFactoryEvent newObjectEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, object);
		int id = getNextId();
		newObjectEvent.setId(id);
		newObjectEvent.setOwner(owner);
		logic.onGameEventAppeared(newObjectEvent);

		MovementEvent setPos = new MovementEvent(GameEventNumber.SET_POS, id);
		setPos.setNewXPos(position.getX());
		setPos.setNewYPos(position.getY());
		logic.onGameEventAppeared(setPos);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.logic.EventTriggerer#lootItem(int, int)
	 */
	@Override
	public void lootItem(int objectId, int playerId) {

		LootItemEvent lootEvent = new LootItemEvent(objectId, playerId);

		logic.onGameEventAppeared(lootEvent);

	}

	@Override
	public void setPositionOfObject(int objectId, Vector2D newPosition) {
		MovementEvent e = new MovementEvent(GameEventNumber.SET_POS, objectId);
		e.setNewXPos(newPosition.getX());
		e.setNewYPos(newPosition.getY());
		logic.onGameEventAppeared(e);
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

		Map map = logic.getGame().getMap();

		for (GameObject singleObject : map.getInitialObjects()) {
			this.createObjectAt(singleObject.getType(),
					singleObject.getPositionVector(), singleObject.getOwner());
		}

	}

	@Override
	public void createObject(ObjectType object, int owner) {
		ObjectFactoryEvent newObjectEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, object);
		newObjectEvent.setId(getNextId());
		newObjectEvent.setOwner(owner);
		logic.onGameEventAppeared(newObjectEvent);
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
			try {
				String eventString = NetworkMessageSerializer.serialize(event);
				outputBuffer.append(eventString);
			} catch (MessageNotSupportedException e) {
				EduLog.passException(e);
				return;
			}
		}
	}

	@Override
	public void respawnPlayer(Player player) {
		// TODO: This should be dependend on game mode:
		for (int i = 0; i < 6; i++)
			changeItemSlot(i, player.getOwner(), null);

		// TODO: Fire a respawn event to client.
		remaxHealth(player);

		Random r = new Random();
		Rectangle m = gameInfo.getMap().getBounds();
		int x = r.nextInt(m.width);
		int y = r.nextInt(m.height);
		setPositionOfObject(player.getId(), new Vector2D(x, y));
	}

	@Override
	public void renamePlayer(int ownerId, String newName) {

		try {
			ClientRenameEvent renameEvent = new ClientRenameEvent(ownerId,
					newName);
			logic.onGameEventAppeared(renameEvent);
			outputBuffer
					.append(NetworkMessageSerializer.serialize(renameEvent));
		} catch (InvalidNameException e) {
			EduLog.passException(e);
			return;
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}

	}

	@Override
	public void onMatchEnd() {
		MatchEndEvent matchEndEvent = new MatchEndEvent(gameInfo
				.getGameSettings().getStats().findPlayerWithMostFrags());

		try {
			outputBuffer.append(NetworkMessageSerializer
					.serialize(matchEndEvent));
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void changeGameMode(GameMode newMode) {
		gameInfo.getGameSettings().changeGameMode(newMode);
		SetGameModeEvent event = new SetGameModeEvent(newMode.getName());
		try {
			String eventString = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(eventString);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
	}

	@Override
	public void changeMap(Map map) {

		gameInfo.setMap(map);

		removeAllNonPlayers();

		for (GameObject initialObject : map.getInitialObjects()) {
			createObjectAt(initialObject.getType(),
					initialObject.getPositionVector(), initialObject.getOwner());
		}

		for (Player player : gameInfo.getPlayers()) {
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
			if (!(oldObject instanceof Player))
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
		logic.onGameEventAppeared(e);
		try {
			outputBuffer.append(NetworkMessageSerializer.serialize(e));
		} catch (MessageNotSupportedException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void remaxHealth(Unit unit) {
		unit.resetHealth();
		setHealth(unit.getId(), unit.getHealth());
	}

	@Override
	public void onDeath(Unit unit, int killer) {
		DeathEvent event = new DeathEvent(unit.getId(), killer);
		gameInfo.getGameSettings().getGameMode().onDeath(unit, killer);
		logic.onGameEventAppeared(event);

	}

	/**
	 * Sets all stats of the given player to zero.
	 * 
	 * @param player
	 */
	private void resetStats(Player player) {
		int playerId = player.getOwner();

		SetIntegerGameObjectAttributeEvent setdeaths = new SetIntegerGameObjectAttributeEvent(
				GameEventNumber.SET_DEATHS, playerId, 0);
		SetIntegerGameObjectAttributeEvent setkills = new SetIntegerGameObjectAttributeEvent(
				GameEventNumber.SET_KILLS, playerId, 0);

		try {
			outputBuffer.append(NetworkMessageSerializer.serialize(setdeaths));
			outputBuffer.append(NetworkMessageSerializer.serialize(setkills));
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}

		logic.onGameEventAppeared(setkills);
		logic.onGameEventAppeared(setdeaths);
	}

	private void resetSettings() {

		gameInfo.getGameSettings().resetRemainingTime();

		SetRemainingTimeEvent setTimeEvent = new SetRemainingTimeEvent(gameInfo
				.getGameSettings().getRemainingTime());

		try {
			outputBuffer.append(NetworkMessageSerializer
					.serialize(setTimeEvent));
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		}
	}
}
