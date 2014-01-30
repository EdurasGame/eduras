package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * The missile for the corresponding {@link AssaultRifle}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class AssaultMissile extends Missile {

	/**
	 * Create an assault missile.
	 * 
	 * @param game
	 * @param id
	 */
	public AssaultMissile(GameInformation game, int id) {
		super(game, id);
		setDamage(S.go_assaultmissile_damage);
		setObjectType(ObjectType.ASSAULT_MISSILE);
		setDamageRadius(S.go_assaultmissile_damageradius);
		setSpeed(S.go_assaultmissile_speed);
		setMaxRange(S.go_assaultmissile_maxrange);
		setShape(new Circle(S.go_assaultmissile_shape_size));
	}
}
