package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.MoveableGameObject;

public abstract class Missile extends MoveableGameObject {

	private int damage;
	private double damageRadius;

	public Missile(GameInformation game) {
		super(game);
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}

	public double getDamageRadius() {
		return damageRadius;
	}

	public void setDamageRadius(double damageRadius) {
		this.damageRadius = damageRadius;
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// TODO: Damage collidingObject
		getGame().removeObject(this);
	}

	/**
	 * Creates a new missile.
	 * 
	 * @return a new missile that is identic to this one.
	 */
	public abstract Missile spawn();
}
