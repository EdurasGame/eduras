package de.illonis.eduras.gameobjects;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.ReferencedEntity;
import de.illonis.eduras.Team;
import de.illonis.eduras.ai.AIControllable;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.exceptions.ShapeNotSupportedException;
import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.Unit;

/**
 * Meta class for all objects that can be on the game's map.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class GameObject extends ReferencedEntity implements
		EditorPlaceable, Comparable<GameObject> {

	private final static Logger L = EduLog.getLoggerFor(GameObject.class
			.getName());

	/**
	 * denotes the environment (or that there is no owner respectivly)
	 */
	public static final int OWNER_WORLD = -1;

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

	private Shape shape;
	private boolean collidable = true;
	private Visibility visible = Visibility.ALL;
	private float visionRange = 200;
	private float visionAngle = 90;
	private boolean isVisionBlocking = false;
	private int zLayer = 1;
	private TextureKey texture;

	private int id;
	private int owner = OWNER_WORLD;

	protected float rotation = 0;

	private float xPosition, yPosition;
	protected float shapeOffsetX = 0, shapeOffsetY = 0;

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
		setRefName("");
		this.timingSource = timingSource;
		texture = TextureKey.NONE;
		setObjectType(ObjectType.NO_OBJECT);
	}

	public TextureKey getTexture() {
		return texture;
	}

	public void setTexture(TextureKey texture) {
		this.texture = texture;
	}

	/**
	 * Returns the x coordinate of the shape's offset.
	 * 
	 * @return x coordinate of shape's offset.
	 */
	public float getShapeOffsetX() {
		return shapeOffsetX;
	}

	/**
	 * Returns the y coordinate of the shape's offset.
	 * 
	 * @return y coordinate of shape's offset.
	 */
	public float getShapeOffsetY() {
		return shapeOffsetY;
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
	 * Returns the timing source this object uses for timing events.
	 * 
	 * @return timing source
	 */
	public TimingSource getTimingSource() {
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
	@Override
	public final void setPosition(float x, float y) {
		setXPosition(x);
		setYPosition(y);
	}

	/**
	 * Sets the position of the object.
	 * 
	 * @param pos
	 *            new position.
	 */
	public final void setPosition(Vector2f pos) {
		setPosition(pos.x, pos.y);
	}

	@Override
	public float getWidth() {
		return shape.getWidth();
	}

	@Override
	public float getHeight() {
		return shape.getHeight();
	}

	@Override
	public void setWidth(float width) {
		if (shape instanceof Rectangle) {
			((Rectangle) shape).setWidth(width);
		}
	}

	@Override
	public void setHeight(float height) {
		if (shape instanceof Rectangle) {
			((Rectangle) shape).setHeight(height);
		}
	}

	/**
	 * Returns the x-position of the object.
	 * 
	 * @return The x-position.
	 */
	@Override
	public final float getXPosition() {
		return xPosition;
	}

	/**
	 * Sets the x-position of the object.
	 * 
	 * @param xPosition
	 *            The new value of the x-position.
	 */
	@Override
	public final void setXPosition(float xPosition) {
		this.xPosition = xPosition;
		if (shape != null)
			shape.setX(xPosition + shapeOffsetX);
	}

	/**
	 * Modifies current x-position of the object.<br>
	 * This modifies current position by adding given value. This is equal to
	 * <code>setXPosition(getXPosition() + xDiff)</code>.
	 * 
	 * @see #setXPosition(float)
	 * 
	 * @param xDiff
	 *            value to be added to x-position.
	 */
	public final void modifyXPosition(float xDiff) {
		setXPosition(xPosition + xDiff);
	}

	/**
	 * Returns the y-position of the object.
	 * 
	 * @return The y-position.
	 */
	@Override
	public final float getYPosition() {
		return yPosition;
	}

	/**
	 * Sets the y-position of the object.
	 * 
	 * @param yPosition
	 *            The value of the new y-position.
	 */
	@Override
	public final void setYPosition(float yPosition) {
		this.yPosition = yPosition;
		if (shape != null)
			shape.setY(yPosition + shapeOffsetY);
	}

	/**
	 * Modifies current y-position of the object.<br>
	 * This modifies current position by adding given value. This is equal to
	 * <code>setYPosition(getYPosition() + yDiff)</code>.
	 * 
	 * @see #setYPosition(float)
	 * 
	 * @param yDiff
	 *            value to be added to y-position.
	 */
	public void modifyYPosition(float yDiff) {
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
	@Deprecated
	public final int getDrawX() {
		return Math.round(xPosition);
	}

	/**
	 * Returns y-value of draw-postion of this object. Draw position is
	 * generated by rounding current position.
	 * 
	 * @see #getYPosition()
	 * 
	 * @return y-value of draw-postion
	 */
	@Deprecated
	public final int getDrawY() {
		return Math.round(yPosition);
	}

	/**
	 * Returns the shape of the object.
	 * 
	 * @return The shape of the object.
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Sets shape of object to given shape.
	 * 
	 * @param shape
	 *            new shape.
	 */
	protected void setShape(Shape shape) {
		this.shape = shape;
		updatePosition();
	}

	private void updatePosition() {
		setXPosition(xPosition);
		setYPosition(yPosition);
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
	 * Get the zLayer of this object.
	 * 
	 * @return zlayer
	 */
	public int getzLayer() {
		return zLayer;
	}

	/**
	 * Set the zLayer of this object.
	 * 
	 * @param newVal
	 *            the value to set to
	 */
	public void setzLayer(int newVal) {
		this.zLayer = newVal;
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
	public Vector2df getPositionVector() {
		return new Vector2df(getXPosition(), getYPosition());
	}

	/**
	 * This method is called when an object collides with this object.<br>
	 * Note that this method is not called, when this object is not collidable.
	 * Use {@link #onTouch(GameObject)} to receive non-colliding overlapping
	 * events.
	 * 
	 * @param collidingObject
	 *            The object colliding with this object.
	 * @param angle
	 *            the angle at which the colliding object collides with this
	 *            object
	 * 
	 * @see #isCollidable(GameObject)
	 * @see #onTouch(GameObject)
	 */
	public abstract void onCollision(GameObject collidingObject, float angle);

	/**
	 * This method is called when an object touches this object.<br>
	 * A <i>touch</i> occurs when two objects overlap and at least one of them
	 * is non-collding.<br>
	 * This means, whenever a non-collidable object would collide if it was
	 * collidable, this method is called instead of
	 * {@link #onCollision(GameObject, float)}.
	 * 
	 * @param touchingObject
	 *            the object touching this object.
	 */
	public void onTouch(GameObject touchingObject) {
	}

	/**
	 * Returns whether this object can collide with another given object. If
	 * this object is set to not be collidable at all via
	 * {@link #setCollidable(boolean)}, false is returned immediately. If it is
	 * collidable in general, {@link #isCollidableWith(GameObject)} is called in
	 * order to determine whether it collides with the given object. If null is
	 * passed as argument, the argument is not considered.
	 * 
	 * @param otherObject
	 *            may be null.
	 * 
	 * @return True if it is collidable and false otherwise.
	 */
	public final boolean isCollidable(GameObject otherObject) {
		if (otherObject == null) {
			return collidable;
		} else {
			return collidable && isCollidableWith(otherObject);
		}
	}

	/**
	 * Determines if this object can collide with the other given object.
	 * 
	 * @param otherObject
	 * @return True if yes, false otherwise
	 */
	protected abstract boolean isCollidableWith(GameObject otherObject);

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
	public boolean isVisibleFor(GameObject other) {
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

			Unit otherUnit = (Unit) other;
			if (otherUnit.isDetector()
					&& getDistanceTo(other) <= otherUnit.getDetectionRange()) {
				return true;
			}

			Team playerTeam;

			if (isUnit()) {
				// we can use the unit's team directly
				playerTeam = ((Unit) this).getTeam();
			} else {
				// if it's not a unit, use the owners team
				Player player;
				try {
					player = game.getPlayerByOwnerId(this.owner);
				} catch (ObjectNotFoundException e) {
					return false;
				}
				try {
					playerTeam = player.getTeam();
				} catch (PlayerHasNoTeamException e) {
					return false;
				}
			}

			Team otherTeam = ((Unit) other).getTeam();
			if (playerTeam == null || otherTeam == null)
				return false;
			return playerTeam.equals(otherTeam);
		case OWNER_ALLIED:
			return (game.getGameSettings().getGameMode()
					.getRelation(this, other) == Relation.ALLIED);
		case TEAM:
			if (!isUnit() || !other.isUnit())
				return false;
			Team thisTeam = ((Unit) this).getTeam();
			Team oTeam = ((Unit) other).getTeam();
			if (thisTeam == null || oTeam == null) {
				return false;
			}
			return thisTeam.equals(oTeam);
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
	public float getVisionAngle() {
		return visionAngle;
	}

	/**
	 * @return the vision range.
	 */
	public float getVisionRange() {
		return visionRange;
	}

	/**
	 * Sets the vision angle of this object.
	 * 
	 * @param visionAngle
	 *            angle in degree.
	 */
	public void setVisionAngle(float visionAngle) {
		this.visionAngle = visionAngle;
	}

	/**
	 * Sets the vision radius of this object.
	 * 
	 * @param visionRange
	 *            new vision range.
	 */
	public void setVisionRange(float visionRange) {
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
	 * @throws ShapeNotSupportedException
	 */
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines)
			throws ShapeNotSupportedException {

		return Geometry.isShapeIntersectedByLines(lines, this.getShape());

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
	public float getRotation() {
		return rotation;
	}

	/**
	 * Sets this game object's rotation angle to the given new value.
	 * 
	 * @param newValue
	 *            The new value.
	 */
	public void setRotation(float newValue) {
		// if (this instanceof MoveableGameObject) {
		// ((MoveableGameObject) this).onRotate(newValue);
		// } else {
		rotation = newValue;
		// }
	}

	/**
	 * Returns the center of this object's shape.
	 * 
	 * @return center
	 */
	public Vector2f getCenterPosition() {
		return new Vector2f(shape.getCenterX(), shape.getCenterY());
	}

	public void setCenterPosition(Vector2f newCenterPosition) {
		shape.setCenterX(newCenterPosition.x);
		shape.setCenterY(newCenterPosition.y);

		setXPosition(shape.getX() - shapeOffsetX);
		setYPosition(shape.getY() - shapeOffsetY);
	}

	/**
	 * Returns true if the given objects are collidable with each other.
	 * 
	 * @param first
	 *            first object
	 * @param second
	 *            second object
	 * @return true if the given objects are collidable with each other, false
	 *         otherwise
	 */
	public static boolean canCollideWithEachOther(GameObject first,
			GameObject second) {
		return first.isCollidable(second) && second.isCollidable(first);
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

	/**
	 * Calculates the distance of this object to some point
	 * 
	 * @param point
	 * @return distance
	 */
	public float getDistanceTo(Vector2f point) {
		return getCenterPosition().distance(point);
	}

	/**
	 * Returns the distance to the other object by considering the other
	 * object's center point.
	 * 
	 * @param object
	 * @return distance
	 */
	public float getDistanceTo(GameObject object) {
		return getCenterPosition().distance(object.getCenterPosition());
	}
}
