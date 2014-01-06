package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jdesktop.core.animation.timing.Animator;

import de.illonis.eduras.math.Vector2D;

public class DemoAnimation extends Animation {
	private int xPos;
	private int yPos;

	public DemoAnimation(Vector2D guiPosition) {
		super("Test animation", 5000, guiPosition);
		yPos = (int) guiPosition.getY();
		xPos = (int) guiPosition.getX();
	}

	@Override
	public void timingEvent(Animator source, double fraction) {
		xPos = (int) Math.round(position.getX() + fraction * 150);
	}

	@Override
	public void drawAnimation(Graphics2D g2d) {
		g2d.setColor(Color.CYAN);
		g2d.fillRect(xPos, yPos, 30, 30);
	}
}
