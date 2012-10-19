package de.illonis.eduras.test;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.MoveableGameObject;

/**
 * A yellow circle is drawn around its position with specified diameter.
 * 
 * @author illonis
 * 
 */
public class YellowCircle extends MoveableGameObject {
	private int size;

	/**
	 * Creates a new yellow circle with default size (20).
	 * 
	 * @see #YellowCircle(int)
	 */
	public YellowCircle() {
		this(20);
	}

	/**
	 * Creates a new circle with given size.
	 * 
	 * @see #YellowCircle()
	 * 
	 * @param size
	 *            diameter of circle.
	 */
	public YellowCircle(int size) {
		if (size <= 0)
			size = 20;
		this.size = size;
		setSpeed(1);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillOval(getXPosition() - size / 2, getYPosition() - size / 2, size,
				size);
	}

	@Override
	public void onMove(Direction direction) {
		super.onMove(direction);
		switch (direction) {
		case DOWN:
			modifyYPosition(getSpeed());
			break;
		case UP:
			modifyYPosition(-getSpeed());
			break;
		case LEFT:
			modifyXPosition(-getSpeed());
			break;
		case RIGHT:
			modifyXPosition(getSpeed());
			break;
		default:
			break;
		}
	}
}
