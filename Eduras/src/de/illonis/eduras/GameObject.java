package de.illonis.eduras;

import java.awt.Graphics2D;

import de.illonis.eduras.interfaces.Drawable;

/**
 * Meta class for all objects that can be on the game's map.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class GameObject implements Drawable {

	public static int lastId = 0;

	private int id;

	private int xPosition, yPosition;

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
	 * Sets the position of the object.<br>
	 * This is equal to calling <code>setXPosition(x)</code> and
	 * <code>setYPosition(y)</code>.
	 * 
	 * @param x
	 *            The new value of the x-position.
	 * @param y
	 *            The new value of the y-position.
	 */
	public void setPosition(int x, int y) {
		setXPosition(x);
		setYPosition(y);
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
	 * Modifies current x-position of the object.<br>
	 * This modifies current position by adding given value. This is equal to
	 * <code>setXPosition(getXPosition() + xDiff)</code>.
	 * 
	 * @see #setXPosition(int)
	 * 
	 * @param xDiff
	 *            value to be added to x-position.
	 */
	public void modifyXPosition(int xDiff) {
		setXPosition(xPosition + xDiff);
	}

	/**
	 * Returns the y-position of the object.
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

	/**
	 * Modifies current y-position of the object.<br>
	 * This modifies current position by adding given value. This is equal to
	 * <code>setYPosition(getYPosition() + yDiff)</code>.
	 * 
	 * @see #setYPosition(int)
	 * 
	 * @param yDiff
	 *            value to be added to y-position.
	 */
	public void modifyYPosition(int yDiff) {
		setYPosition(yPosition + yDiff);
	}

	@Override
	public void draw(Graphics2D g) {
	}
}