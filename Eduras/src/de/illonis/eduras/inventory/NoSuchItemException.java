package de.illonis.eduras.inventory;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * Thrown if the requested item type is not available.
 * 
 * @author Florian Mai
 * 
 */
public class NoSuchItemException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger L = EduLog
			.getLoggerFor(NoSuchItemException.class.getName());

	private ObjectType itemType;

	/**
	 * Create a new NoSuchItemException.
	 * 
	 * @param itemType
	 *            The type of item that is not available.
	 */
	public NoSuchItemException(ObjectType itemType) {
		super("No item of type " + itemType + " available");
		this.itemType = itemType;
	}

	/**
	 * Returns the {@link ObjectType} that is not available.
	 * 
	 * @return type
	 */
	public ObjectType getItemType() {
		return itemType;
	}
}
