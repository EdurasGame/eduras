package de.illonis.eduras.gameobjects;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.ai.AIControllable;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Meta class for all objects that can be on the game's map.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class GameObject implements Comparable<GameObject> {

	/**
	 * Defines the kind of relation between two units.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum Relation {
		HOSTILE, ALLIED, NEUTRAL, IGNORE, ENVIRONMENT, UNKNOWN
	}

	private final GameInformation game;
	private final TimingSource timingSource;
	private ObjectType type;

	private ObjectShape shape;
	private boolean collidable = true;
	private Visibility visible = Visibility.ALL;
	private double visionRange = 200;
	private double visionAngle = 90;
	private boolean isVisionBlocking = false;
	private int zLayer = 1;

	private int id;
	private int owner = -1;

	protected double rotation = 0;

	private double xPosition, yPosition;

	/**
	 * Describes which other objects can see this object.
	 */
	public enum Visibility {
		/**
		 * This object is invisible for everybody (including itself).
		 */
		INVISIBLE,
		/**
		 * This object is only visible for its direct owner.
		 */
		OWNER,
		/**
		 * This object is only visible for members in the same team. If this
		 * object or the compared object is not a unit, it is invisible.
		 */
		TEAM,
		/**
		 * This object is only visible for the team of this objects owner.
		 */
		OWNER_TEAM,
		/**
		 * This object is only visible for objects that are in an
		 * {@link Relation#ALLIED} relation to this object.
		 */
		OWNER_ALLIED,
		/**
		 * This object is visible for everybody.
		 */
		ALL;
	}

	/**
	 * Creates a new gameobject that is associated with given game. Each
	 * gameobject will be given an unique id.
	 * 
	 * @param game
	 *            game that contains this object.
	 * @param timingSource
	 *            the timing source used for timed events.
	 * @param id
	 *            the object id.
	 */
	public GameObject(GameInformation game, TimingSource timingSource, int id) {
		this.game = game;
		this.id = id;
		this.timingSource = timingSource;
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

	protected TimingSource getTimingSource() {
		return timingSource;
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

	@Override
	public int compareTo(GameObject o) {
		if (zLayer == o.zLayer)
			return 0;
		else if (zLayer > o.zLayer)
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
		// FIXME: Here is assumed that shape's center equals object's position.
		Rectangle2D.Double r = getShape().getBoundingBox();
		r.x = getDrawX() - getShape().getBoundingBox().getWidth() / 2;
		r.y = getDrawY() - getShape().getBoundingBox().getHeight() / 2;
		return r;
	}

	/**
	 * This method is called when an object collides with this object.<br>
	 * Note that this method is not called, when this object is not collidable.
	 * Use {@link #onTouch(GameObject)} to receive non-colliding overlapping
	 * events.
	 * 
	 * @param collidingObject
	 *            The object colliding with this object.
	 * 
	 * @see #isCollidable()
	 * @see #onTouch(GameObject)
	 */
	public abstract void onCollision(GameObject collidingObject);

	/**
	 * This method is called when an object touches this object.<br>
	 * A <i>touch</i> occurs when two objects overlap and at least one of them
	 * is non-collding.<br>
	 * This means, whenever a non-collidable object would collide if it was
	 * collidable, this method is called instead of
	 * {@link #onCollision(GameObject)}.
	 * 
	 * @param touchingObject
	 *            the object touching this object.
	 */
	public void onTouch(GameObject touchingObject) {
	}

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
	 * Returns whether the given object may see this object.
	 * 
	 * @param other
	 *            the spectating object.
	 * 
	 * @return Returns true if this object is visible and false otherwise.
	 */
	public final boolean isVisibleFor(GameObject other) {
		switch (visible) {
		case ALL:
			return true;
		case INVISIBLE:
			return false;
		case OWNER:
			return (this.owner == other.getOwner());
		case OWNER_TEAM:
			if (!other.isUnit())
				return false;
			PlayerMainFigure player;
			try {
				player = game.getPlayerByOwnerId(this.owner);
			} catch (ObjectNotFoundException e) {
				return false;
			}
			return player.getTeam().equals(((Unit) other).getTeam());
		case OWNER_ALLIED:
			return (game.getGameSettings().getGameMode()
					.getRelation(this, other) == Relation.ALLIED);
		case TEAM:
			if (!isUnit() || !other.isUnit())
				return false;
			Unit a = (Unit) this;
			Unit b = (Unit) other;
			return a.getTeam().equals(b.getTeam());
		default:
			return false;
		}
	}

	/**
	 * @return the visibility state of this object.
	 * @see GameObject#isVisibleFor(GameObject)
	 */
	public Visibility getVisibility() {
		return visible;
	}

	/**
	 * Sets the visibility status of this object.
	 * 
	 * @param visible
	 *            The new status.
	 */
	public void setVisible(Visibility visible) {
		this.visible = visible;
	}

	/**
	 * @return the vision radius in degree.
	 */
	public double getVisionAngle() {
		return visionAngle;
	}

	/**
	 * @return the vision range.
	 */
	public double getVisionRange() {
		return visionRange;
	}

	/**
	 * Sets the vision angle of this object.
	 * 
	 * @param visionAngle
	 *            angle in degree.
	 */
	public void setVisionAngle(double visionAngle) {
		this.visionAngle = visionAngle;
	}

	/**
	 * Sets the vision radius of this object.
	 * 
	 * @param visionRange
	 *            new vision range.
	 */
	public void setVisionRange(double visionRange) {
		this.visionRange = visionRange;
	}

	/**
	 * @return true if this object blocks others' vision.
	 */
	public boolean isVisionBlocking() {
		return isVisionBlocking;
	}

	/**
	 * Sets vision blocking state. If true, this object blocks vision from other
	 * objects.
	 * 
	 * @param isVisionBlocking
	 *            new value.
	 */
	public void setVisionBlocking(boolean isVisionBlocking) {
		this.isVisionBlocking = isVisionBlocking;
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
		return this.getShape().isIntersected(lines, this);
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
	 * Triggers a remove of this object from gameobject list. If the gameobject
	 * has an AI, that is discarded, too.
	 */
	protected final void removeSelf() {
		if (this instanceof AIControllable) {
			((AIControllable) this).getAI().discard();
		}
		getGame().getEventTriggerer().removeObject(getId());
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof GameObject)
			return this.equals((GameObject) obj);
		return false;
	}

	/**
	 * Returns the angle this object is rotated by.
	 * 
	 * @return The angle in degrees.
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Sets this game object's rotation angle to the given new value.
	 * 
	 * @param newValue
	 *            The new value.
	 */
	public void setRotation(Double newValue) {
		// if (this instanceof MoveableGameObject) {
		// ((MoveableGameObject) this).onRotate(newValue);
		// } else {
		rotation = newValue;
		// }
	}

	/**
	 * Compares {@link GameObject}s by their {@link GameObject#id}.
	 * 
	 * @author illonis
	 * 
	 */
	public static class GameObjectIdComparator implements
			Comparator<GameObject> {

		@Override
		public int compare(GameObject lhs, GameObject rhs) {
			if (lhs.getId() == rhs.getId())
				return 0;
			else if (lhs.getId() > rhs.getId())
				return 1;
			else
				return -1;
		}

	}
}
