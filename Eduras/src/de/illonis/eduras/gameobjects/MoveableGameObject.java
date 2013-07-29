package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.exceptions.MapBorderReachedException;
import de.illonis.eduras.interfaces.Moveable;
import de.illonis.eduras.math.Vector2D;

/**
 * A moveable gameobject. It differs from {@link GameObject} because it has a
 * movement speed and a move action.
 * 
 * @author illonis
 * 
 */
public abstract class MoveableGameObject extends ActiveGameObject implements
		Moveable {

	/**
	 * Directions of movement.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public static enum Direction {
		LEFT, RIGHT, TOP, BOTTOM, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT
	}

	private Direction currentDirection;

	private double speed = 0;
	private Vector2D speedVector = new Vector2D();

	/**
	 * Returns true if movement direction is horizontal.
	 * 
	 * @param direction
	 *            direction to check.
	 * @return true if movement direction is horizontal, false otherwise.
	 */
	public final static boolean isHorizontal(Direction direction) {
		return (direction == Direction.LEFT || direction == Direction.RIGHT);
	}

	/**
	 * Creates a new moveable gameobject that is associated with given game.
	 * 
	 * @param game
	 *            game that contains this object.
	 * @param id
	 *            the object id.
	 */
	public MoveableGameObject(GameInformation game, int id) {
		super(game, id);
	}

	/**
	 * Returns current facing of gameobject. This is the last direction this
	 * object moved to.
	 * 
	 * @return current facing of gameobject.
	 */
	public Direction getCurrentDirection() {
		return currentDirection;
	}

	/**
	 * Sets speed.
	 * 
	 * @param speed
	 *            new speed.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Returns speed of gameobject.
	 * 
	 * @return speed of gameobject.
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Sets speed vector to given vector.
	 * 
	 * @param speedVector
	 *            new speed vector
	 */
	public void setSpeedVector(Vector2D speedVector) {
		this.speedVector = speedVector;
	}

	/**
	 * Returns the speed vector.
	 * 
	 * @return the speed vector.
	 * 
	 * @author illonis
	 */
	public Vector2D getSpeedVector() {
		return speedVector;
	}

	@Override
	public void onMove(long delta) {
		if (speedVector.isNull())
			return;
		double distance = speed * (delta / (double) 1000L);
		Vector2D unitSpeed = speedVector.getUnitVector();
		unitSpeed.mult(distance);
		double targetX = unitSpeed.getX() + getXPosition();
		double targetY = unitSpeed.getY() + getYPosition();

		Vector2D targetPos;
		try {
			targetPos = this.checkCollision(new Vector2D(targetX, targetY));
			setPosition(targetPos.getX(), targetPos.getY());
		} catch (MapBorderReachedException e) {
			onMapBoundsReached();
		}
	}

	/**
	 * Checks if there will be a collision of the movable object trying to move
	 * to the target position.
	 * 
	 * @param target
	 *            The target position.
	 * @return Returns the objects position after the move. Note that the
	 *         objects new position won't be set.
	 * @throws MapBorderReachedException
	 *             if object reached map border.
	 */
	public Vector2D checkCollision(Vector2D target)
			throws MapBorderReachedException {
		if (!getGame().getMap().contains(target)) {
			throw new MapBorderReachedException();
		}

		Vector2D collisionPoint = this.getShape().checkCollision(getGame(),
				this, target);

		return collisionPoint;
	}

	public void setCurrentDirection(Direction direction) {

		this.currentDirection = direction;

	}
}