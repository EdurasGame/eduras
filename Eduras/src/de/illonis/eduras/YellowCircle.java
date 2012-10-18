package de.illonis.eduras;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.interfaces.Drawable;

public class YellowCircle extends GameObject implements Drawable {
	private int size;

	public YellowCircle() {
		this(20);
	}

	public YellowCircle(int size) {
		if (size <= 0)
			size = 20;
		this.size = size;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillOval(xPosition - size / 2, yPosition - size / 2, size, size);
	}
}
