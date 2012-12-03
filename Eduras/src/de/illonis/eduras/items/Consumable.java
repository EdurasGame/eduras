package de.illonis.eduras.items;

import de.illonis.eduras.GameObject;

public interface Consumable extends Usable {

	@Override
	public void use(GameObject unit);
}
