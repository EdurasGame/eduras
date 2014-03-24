package de.illonis.eduras.gameclient.gui.animation;

import org.jdesktop.core.animation.timing.Animator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.math.Vector2df;

/**
 * A demo animation moving a block right.
 * 
 * @author illonis
 * 
 */
public class DemoAnimation extends Animation {
	private float xPos;
	private float yPos;

	DemoAnimation(Vector2df mapPosition) {
		super("Test animation", 5000, mapPosition);

	}

	@Override
	public void begin(Animator source) {
		xPos = position.getX();
		yPos = position.getY();
		super.begin(source);
	}

	@Override
	public void timingEvent(Animator source, double fraction) {
		xPos = (int) Math.round(position.getX() + fraction * 150);
	}

	@Override
	public void drawAnimation(Graphics g, float cameraX, float cameraY) {
		g.setColor(Color.cyan);
		g.fillRect(xPos + cameraX, yPos + cameraY, 30, 30);
	}

	@Override
	public void addParams(Object[] params) {
		// no parameters used.
	}
}
