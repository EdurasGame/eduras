package de.illonis.eduras.items;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.ObjectFactory.ObjectType;

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

	private int sellValue;
	private int buyValue;
	private String name;

	/**
	 * Creates a new item of given item type.
	 * 
	 * @param type
	 *            item type.
	 * @param gi
	 *            game information.
	 */
	public Item(ObjectType type, GameInformation gi) {
		super(gi);
		setObjectType(type);
		this.name = "unknown";
	}

	/**
	 * Returns name of item.
	 * 
	 * @return item name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of item.
	 * 
	 * @param name
	 *            new itemname.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks whether this item is usable.
	 * 
	 * @return true if item is usable, false otherwise.
	 */
	public boolean isUsable() {
		return (this instanceof Usable);
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
	 *            item type id.
	 * @return new item of given id.
	 */
	public final static Item createById(ObjectType id) {
		// TODO: implement
		return null;
	}

	/**
	 * Checks if this item has the same itemtype as given item.
	 * 
	 * @param item
	 *            item to compare.
	 * @return true if item has same itemtype.
	 */
	public final boolean equalsType(Item item) {
		return getType().equals(item.getType());
	}
}
