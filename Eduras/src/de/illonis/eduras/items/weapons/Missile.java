package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;

/**
 * A missile is shot by a weapon.
 * 
 * @author illonis
 * 
 */
public abstract class Missile extends MoveableGameObject {

	private int damage;
	private double damageRadius;
	private double maxRange = 0;
	private double rangeMoved;

	/**
	 * Creates a new missile with the id given and in the context of specific
	 * gameinformation.
	 * 
	 * @param game
	 *            The gameinformation.
	 * @param id
	 *            The id.
	 */
	public Missile(GameInformation game, int id) {
		super(game, id);
		rangeMoved = 0;
		setShape(new Circle(5));
	}

	/**
	 * Returns range of this missile.
	 * 
	 * @return missile range.
	 * 
	 * @author illonis
	 */
	public double getMaxRange() {
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
	protected void setMaxRange(double maxRange) {
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
	public double getDamageRadius() {
		return damageRadius;
	}

	/**
	 * Sets the radius in which the missile causes damage to objects relative to
	 * its position.
	 * 
	 * @param damageRadius
	 */
	public void setDamageRadius(double damageRadius) {
		this.damageRadius = damageRadius;
	}

	/**
	 * Creates a new missile.
	 * 
	 * @return a new missile that is identic to this one.
	 */
	public abstract Missile spawn();

	@Override
	public void onMove(long delta) {
		if (maxRange == 0)
			super.onMove(delta);
		else {
			Vector2D lastPos = getPositionVector();
			super.onMove(delta);
			Vector2D newPos = getPositionVector();
			rangeMoved += lastPos.calculateDistance(newPos);
			if (rangeMoved > maxRange)
				removeSelf();
		}
	}

}
