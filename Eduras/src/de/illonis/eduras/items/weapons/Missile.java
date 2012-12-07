package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.MoveableGameObject;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.shapes.Circle;

/**
 * A missile is shot by a weapon.
 * 
 * @author illonis
 * 
 */
public class Missile extends MoveableGameObject {

	private int damage;
	private double damageRadius;

	public Missile(GameInformation game) {
		super(game);
		setShape(new Circle(5));
		setObjectType(ObjectType.MISSILE);
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
	public Missile spawn() {
		Missile m = new Missile(getGame());
		m.setDamage(getDamage());
		m.setDamageRadius(getDamageRadius());
		return m;
	}
}
