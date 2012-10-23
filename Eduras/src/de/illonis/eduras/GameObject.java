package de.illonis.eduras;

import java.awt.Graphics2D;

import de.illonis.eduras.interfaces.Drawable;
import de.illonis.eduras.math.Vector2D;

/**
 * Meta class for all objects that can be on the game's map.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class GameObject implements Drawable {

	public static int lastId = 0;
	private final Game game;

	private ObjectShape shape;

	private int id;
	private int owner = 0;

	private double xPosition, yPosition;

	/**
	 * Creates a new gameobject that is associated with given game. Each
	 * gameobject will be given an unique id.
	 * 
	 * @param game
	 *            game that contains this object.
	 */
	public GameObject(Game game) {
		this.game = game;
		this.id = lastId++;
	}

	/**
	 * Sets owner to given owner.
	 * 
	 * @param owner
	 *            new owner.
	 */
	public void setOwner(int owner) {
		this.owner = owner;
	}

	/**
	 * Returns owner of this object.
	 * 
	 * @return owner.
	 */
	public int getOwner() {
		return owner;
	}

	/**
	 * Returns game that this object belongs to.
	 * 
	 * @return game that this object belongs to.
	 */
	public Game getGame() {
		return game;
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
	public void setPosition(double x, double y) {
		setXPosition(x);
		setYPosition(y);
	}

	/**
	 * Returns the x-position of the object.
	 * 
	 * @return The x-position.
	 */
	public double getXPosition() {
		return xPosition;
	}

	/**
	 * Sets the x-position of the object.
	 * 
	 * @param xPosition
	 *            The new value of the x-position.
	 */
	public void setXPosition(double xPosition) {
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
	public void modifyXPosition(double xDiff) {
		setXPosition(xPosition + xDiff);
	}

	/**
	 * Returns the y-position of the object.
	 * 
	 * @return The y-position.
	 */
	public double getYPosition() {
		return yPosition;
	}

	/**
	 * Sets the y-position of the object.
	 * 
	 * @param yPosition
	 *            The value of the new y-position.
	 */
	public void setYPosition(double yPosition) {
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
	public void modifyYPosition(double yDiff) {
		setYPosition(yPosition + yDiff);
	}

	/**
	 * Returns x-value of draw-postion of this object. Draw position is
	 * generated by rounding current position.
	 * 
	 * @see #getXPosition()
	 * 
	 * @return x-value of draw-postion
	 */
	public int getDrawX() {
		return (int) Math.round(xPosition);
	}

	/**
	 * Returns y-value of draw-postion of this object. Draw position is
	 * generated by rounding current position.
	 * 
	 * @see #getYPosition()
	 * 
	 * @return y-value of draw-postion
	 */
	public int getDrawY() {
		return (int) Math.round(yPosition);
	}

	/**
	 * Returns the shape of the object.
	 * 
	 * @return The shape of the object.
	 */
	public ObjectShape getShape() {
		return shape;
	}

	/* (non-Javadoc)
	 * @see de.illonis.eduras.interfaces.Drawable#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g) {
	}
	
	/**
	 * Returns a vector that points from origin to the position of the GameObject.
	 * @return The position vector.
	 */
	public Vector2D toPositionVector() {
		return new Vector2D(xPosition, yPosition);
	}
}