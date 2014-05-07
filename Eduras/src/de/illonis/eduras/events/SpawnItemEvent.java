package de.illonis.eduras.events;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.actions.SpawnItemAction;

/**
 * Respective event for {@link SpawnItemAction}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SpawnItemEvent extends RTSActionEvent {

	private final static Logger L = EduLog.getLoggerFor(SpawnItemEvent.class
			.getName());

	private ObjectType objectType;
	private Vector2f position;

	/**
	 * Create the event.
	 * 
	 * @param executingPlayer
	 *            id of player who wants to perform the action.
	 * @param type
	 *            Type of item to location to spawn the player at.spawn.
	 * @param position
	 */
	public SpawnItemEvent(int executingPlayer, ObjectType type,
			Vector2f position) {
		super(GameEventNumber.SPAWN_ITEM, executingPlayer);

		putArgument(type.getNumber());
		putArgument(position.x);
		putArgument(position.y);

		this.objectType = type;
		this.position = position;
	}

	/**
	 * Get object type of item to spawn.
	 * 
	 * @return itemtype to spawn
	 */
	public ObjectType getObjectType() {
		return objectType;
	}

	/**
	 * Returns the location to spawn the item at.
	 * 
	 * @return Location
	 */
	public Vector2f getPosition() {
		return position;
	}
}
