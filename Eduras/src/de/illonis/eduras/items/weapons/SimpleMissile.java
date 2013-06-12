package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.units.Unit;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SimpleMissile extends Missile {

	/**
	 * Creates a new simplemissile
	 * 
	 * @param game
	 *            The game context
	 * @param id
	 *            The id of the new missile
	 */
	public SimpleMissile(GameInformation game, int id) {
		super(game, id);
		setDamage(5);
		setDamageRadius(5);
		setObjectType(ObjectType.SIMPLEMISSILE);
		setSpeed(100);
		setMaxRange(200);
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
