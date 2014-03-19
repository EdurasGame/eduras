package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;
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
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id the missile is assigned to.
	 */
	public SwordMissile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setDamage(S.go_swordmissile_damage);
		setDamageRadius(S.go_swordmissile_damageradius);
		setObjectType(ObjectType.SWORDMISSILE);
		setShape(new Sword());
		setSpeed(S.go_swordmissile_speed);
		setMaxRange(S.go_swordmissile_maxrange);
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
