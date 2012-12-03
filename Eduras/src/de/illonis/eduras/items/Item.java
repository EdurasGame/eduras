package de.illonis.eduras.items;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;

/**
 * Items can be hold in player's inventory.
 * 
 * @author illonis
 * 
 */
public abstract class Item extends GameObject {

	/**
	 * Item types are unique and identify every item type. Note that multiple
	 * items can have the same item-type but not the same id.
	 * 
	 * @author illonis
	 * 
	 */
	public static enum ItemType {
		WEAPON_LASER(20), WEAPON_MISSILE(21);

		private int id;

		private ItemType(int id) {
			this.id = id;
		}

		public final int getTypeId() {
			return id;
		}

	}

	private ItemType type;

	private int sellValue;
	private int buyValue;

	public Item(ItemType type, GameInformation gi) {
		super(gi);
		this.type = type;
	}

	public ItemType getType() {
		return type;
	}

	/**
	 * Returns true if this item is stackable.
	 * 
	 * @return true if this item is stackable.
	 */
	public final boolean stacks() {
		return (this instanceof StackableItem);
	}

	/**
	 * Returns gold value of item that is received when selling.
	 * 
	 * @return sell value of item.
	 */
	public int getSellValue() {
		return sellValue;
	}

	/**
	 * Returns gold value of item that must be spend when buying.
	 * 
	 * @return buying value of item.
	 */
	public int getBuyValue() {
		return buyValue;
	}

	/**
	 * Creates a new item of given type.
	 * 
	 * @param id
	 * @return
	 */
	public final static Item createById(ItemType id) {
		return null;
	}
}
