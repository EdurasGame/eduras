package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jdesktop.core.animation.timing.Animator;

import de.illonis.eduras.math.Vector2D;

/**
 * A demo animation moving a block right.
 * 
 * @author illonis
 * 
 */
public class DemoAnimation extends Animation {
	private int xPos;
	private int yPos;

	DemoAnimation(Vector2D mapPosition) {
		super("Test animation", 5000, mapPosition);
		xPos = (int) mapPosition.getX();
		yPos = (int) mapPosition.getY();
		System.out.println("anim at " + mapPosition);
	}

	@Override
	public void timingEvent(Animator source, double fraction) {
		xPos = (int) Math.round(position.getX() + fraction * 150);
	}

	@Override
	public void drawAnimation(Graphics2D g2d, int cameraX, int cameraY) {
		g2d.setColor(Color.CYAN);
		g2d.fillRect(xPos + cameraX, yPos + cameraY, 30, 30);
	}
}
