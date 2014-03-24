package de.illonis.eduras.gameclient.gui.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.jdesktop.core.animation.timing.Animator;

import de.illonis.eduras.math.Vector2df;

/**
 * Animation displayed when rocket missile explodes.
 * 
 * @author illonis
 * 
 */
public class RocketSplashAnimation extends Animation {

	private double radius;
	private int x, y, dim;
	private final static BasicStroke STROKE = new BasicStroke(6f);

	RocketSplashAnimation(Vector2df position) {
		super("Rocket splash", 300, position);
	}

	@Override
	public void begin(Animator source) {
		x = (int) position.getX();
		y = (int) position.getY();
		dim = 1;
		super.begin(source);
	}

	@Override
	public void timingEvent(Animator source, double fraction) {
		double currentRadius = radius * fraction;
		x = (int) (position.getX() - currentRadius);
		y = (int) (position.getY() - currentRadius);
		dim = (int) (2 * currentRadius);
	}

	@Override
	protected void drawAnimation(Graphics2D g2d, int cameraX, int cameraY) {
		g2d.setStroke(STROKE);
		g2d.setColor(Color.RED);
		g2d.drawOval(x + cameraX, y + cameraY, dim, dim);
	}

	@Override
	public void addParams(Object[] params) {
		if (params.length > 0)
			radius = (double) params[0] - 5;
		else
			radius = 15;
	}
}
