package de.illonis.eduras.gameobjects;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.units.Unit;

/**
 * Meta class for all objects that can be on the game's map.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class GameObject implements Comparable<GameObject> {

	private final GameInformation game;
	private ObjectType type;

	private ObjectShape shape;
	private boolean collidable = true;
	private boolean visible = true;

	private int id;
	private int owner = -1;

	private double xPosition, yPosition;

	/**
	 * Creates a new gameobject that is associated with given game. Each
	 * gameobject will be given an unique id.
	 * 
	 * @param game
	 *            game that contains this object.
	 */
	public GameObject(GameInformation game, int id) {
		this.game = game;
		this.id = id;
		setObjectType(ObjectType.NO_OBJECT);
	}

	/**
	 * Returns type of this object.
	 * 
	 * @return type of object.
	 */
	public ObjectType getType() {
		return type;
	}

	protected void setObjectType(ObjectType type) {
		this.type = type;
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
	public GameInformation getGame() {
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
	 * @see #setXPosition(double)
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
	 * @see #setYPosition(double)
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

	/**
	 * Sets shape of object to given shape.
	 * 
	 * @param shape
	 *            new shape.
	 */
	protected void setShape(ObjectShape shape) {
		this.shape = shape;
	}

	/**
	 * Returns a vector that points from origin to the position of the
	 * GameObject.
	 * 
	 * @return The position vector.
	 */
	public Vector2D toPositionVector() {
		return new Vector2D(xPosition, yPosition);
	}

	@Override
	public int compareTo(GameObject o) {
		if (getId() == o.getId())
			return 0;
		else if (getId() > o.getId())
			return 1;
		else
			return -1;
	}

	/**
	 * Check wether the given object is the same by comparing the ids.
	 * 
	 * @param object
	 *            The object to compare.
	 * @return Returns true if the ids match and false otherwise.
	 */
	public boolean equals(GameObject object) {
		return this.id == object.getId();
	}

	/**
	 * Returns a vector pointing to the position of the object.
	 * 
	 * @return The position vector.
	 */
	public Vector2D getPositionVector() {
		return new Vector2D(getXPosition(), getYPosition());
	}

	/**
	 * Returns bounding box. This is required for renderer to compute which
	 * objects are in visible region.
	 * 
	 * @return bounding box.
	 */
	public Rectangle2D.Double getBoundingBox() {

		Rectangle2D.Double r = getShape().getBoundingBox();
		r.x = getDrawX() - getShape().getBoundingBox().getWidth() / 2;
		r.y = getDrawY() - getShape().getBoundingBox().getHeight() / 2;

		return r;
	}

	/**
	 * This method is called when an object collides with this object.
	 * 
	 * @param collidingObject
	 *            The object colliding with this object.
	 */
	public abstract void onCollision(GameObject collidingObject);

	/**
	 * Returns wether this object is collidable or not.
	 * 
	 * @return True if it is collidable and false otherwise.
	 */
	public boolean isCollidable() {
		return collidable;
	}

	/**
	 * Sets the collidable status of the object.
	 * 
	 * @param collidable
	 *            The new status of the collidable object. If true, the object
	 *            will be collidable.
	 */
	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	/**
	 * Returns the visible status of this object.
	 * 
	 * @return Returns true if the object is visible and false otherwise.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible status of this object.
	 * 
	 * @param visible
	 *            The new status. If you set it to false, the object won't be
	 *            visible.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Checks if the shape related to this object is intersected by another
	 * moving object, which is represented by lines.
	 * 
	 * @param lines
	 *            The lines representing the moving object.
	 * @return Returns a linked list of collision points. The list will be empty
	 *         if there is no collision.
	 */
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines) {

		if (this.isCollidable()) {
			return this.getShape().isIntersected(lines, this);
		}

		return new LinkedList<CollisionPoint>();
	}

	/**
	 * Checks if this is a unit.
	 * 
	 * @return true if this is a unit.
	 */
	public final boolean isUnit() {
		return (this instanceof Unit);
	}

	/**
	 * Triggers a remove of this object from gameobject list.
	 */
	protected final void removeSelf() {
		getGame().getEventTriggerer().removeObject(getId());
	}
}