package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.units.Unit;

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
	 * @param id
	 *            object id.
	 */
	public SplashMissile(GameInformation game, int id) {
		super(game, id);
		setObjectType(ObjectType.MISSILE_SPLASH);
		setDamage(7);
		setDamageRadius(1);
		setShape(new Circle(5));
		setSpeed(150);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		if (collidingObject.isUnit()) {
			((Unit) collidingObject).damagedBy(getDamage(), getOwner());
		}
		removeSelf();
		Vector2D speed[] = { new Vector2D(1, 1), new Vector2D(-1, 1),
				new Vector2D(-1, -1), new Vector2D(1, -1) };
		for (int i = 0; i < speed.length; i++) {
			Vector2D pos = getPositionVector().copy();
			speed[i].mult(10);
			pos.add(speed[i]);
			getGame().getEventTriggerer().createMissile(
					ObjectType.MISSILE_SPLASHED, getOwner(), pos, speed[i]);
		}
	}

	@Override
	public void onMapBoundsReached() {
		removeSelf();
	}

}
