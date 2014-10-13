package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

/**
 * A missile that explodes on collision and spawns several
 * {@link SplashedMissile}s.
 * 
 * @author illonis
 * 
 */
public class SplashMissile extends Missile {

	/**
	 * Creates a new splashmissile.
	 * 
	 * @param game
	 *            game infos.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public SplashMissile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setObjectType(ObjectType.MISSILE_SPLASH);
		setDamage(S.Server.go_splashmissile_damage);
		setShape(new Circle(S.Server.go_splashmissile_shape_radius,
				S.Server.go_splashmissile_shape_radius,
				S.Server.go_splashmissile_shape_radius));
		setSpeed(S.Server.go_splashmissile_speed);
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
		super.onCollision(collidingObject, angle);

		if (collidingObject.getType() == ObjectType.ASSAULT_MISSILE) {
			AoEMissile.calculateAoEDamage(getGame(), this, collidingObject,
					S.Server.go_splashedmissile_damageradius,
					S.Server.go_splashedmissile_damage);
			getGame().getEventTriggerer().notifyAoEDamage(getType(),
					getCenterPosition());
		}
	}

	@Override
	public boolean isCollidableWith(GameObject otherObject) {
		if (otherObject.getType().equals(ObjectType.ASSAULT_MISSILE)
				&& otherObject.getOwner() == getOwner()) {
			return true;
		} else {
			return super.isCollidableWith(otherObject);
		}
	}
}
