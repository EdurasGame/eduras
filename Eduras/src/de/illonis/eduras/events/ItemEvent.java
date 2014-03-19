package de.illonis.eduras.events;

import org.newdawn.slick.geom.Vector2f;

import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;

/**
 * Used as a wrapper for item events like use_item_pressed and _released. An
 * item event is related to a specific item (identified by its objectid) which
 * belongs to a specific owner. In some cases, for example item_use, you need
 * some additional info, e.g. the target where to use an item. Thats why this
 * class contains a target argument which you have to set in some case.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ItemEvent extends OwnerGameEvent {

	private int slotNum = -1;
	private Vector2f target;

	/**
	 * Create a new ItemEvent of the given type.
	 * 
	 * @param eventType
	 *            The type.
	 * @param ownerId
	 *            The id of the owner this item belongs to.
	 */
	public ItemEvent(GameEventNumber eventType, int ownerId) {
		super(eventType, ownerId);
		target = new Vector2f();
	}

	/**
	 * (jme) Create a new ItemEvent of the given type that has an related item
	 * slot.
	 * 
	 * @param eventType
	 *            The type.
	 * @param ownerId
	 *            The id of the owner this item belongs to.
	 * @param slot
	 *            related item slot.
	 */
	public ItemEvent(GameEventNumber eventType, int ownerId, int slot) {
		this(eventType, ownerId);
		setSlotNum(slot);
	}

	/**
	 * Set the itemslot of the player's inventory which is related to this
	 * event.
	 * 
	 * @param slotNum
	 *            The number of the slot.
	 */
	public void setSlotNum(int slotNum) {
		this.slotNum = slotNum;
	}

	/**
	 * Returns the slotnumber of the item to use.
	 * 
	 * @return The slotnumber.
	 */
	public int getSlotNum() {
		return this.slotNum;
	}

	/**
	 * Set the target position of the item use.
	 * 
	 * @param target
	 *            The target position.
	 */
	public void setTarget(Vector2f target) {
		this.target = target;
	}

	/**
	 * Returns the target of the item event.
	 * 
	 * @return The target.
	 */
	public Vector2f getTarget() {
		return target;
	}

	@Override
	public Object getArgument(int i) throws TooFewArgumentsExceptions {
		switch (getType()) {
		case ITEM_CD_FINISHED:
		case ITEM_CD_START:
			if (i == 0)
				return getOwner();
			if (i == 1)
				return slotNum;
			else
				throw new TooFewArgumentsExceptions(i, 1);
		case ITEM_USE:
			switch (i) {
			case 0:
				return slotNum;
			case 1:
				return getOwner();
			case 2:
				return target.getX();
			case 3:
				return target.getY();
			default:
				throw new TooFewArgumentsExceptions(i, 3);
			}
		default:
			throw new IllegalStateException(
					"This ItemEvent has no valid event number.");
		}

	}

	@Override
	public int getNumberOfArguments() {
		switch (getType()) {
		case ITEM_CD_FINISHED:
		case ITEM_CD_START:
			return 2;
		case ITEM_USE:
			return 4;
		default:
			return -1;
		}
	}

}
