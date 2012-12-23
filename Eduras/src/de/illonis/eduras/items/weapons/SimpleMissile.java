/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;

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
		setSpeed(5);
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
		getGame().getEventTriggerer().removeObject(getId());
	}

}
