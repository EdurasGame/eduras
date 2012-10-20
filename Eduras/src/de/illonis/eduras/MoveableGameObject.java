package de.illonis.eduras;

import java.awt.geom.Point2D;

import de.illonis.eduras.interfaces.Moveable;
import de.illonis.eduras.math.Vector2D;

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
	public static enum Direction {
		LEFT, RIGHT, UP, DOWN, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT
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
	 */
	public MoveableGameObject(Game game) {
		super(game);
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

	public Vector2D getSpeedVector() {
		return speedVector;
	}

	@Override
	public void onMove(long delta) {
		double distance = speed * (delta / (double) 1000);
		Vector2D unitSpeed = speedVector.getUnitVector();
		unitSpeed.mult(distance);
		double targetX = unitSpeed.getX() + getXPosition();
		double targetY = unitSpeed.getY() + getYPosition();

		Point2D.Double targetPos = getGame().checkCollision(this,
				new Point2D.Double(targetX, targetY));

		setPosition(targetPos.getX(), targetPos.getY());
	}
}