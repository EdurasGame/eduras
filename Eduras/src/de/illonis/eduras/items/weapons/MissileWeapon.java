package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;

public abstract class MissileWeapon extends Weapon {

	private double missileSpeed;

	public MissileWeapon(ItemType type, GameInformation gi) {
		super(type, gi);
	}

	public double getMissileSpeed() {
		return missileSpeed;
	}

	public void setMissileSpeed(double missileSpeed) {
		this.missileSpeed = missileSpeed;
	}

}
