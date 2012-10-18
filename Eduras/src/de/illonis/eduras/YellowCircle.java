package de.illonis.eduras;

import java.awt.Color;
import java.awt.Graphics2D;

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
		speed = 1;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillOval(xPosition - size / 2, yPosition - size / 2, size, size);
	}

	@Override
	public void onMove(Direction direction) {
		super.onMove(direction);
		switch (direction) {
		case DOWN:
			yPosition += speed;
			break;
		case UP:
			yPosition -= speed;
			break;
		case LEFT:
			xPosition -= speed;
			break;
		case RIGHT:
			xPosition += speed;
			break;
		default:
			break;
		}
	}
}
