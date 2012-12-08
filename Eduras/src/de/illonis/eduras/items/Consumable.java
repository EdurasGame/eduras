package de.illonis.eduras.items;


/**
 * Indicates an item is consumable, that means it is removed when used.
 * 
 * @author illonis
 * 
 */
public interface Consumable extends Usable {

	@Override
	public void use(ItemUseInformation info);
}
