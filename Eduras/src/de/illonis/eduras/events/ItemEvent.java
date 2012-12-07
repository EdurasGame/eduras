/**
 * 
 */
package de.illonis.eduras.events;

import de.illonis.eduras.math.Vector2D;

/**
 * Used as a wrapper for item events like use_item_pressed and _released. An
 * item event is related to a specific item (identified by its objectid) which
 * belongs to a specific owner. In some cases, for example item_use, you need
 * some additional info, e.g. the target where to use an item. Thats why this
 * class contains a target argument which you have to set in some case.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ItemEvent extends OwnerGameEvent {

	private int objectId = -1;
	private Vector2D target;

	/**
	 * Create a new ItemEvent of the given type.
	 * 
	 * @param eventType
	 *            The type.
	 * @param ownerId
	 *            The id of the owner this item belongs to.
	 */
	public ItemEvent(GameEventNumber eventType, int ownerId) {

		super(eventType, ownerId);

		target = new Vector2D();

	}

	/**
	 * Set the objectid of the item which is related to this event.
	 * 
	 * @param id
	 *            The item's id.
	 */
	public void setObjectId(int id) {
		objectId = id;
	}

	/**
	 * Returns the objectid of the item.
	 * 
	 * @return The item's objectid.
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * Set the target's X position of the item use.
	 * 
	 * @param targetX
	 *            The x-component of the target.
	 */
	public void setTargetX(double targetX) {
		this.target.setX(targetX);
	}

	/**
	 * Set the target's Y position of the item use.
	 * 
	 * @param targetY
	 *            The y-component of the target.
	 */
	public void setTargetY(double targetY) {
		this.target.setY(targetY);
	}

	/**
	 * Set the target position of the item use.
	 * 
	 * @param target
	 *            The target position.
	 */
	public void setTarget(Vector2D target) {
		this.target = target;
	}

	public Vector2D getTarget() {
		return target;
	}
}
