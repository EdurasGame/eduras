/**
 * 
 */
package de.illonis.eduras.logic;

import de.illonis.eduras.Map;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.LootItemEvent;
import de.illonis.eduras.events.MissileLaunchEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.math.Vector2D;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerEventTriggerer implements EventTriggerer {

	private static int lastGameObjectId = 0;

	private final GameLogicInterface logic;

	public ServerEventTriggerer(GameLogicInterface logic) {
		this.logic = logic;
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

	}

	@Override
	public void createObject(ObjectType object, int owner) {
		ObjectFactoryEvent newObjectEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, object);
		newObjectEvent.setId(getNextId());
		newObjectEvent.setOwner(owner);
		logic.onGameEventAppeared(newObjectEvent);
	}
}
