package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ObjectShape.ShapeType;
import de.illonis.eduras.shapes.ShapeFactory;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class RocketMissile extends Missile {

	/**
	 * Creates a new simplemissile
	 * 
	 * @param game
	 *            The game context
	 * @param id
	 *            The id of the new missile
	 */
	public RocketMissile(GameInformation game, int id) {
		super(game, id);
		setDamage(S.go_rocketmissile_damage);
		setDamageRadius(S.go_rocketmissile_damageradius);
		setObjectType(ObjectType.ROCKET_MISSILE);
		setSpeed(S.go_rocketmissile_speed);
		setMaxRange(S.go_rocketmissile_maxrange);
		setShape(ShapeFactory.createShape(ShapeType.ROCKET));
	}

}
