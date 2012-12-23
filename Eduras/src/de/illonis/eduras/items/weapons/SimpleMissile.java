/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.ObjectFactory.ObjectType;
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
	 */
	public SimpleMissile(GameInformation game) {
		super(game);
		setDamage(5);
		setDamageRadius(5);
		setObjectType(ObjectType.SIMPLEMISSILE);
		setSpeed(30);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.items.weapons.Missile#spawn()
	 */
	@Override
	public Missile spawn() {
		return new SimpleMissile(getGame());
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		if (collidingObject.isUnit()) {
			((Unit) collidingObject).damage(getDamage());
		}
		getGame().getEventTriggerer().removeObject(getId());
	}

}
