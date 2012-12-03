package de.illonis.eduras.items;

import de.illonis.eduras.GameObject;

/**
 * Indicates an item is consumable, that means it is removed when used.
 * 
 * @author illonis
 * 
 */
public interface Consumable extends Usable {

	@Override
	public void use(GameObject unit);
}
