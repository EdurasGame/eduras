package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.shapes.Sword;
import de.illonis.eduras.units.Unit;

/**
 * This is the missile fired by {@link SwordWeapon}. It has a fancy sword-like
 * shape.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SwordMissile extends Missile {
	/**
	 * Create a new missile.
	 * 
	 * @param game
	 *            The game info context.
	 * @param id
	 *            The id the missile is assigned to.
	 */
	public SwordMissile(GameInformation game, int id) {
		super(game, id);
		setDamage(2);
		setDamageRadius(1);
		setObjectType(ObjectType.SWORDMISSILE);
		setShape(new Sword());
		setSpeed(100);
		setMaxRange(10);

	}

	@Override
	public void onCollision(GameObject collidingObject) {
		if (collidingObject.isUnit()) {
			((Unit) collidingObject).damagedBy(getDamage(), getOwner());
		}
		removeSelf();
	}

	@Override
	public void onMapBoundsReached() {
		removeSelf();
	}
}
