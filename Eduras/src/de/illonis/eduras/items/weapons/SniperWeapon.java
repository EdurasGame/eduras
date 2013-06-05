/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;

/**
 * 
 * @author Jan Reese
 * 
 */
public class SniperWeapon extends Weapon {
	/**
	 * 
	 * @param gi
	 *            game info
	 * @param id
	 *            weapon id
	 */
	public SniperWeapon(GameInformation gi, int id) {

		super(ObjectType.ITEM_WEAPON_SNIPER, gi, id);
		setMissile(null);
		setName("Sniper");
		setShape(new Circle(5));
		defaultCooldown = 1050;
	}

	@Override
	public void use(ItemUseInformation info) {
		// (jme) Spawn position will be calculated in a simplified way. We use
		// diagonal's length of shooting player to move missile away from him.

		if (!hasCooldown()) {
			super.use(info);
			Vector2D target = info.getTarget();
			GameObject triggeringObject = info.getTriggeringObject();

			Vector2D position = triggeringObject.getPositionVector();

			Vector2D speedVector = new Vector2D(target);
			speedVector.subtract(position);

			Vector2D diag = new Vector2D(triggeringObject.getBoundingBox()
					.getWidth(), triggeringObject.getBoundingBox().getHeight());
			Vector2D copy = speedVector.copy();
			copy.setLength(diag.getLength());
			position.add(copy);

			getGame().getEventTriggerer()
					.createMissile(ObjectType.SNIPERMISSILE, getOwner(),
							position, speedVector);
		}
	}

}
