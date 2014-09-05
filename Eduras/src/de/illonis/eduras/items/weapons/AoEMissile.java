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
	public void onCollision(GameObject collidingObject) {
		super.onCollision(collidingObject);

		LinkedList<GameObject> nearObjects = getGame().findObjectsInDistance(
				new Vector2f(getShape().getCenterX(), getShape().getCenterY()),
				getDamageRadius());
		for (GameObject nearObject : nearObjects) {
			// do not handle collided object twice.
			if (nearObject.equals(collidingObject) || nearObject.equals(this))
				continue;
			Relation nearRelation = getGame().getGameSettings().getGameMode()
					.getRelation(this, nearObject);

			if (nearRelation == Relation.HOSTILE && nearObject.isUnit()) {
				((Unit) nearObject).damagedBy(getDamage(), getOwner());
			}
		}
	}
}
