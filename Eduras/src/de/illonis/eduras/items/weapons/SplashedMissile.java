package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.units.Unit;

public class SplashedMissile extends Missile {

	public SplashedMissile(GameInformation game, int id) {
		super(game, id);
		setObjectType(ObjectType.MISSILE_SPLASHED);
		setDamage(3);
		setDamageRadius(1);
		setShape(new Circle(1));
		setSpeed(250);
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
