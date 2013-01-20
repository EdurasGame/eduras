package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.MoveableGameObject;
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

	public Missile(GameInformation game, int id) {
		super(game, id);
		setShape(new Circle(5));
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

	/**
	 * Creates a new missile.
	 * 
	 * @return a new missile that is identic to this one.
	 */
	public abstract Missile spawn();
}
