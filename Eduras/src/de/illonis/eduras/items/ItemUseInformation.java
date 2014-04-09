/**
 * 
 */
package de.illonis.eduras.items;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameobjects.GameObject;

/**
 * A wrapper class for information needed to process an item use.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */

public class ItemUseInformation {
	private final GameObject triggeringObject;
	private final Vector2f target;

	/**
	 * Create new ItemUseInformation containing the given information.
	 * 
	 * @param triggeringObject
	 *            The object that triggered the item use.
	 * @param target
	 *            The target where to use the item.
	 */
	public ItemUseInformation(GameObject triggeringObject, Vector2f target) {
		this.triggeringObject = triggeringObject;
		this.target = target;
	}

	/**
	 * Returns the object that triggered the itemuse.
	 * 
	 * @return The triggering object.
	 */
	public GameObject getTriggeringObject() {
		return triggeringObject;
	}

	/**
	 * Returns where to use the item.
	 * 
	 * @return The target.
	 */
	public Vector2f getTarget() {
		return target;
	}

}
