package de.illonis.eduras;

/**
 * Meta class for all objects that can be on the game's map.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class GameObject {

	public static int lastId = 0;

	private int id;

	private int xPosition;

	private int yPosition;

	public GameObject() {
		this.id = lastId++;
	}

	/**
	 * Returns the object's id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the object's id to the given value.
	 * 
	 * @param id
	 *            The new id value.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the x-position of the object.
	 * 
	 * @return The x-position.
	 */
	public int getXPosition() {
		return xPosition;
	}

	/**
	 * Sets the x-position of the object.
	 * 
	 * @param xPosition
	 *            The new value of the x-position.
	 */
	public void setXPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	/**
	 * Returns the y-position of the
	 * 
	 * @return The y-position.
	 */
	public int getYPosition() {
		return yPosition;
	}

	/**
	 * Sets the y-position of the object.
	 * 
	 * @param yPosition
	 *            The value of the new y-position.
	 */
	public void setYPosition(int yPosition) {
		this.yPosition = yPosition;
	}
}