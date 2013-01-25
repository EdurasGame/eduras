/**
 * 
 */
package de.illonis.eduras.events;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.math.Vector2D;

/**
 * Triggered when a missile is launched.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MissileLaunchEvent extends OwnerGameEvent {

	private final Vector2D position;
	private final Vector2D speedVector;
	private final int id;
	private final ObjectType objectType;

	/**
	 * Creates a MissileLaunchEvent that launches a missile of the given type at
	 * the given position with the given speedvector that belongs to the given
	 * owner.
	 * 
	 * @param objectType
	 *            The objectType
	 * @param position
	 *            The position
	 * @param speedVector
	 *            The speedvector
	 * @param owner
	 *            the id of the owner
	 */
	public MissileLaunchEvent(ObjectType objectType, Vector2D position,
			Vector2D speedVector, int owner, int id) {
		super(GameEventNumber.MISSILE_LAUNCH, owner);
		this.id = id;
		this.position = position;
		this.speedVector = speedVector;
		this.objectType = objectType;

	}

	/**
	 * Returns spawning position
	 * 
	 * @return the position
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Returns speed vector of spawned missile.
	 * 
	 * @return the speedVector
	 */
	public Vector2D getSpeedVector() {
		return speedVector;
	}

	/**
	 * Returns object type of missile.
	 * 
	 * @return the objectType
	 */
	public ObjectType getObjectType() {
		return objectType;
	}

	/**
	 * Returns the id of the missile.
	 * 
	 * @return missile id.
	 */
	public int getId() {
		return id;
	}

}
