package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ShapeFactory;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class RocketMissile extends AoEMissile {

	/**
	 * Creates a new simplemissile
	 * 
	 * @param game
	 *            The game context
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id of the new missile
	 */
	public RocketMissile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setDamage(S.Server.go_rocketmissile_damage);
		setDamageRadius(S.Server.go_rocketmissile_damageradius);
		setObjectType(ObjectType.ROCKET_MISSILE);
		setSpeed(S.Server.go_rocketmissile_speed);
		setMaxRange(S.Server.go_rocketmissile_maxrange);
		setShape(ShapeFactory.createShape(ShapeType.ROCKET));
	}

}
