package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

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
		setDamage(S.go_splashmissile_damage);
		setDamageRadius(S.go_splashmissile_damageradius);
		setShape(new Circle(S.go_splashmissile_shape_radius));
		setSpeed(S.go_splashmissile_speed);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		super.onCollision(collidingObject);

		int numberOfSplinters = S.go_splashmissile_splinters;

		double circumferenceOfSplittering = 1;

		Vector2D speed[] = new Vector2D[numberOfSplinters];
		double degreeSteps = 2 * Math.PI / numberOfSplinters;
		for (int i = 0; i < numberOfSplinters; i++) {
			double currentDegree = i * degreeSteps;
			double x = Math.cos(currentDegree) * circumferenceOfSplittering;
			double y;
			if (x == circumferenceOfSplittering
					|| x == -circumferenceOfSplittering) {
				y = 0;
			} else {
				if (x == 0) {
					y = circumferenceOfSplittering;
				} else {
					y = Math.sqrt(Math.pow(circumferenceOfSplittering, 2)
							- Math.pow(x, 2));
				}
			}

			if (currentDegree >= Math.PI) {
				y = -y;
			}

			speed[i] = new Vector2D(x, y);
		}

		// Vector2D speed[] = { new Vector2D(1, 1), new Vector2D(-1, 1),
		// new Vector2D(-1, -1), new Vector2D(1, -1) };
		for (int i = 0; i < speed.length; i++) {
			Vector2D pos = getPositionVector().copy();
			speed[i].mult(S.go_splashmissile_shape_radius * 2);
			pos.add(speed[i]);
			getGame().getEventTriggerer().createMissile(
					ObjectType.MISSILE_SPLASHED, getOwner(), pos, speed[i]);
		}
	}
}
