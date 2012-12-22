/**
 * 
 */
package de.illonis.eduras.logic;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.LootItemEvent;
import de.illonis.eduras.events.MissileLaunchEvent;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.math.Vector2D;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerEventTriggerer implements EventTriggerer {

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
				position, speedVector, owner);

		logic.onGameEventAppeared(event);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.logic.EventTriggerer#removeObject(int)
	 */
	@Override
	public void removeObject(int objectId) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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

}
