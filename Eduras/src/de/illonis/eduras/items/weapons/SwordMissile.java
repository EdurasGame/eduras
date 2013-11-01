package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.shapes.Sword;
import de.illonis.eduras.units.Unit;

public class SwordMissile extends Missile {
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
