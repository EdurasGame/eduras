package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.Usable;

public abstract class Weapon extends Item implements Lootable, Usable {

	private int damage = 0;
	private long cooldown = 0;
	private long lastDamageTime = 0;

	public Weapon(ItemType type, GameInformation gi) {
		super(type, gi);
	}

	public int getDamage() {
		return damage;
	}

}
