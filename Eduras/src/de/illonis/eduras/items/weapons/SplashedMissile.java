package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.ArtificialIntelligence;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.units.Unit;

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
		setDamage(3);
		setDamageRadius(1);
		setShape(new Circle(3));
		setSpeed(250);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		if (collidingObject.isUnit()) {
			((Unit) collidingObject).damagedBy(getDamage(), getOwner());
		}
		removeSelf();
	}

	@Override
	public void onMapBoundsReached() {
		removeSelf();
	}

	@Override
	public ArtificialIntelligence getAI() {
		// TODO Auto-generated method stub
		return null;
	}

}
