package de.illonis.eduras.items.weapons;

import java.util.LinkedList;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.math.ShapeGeometry;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

/**
 * A missile is shot by a weapon.
 * 
 * @author illonis
 * 
 */
public abstract class Missile extends MoveableGameObject {

	private int damage;
	private float damageRadius;
	private float maxRange = 0;
	private float rangeMoved;

	/**
	 * Creates a new missile with the id given and in the context of specific
	 * gameinformation.
	 * 
	 * @param game
	 *            The gameinformation.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id.
	 */
	public Missile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		rangeMoved = 0;
		setShape(new Circle(S.Server.go_missile_radius,
				S.Server.go_missile_radius, S.Server.go_missile_radius));
		setMaxRange(S.Server.go_missile_defaultrange);
	}

	/**
	 * Returns range of this missile.
	 * 
	 * @return missile range.
	 * 
	 * @author illonis
	 */
	public float getMaxRange() {
		return maxRange;
	}

	/**
	 * Sets the maximum range of this missile.
	 * 
	 * @param maxRange
	 *            new value
	 * 
	 * @author illonis
	 */
	protected void setMaxRange(float maxRange) {
		this.maxRange = maxRange;
	}

	/**
	 * Set the damage the missile causes when hitting someone.
	 * 
	 * @param damage
	 *            The new damage.
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Returns the damage the missile causes when hitting someone.
	 * 
	 * @return The damage.
	 */
	public int getDamage() {
		return damage;
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
	public void onMove(long delta, ShapeGeometry geometry) {
		if (maxRange == 0)
			super.onMove(delta, geometry);
		else {
			Vector2df lastPos = getPositionVector();
			super.onMove(delta, geometry);
			Vector2df newPos = getPositionVector();
			rangeMoved += lastPos.distance(newPos);
			if (rangeMoved > maxRange)
				removeSelf();
		}
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		Relation relation = getGame().getGameSettings().getGameMode()
				.getRelation(this, collidingObject);

		if (relation == Relation.HOSTILE && collidingObject.isUnit()) {
			((Unit) collidingObject).damagedBy(getDamage(), getOwner());
		}

		if (getDamageRadius() > 1.5) {
			LinkedList<GameObject> nearObjects = getGame()
					.findObjectsInDistance(getPositionVector(),
							getDamageRadius());
			for (GameObject nearObject : nearObjects) {
				// do not handle collided object twice.
				if (nearObject.equals(collidingObject))
					continue;
				Relation nearRelation = getGame().getGameSettings()
						.getGameMode().getRelation(this, nearObject);

				if (nearRelation == Relation.HOSTILE && nearObject.isUnit()) {
					((Unit) nearObject).damagedBy(getDamage(), getOwner());
				}
			}
		}
		// TODO: use damage radius
		removeSelf();
	}

}
