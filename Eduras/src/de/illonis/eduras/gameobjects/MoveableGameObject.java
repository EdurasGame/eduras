package de.illonis.eduras.gameobjects;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.exceptions.MapBorderReachedException;
import de.illonis.eduras.interfaces.Moveable;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A moveable gameobject. It differs from {@link GameObject} because it has a
 * movement speed and a move action.
 * 
 * @author illonis
 * 
 */
public abstract class MoveableGameObject extends GameObject implements Moveable {

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

	private float speed = 0;
	protected float currentSpeedX;
	protected float currentSpeedY;

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
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            the object id.
	 */
	public MoveableGameObject(GameInformation game, TimingSource timingSource,
			int id) {
		super(game, timingSource, id);
		currentSpeedX = 0f;
		currentSpeedY = 0f;
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
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Returns speed of gameobject.
	 * 
	 * @return speed of gameobject.
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Sets speed vector to given vector.
	 * 
	 * @param speedVector
	 *            new speed vector
	 */
	public void setSpeedVector(Vector2df speedVector) {
		currentSpeedX = speedVector.x;
		currentSpeedY = speedVector.y;
	}

	/**
	 * Returns the speed vector.
	 * 
	 * @return the speed vector.
	 * 
	 * @author illonis
	 */
	public Vector2f getSpeedVector() {
		return new Vector2f(currentSpeedX, currentSpeedY);
	}

	@Override
	public void onMove(long delta) {
		if (currentSpeedX == 0f && currentSpeedY == 0f)
			return;
		if (this instanceof PlayerMainFigure)
			System.out.println("x: " + currentSpeedX + ", y: " + currentSpeedY);
		float distance = speed * (delta / (float) 1000L);

		Vector2f target = getSpeedVector().normalise().scale(distance)
				.add(getPositionVector());

		Vector2f targetPos;
		try {
			targetPos = this.checkCollisionOnMove(target);
			setPosition(targetPos);
		} catch (MapBorderReachedException e) {
			onMapBoundsReached();
		}
	}

	@Override
	public void onRotate(float rotationAngle) {
		rotationAngle = checkCollisionOnRotation(rotationAngle);
		rotation = rotationAngle;
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
	public Vector2f checkCollisionOnMove(Vector2f target)
			throws MapBorderReachedException {
		return target;
		// Vector2df currentTarget = target;
		// Vector2df collisionPoint = this.getShape().checkCollisionOnMove(
		// getGame(), this, currentTarget);
		//
		// while (!collisionPoint.equals(currentTarget)) {
		// currentTarget = collisionPoint;
		// collisionPoint = this.getShape().checkCollisionOnMove(getGame(),
		// this, currentTarget);
		// }
		//
		// return currentTarget;
	}

	/**
	 * Check if the game object will collide trying to rotate to the given
	 * angle.
	 * 
	 * @param targetRotationAngle
	 *            The absolute target angle the object tries to rotate to.
	 * @return Returns the angle of the gameobject after the rotation.
	 */
	public float checkCollisionOnRotation(float targetRotationAngle) {
		return targetRotationAngle;
		// return this.getShape().checkCollisionOnRotation(getGame(), this,
		// targetRotationAngle);
	}
}