package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

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
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            the new object id.
	 */
	public AssaultMissile(GameInformation game, TimingSource timingSource,
			int id) {
		super(game, timingSource, id);
		setDamage(S.Server.go_assaultmissile_damage);
		setObjectType(ObjectType.ASSAULT_MISSILE);
		setSpeed(S.Server.go_assaultmissile_speed);
		setMaxRange(S.Server.go_assaultmissile_maxrange);
		// setShape(new Circle(S.go_assaultmissile_shape_size));
	}

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		if ((otherObject.getType() == ObjectType.MISSILE_SPLASH && otherObject
				.getOwner() == getOwner())
				|| otherObject.getType() == ObjectType.MINE_MISSILE) {
			return true;
		} else {
			return super.isCollidableWith(otherObject);
		}
	}
}
