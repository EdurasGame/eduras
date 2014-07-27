package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.math.Vector2df;
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
		setDamageRadius(S.Server.go_splashmissile_damageradius);
		setShape(new Circle(S.Server.go_splashmissile_shape_radius,
				S.Server.go_splashmissile_shape_radius,
				S.Server.go_splashmissile_shape_radius));
		setSpeed(S.Server.go_splashmissile_speed);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		super.onCollision(collidingObject);

		int numberOfSplinters = S.Server.go_splashmissile_splinters;

		float circumferenceOfSplittering = 1;

		Vector2df speed[] = new Vector2df[numberOfSplinters];
		float degreeSteps = 2 * (float) Math.PI / numberOfSplinters;
		for (int i = 0; i < numberOfSplinters; i++) {
			float currentDegree = i * degreeSteps;
			float x = (float) Math.cos(currentDegree)
					* circumferenceOfSplittering;
			float y;
			if (x == circumferenceOfSplittering
					|| x == -circumferenceOfSplittering) {
				y = 0;
			} else {
				if (x == 0) {
					y = circumferenceOfSplittering;
				} else {
					y = (float) Math.sqrt(Math.pow(circumferenceOfSplittering,
							2) - Math.pow(x, 2));
				}
			}

			if (currentDegree >= Math.PI) {
				y = -y;
			}

			speed[i] = new Vector2df(x, y);
		}

		for (int i = 0; i < speed.length; i++) {
			Vector2df pos = getPositionVector().copy();
			speed[i].scale(S.Server.go_splashmissile_shape_radius * 2);
			pos.add(speed[i]);
			getGame().getEventTriggerer().createMissile(
					ObjectType.MISSILE_SPLASHED, getOwner(), pos, speed[i]);
		}
	}

	@Override
	public boolean isCollidableWith(GameObject otherObject) {
		if (otherObject instanceof Missile
				&& otherObject.getOwner() == getOwner()) {
			return true;
		} else {
			return super.isCollidableWith(otherObject);
		}
	}
}
