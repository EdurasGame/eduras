package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

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
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public SplashedMissile(GameInformation game, TimingSource timingSource,
			int id) {
		super(game, timingSource, id);
		setObjectType(ObjectType.MISSILE_SPLASHED);
		setDamage(S.Server.go_splashedmissile_damage);
		setShape(new Circle(S.Server.go_splashedmissile_shape_radius,
				S.Server.go_splashedmissile_shape_radius,
				S.Server.go_splashedmissile_shape_radius));
		setSpeed(S.Server.go_splashedmissile_speed);
		setMaxRange(S.Server.go_splashedmissile_damageradius);
	}
}
