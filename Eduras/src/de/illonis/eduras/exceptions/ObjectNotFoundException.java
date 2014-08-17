/**
 * 
 */
package de.illonis.eduras.exceptions;

/**
 * This is thrown if an object cannot be found from the given id.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ObjectNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int id;

	/**
	 * Creates a new ObjectNotFoundException with the given number telling the
	 * object's id.
	 * 
	 * @param id
	 *            The object's id.
	 */
	public ObjectNotFoundException(int id) {
		super("Object id was: " + id);
		this.id = id;
	}

	/**
	 * Returns the object's id.
	 * 
	 * @return The object's id.
	 */
	public int getObjectId() {
		return id;
	}

}
