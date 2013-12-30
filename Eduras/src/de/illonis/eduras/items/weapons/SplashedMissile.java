package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * A missile that is spawned by {@link SplashMissile} when that collides.
 * 
 * @author illonis
 * 
 */
public class SplashedMissile extends Missile {

	/**
	 * Creates a new splashedmissile.
	 * 
	 * @param game
	 *            game infos.
	 * @param id
	 *            object id.
	 */
	public SplashedMissile(GameInformation game, int id) {
		super(game, id);
		setObjectType(ObjectType.MISSILE_SPLASHED);
		setDamage(S.go_splashedmissile_damage);
		setDamageRadius(S.go_splashedmissile_damageradius);
		setShape(new Circle(S.go_splashedmissile_shape_radius));
		setSpeed(S.go_splashedmissile_speed);
	}

}
