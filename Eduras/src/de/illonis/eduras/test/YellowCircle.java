package de.illonis.eduras.test;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.Game;
import de.illonis.eduras.MoveableGameObject;
import de.illonis.eduras.interfaces.Controllable;
import de.illonis.eduras.math.Vector2D;

/**
 * A yellow circle is drawn around its position with specified diameter.
 * 
 * @author illonis
 * 
 */
public class YellowCircle extends MoveableGameObject implements Controllable {
	private int size;

	/**
	 * Creates a new yellow circle with default size (20).
	 * 
	 * @see #YellowCircle(int)
	 */
	public YellowCircle(Game game) {
		this(game, 20);
	}

	/**
	 * Creates a new circle with given size.
	 * 
	 * @see #YellowCircle()
	 * 
	 * @param size
	 *            diameter of circle.
	 */
	public YellowCircle(Game game, int size) {
		super(game);
		if (size <= 0)
			size = 20;
		this.size = size;
		setSpeed(80);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillOval(getDrawX() - size / 2, getDrawY() - size / 2, size, size);
	}

	@Override
	public void startMoving(Direction direction) {
		switch (direction) {
		case UP:
			getSpeedVector().setY(-getSpeed());
			break;
		case DOWN:
			getSpeedVector().setY(getSpeed());
			break;
		case LEFT:
			getSpeedVector().setX(-getSpeed());
			break;
		case RIGHT:
			getSpeedVector().setX(getSpeed());
			break;
		default:
			break;
		}
	}

	@Override
	public void stopMoving(Direction direction) {
		if (isHorizontal(direction)) {
			getSpeedVector().setX(0);
		} else {
			getSpeedVector().setY(0);
		}
	}

	@Override
	public void stopMoving() {
		setSpeedVector(new Vector2D());
	}
}
