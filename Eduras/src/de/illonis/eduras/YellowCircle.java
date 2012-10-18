package de.illonis.eduras;

import java.awt.Color;
import java.awt.Graphics2D;

public class YellowCircle extends MoveableGameObject {
	private int size;

	public YellowCircle() {
		this(20);
	}

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

		}

	}
}
