/**
 * 
 */
package de.illonis.eduras.logic;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameMode;
import de.illonis.eduras.Map;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.LootItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MissileLaunchEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.logger.EduLog;
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

	public ServerEventTriggerer(GameLogicInterface logic) {
		this.logic = logic;
		this.gameInfo = logic.getGame();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.logic.EventTriggerer#setPositionOfObject(int,
	 * de.illonis.eduras.math.Vector2D)
	 */
	@Override
	public void setPositionOfObject(int objectId, Vector2D newPosition) {
		// TODO Auto-generated method stub

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

		Vector2D posWeap1 = new Vector2D(map.getWidth() * 0.75,
				map.getHeight() * 0.75);
		this.createObjectAt(ObjectType.ITEM_WEAPON_1, posWeap1, -1);
		Vector2D posWeap2 = new Vector2D(map.getWidth() * 0.25,
				map.getHeight() * 0.25);
		this.createObjectAt(ObjectType.ITEM_WEAPON_1, posWeap2, -1);

		Vector2D posWeap3 = new Vector2D(map.getWidth() * 0.25,
				map.getHeight() * 0.75);
		this.createObjectAt(ObjectType.ITEM_WEAPON_1, posWeap3, -1);

		Vector2D posWeap4 = new Vector2D(map.getWidth() * 0.75,
				map.getHeight() * 0.25);
		this.createObjectAt(ObjectType.ITEM_WEAPON_1, posWeap4, -1);
		this.createObjectAt(ObjectType.BIGBLOCK, new Vector2D(
				map.getWidth() * 0.5, map.getHeight() * 0.5), -1);

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
		// TODO: This is an ugly way of resetting the health and position,
		// change this.
		this.removeObject(player.getId());
		this.createObject(ObjectType.PLAYER, player.getOwner());

		// TODO: It should be the client's part to get the name after a respawn.
		// We have to change this as soon as the gui is told that a player was
		// killed.
		renamePlayer(player.getOwner(), player.getName());
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
		logic.onGameEventAppeared(matchEndEvent);
	}

	@Override
	public void restartRound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRemainingTime(long remainingTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeGameMode(GameMode newMode) {
		// TODO Auto-generated method stub

	}
}
