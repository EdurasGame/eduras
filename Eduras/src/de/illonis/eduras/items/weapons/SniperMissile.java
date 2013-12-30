/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * 
 * @author Jan Reese
 * 
 */
public class SniperMissile extends Missile {
	/**
	 * creates a new SniperMissle
	 * 
	 * @param game
	 *            game context
	 * @param id
	 *            id of the new missile
	 */
	public SniperMissile(GameInformation game, int id) {
		super(game, id);
		setDamage(S.go_snipermissile_damage);
		setDamageRadius(S.go_snipermissile_damageradius);
		setObjectType(ObjectType.SNIPERMISSILE);
		setShape(new Circle(S.go_snipermissile_shape_radius));
		setSpeed(S.go_snipermissile_speed);
	}

}
