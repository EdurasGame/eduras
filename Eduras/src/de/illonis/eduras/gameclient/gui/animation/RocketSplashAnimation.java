package de.illonis.eduras.gameclient.gui.animation;

import org.jdesktop.core.animation.timing.Animator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.math.Vector2df;

/**
 * Animation displayed when rocket missile explodes.
 * 
 * @author illonis
 * 
 */
public class RocketSplashAnimation extends Animation {

	private double radius;
	private float x, y, dim;
	private final float STROKE_WIDTH = 6f;

	RocketSplashAnimation(Vector2df position) {
		super("Rocket splash", 300, position);
	}

	@Override
	public void begin(Animator source) {
		x = position.getX();
		y = position.getY();
		dim = 1;
		super.begin(source);
	}

	@Override
	public void timingEvent(Animator source, double fraction) {
		double currentRadius = radius * fraction;
		x = (float) (position.getX() - currentRadius);
		y = (float) (position.getY() - currentRadius);
		dim = (float) (2 * currentRadius);
	}

	@Override
	protected void drawAnimation(Graphics g, float cameraX, float cameraY) {
		g.setLineWidth(STROKE_WIDTH);
		g.setColor(Color.red);
		g.drawOval(x + cameraX, y + cameraY, dim, dim);
	}

	@Override
	public void addParams(Object[] params) {
		if (params.length > 0)
			radius = (double) params[0] - 5;
		else
			radius = 15;
	}
}
