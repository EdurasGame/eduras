package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.items.Item;

public abstract class Weapon extends Item {

	public Weapon(ItemType type, GameInformation gi) {
		super(type, gi);
	}

}
