package de.illonis.eduras.items;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;

/**
 * Items can be hold in player's inventory.
 * 
 * @author illonis
 * 
 */
public abstract class Item extends GameObject {

	private int sellValue;
	private int buyValue;
	private String name;
	private boolean unique;

	/**
	 * Creates a new item of given item type.
	 * 
	 * @param type
	 *            item type.
	 * @param gi
	 *            game information.
	 * @param id
	 *            the object id of the item.
	 */
	public Item(ObjectType type, GameInformation gi, int id) {
		super(gi, id);
		setObjectType(type);
		this.name = "unknown";
		unique = true;
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
	 * Sets whether this item is unique. Unique items can exist only once in
	 * inventory.
	 * 
	 * @param unique
	 *            new unqique flag, default true.
	 * 
	 * @author illonis
	 */
	protected void setUnique(boolean unique) {
		this.unique = unique;
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
	 * Checks if this item has the same itemtype as given item.
	 * 
	 * @param item
	 *            item to compare.
	 * @return true if item has same itemtype.
	 */
	public final boolean equalsType(Item item) {
		return getType().equals(item.getType());
	}

	/**
	 * Returns whether this item is unique.
	 * 
	 * @see #setUnique(boolean)
	 * @return true if this item is unique.
	 * 
	 * @author illonis
	 */
	public boolean isUnique() {
		return unique;
	}
}
