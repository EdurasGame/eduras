package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

/**
 * Wraps properties of {@link MineWeapon}s missiles.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MineMissile extends AoEMissile {

	/**
	 * Create a new MineMissile.
	 * 
	 * @param game
	 * @param timingSource
	 * @param id
	 */
	public MineMissile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setDamage(S.Server.go_minemissile_damage);
		setDamageRadius(S.Server.go_minemissile_damageradius);
		setObjectType(ObjectType.MINE_MISSILE);
		setSpeed(S.Server.go_minemissile_speed);
		setMaxRange(S.Server.go_minemissile_maxrange);
		setShape(new Circle(S.Server.go_minemissile_shape_size,
				S.Server.go_minemissile_shape_size,
				S.Server.go_minemissile_shape_size));
	}
}
