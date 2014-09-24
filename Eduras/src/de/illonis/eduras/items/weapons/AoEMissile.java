package de.illonis.eduras.items.weapons;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.units.Unit;

/**
 * AoEMissiles are those which deal damage to all objects in a circle around
 * their collision.
 * 
 * @author Florian 'Ren' Mai
 *
 */
public class AoEMissile extends Missile {

	private final static Logger L = EduLog.getLoggerFor(AoEMissile.class
			.getName());

	private float damageRadius;

	/**
	 * Create a new AoEMissile.
	 * 
	 * @param game
	 * @param timingSource
	 * @param id
	 */
	public AoEMissile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
	}

	/**
	 * Returns the radius in which the missile causes damage to objects relative
	 * to its position.
	 * 
	 * @return the radius
	 */
	public float getDamageRadius() {
		return damageRadius;
	}

	/**
	 * Sets the radius in which the missile causes damage to objects relative to
	 * its position.
	 * 
	 * @param damageRadius
	 */
	public void setDamageRadius(float damageRadius) {
		this.damageRadius = damageRadius;
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
		super.onCollision(collidingObject, angle);

		calculateAoEDamage(getGame(), this, collidingObject, getDamageRadius(),
				getDamage());
		getGame().getEventTriggerer().notifyAoEDamage(getType(),
				getCenterPosition());
	}

	static void calculateAoEDamage(GameInformation game,
			Missile damagingMissile, GameObject collidingObject,
			float damageRadius, int maxDamage) {
		LinkedList<GameObject> nearObjects = game.findObjectsInDistance(
				new Vector2f(damagingMissile.getShape().getCenterX(),
						damagingMissile.getShape().getCenterY()), damageRadius);
		for (GameObject nearObject : nearObjects) {
			// do not handle collided object twice.
			if (nearObject.equals(collidingObject)
					|| nearObject.equals(damagingMissile))
				continue;
			Relation nearRelation = game.getGameSettings().getGameMode()
					.getRelation(damagingMissile, nearObject);

			if (nearRelation == Relation.HOSTILE && nearObject.isUnit()) {
				int damage = computeDamageForDistance(
						nearObject.getDistanceTo(damagingMissile
								.getCenterPosition()), maxDamage, damageRadius);
				((Unit) nearObject).damagedBy(damage,
						damagingMissile.getOwner());
			}
		}
	}

	private static int computeDamageForDistance(float distanceTo, int damage,
			float damageRadius) {
		return (int) (damage * (1f - distanceTo / damageRadius));
	}
}
