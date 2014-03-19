package de.illonis.eduras.ai.movement;

import java.util.Random;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.math.Vector2df;

/**
 * A random path finder that let a unit move randomly without taking the target
 * into account. Once a unit is moved with this pathfinder (
 * {@link MotionType#RANDOM}), it will move around randomly and never stop until
 * its stopped manually.<br>
 * Note that this pathfinder somehow abuses waypoint-mechanism and should not be
 * a pattern to use.
 * 
 * @author illonis
 * 
 */
public final class RandomPathFinder implements PathFinder {

	private long lastChange = 0;
	private int lastIndex = 0;

	private Vector2f[] waypoints;
	private final static Random r = new Random();

	RandomPathFinder() {
		waypoints = new Vector2f[10];
		for (int i = 0; i < waypoints.length; i++) {
			int xneg = (r.nextBoolean()) ? 1 : -1;
			int yneg = (r.nextBoolean()) ? 1 : -1;

			float x = r.nextFloat();
			float y = r.nextFloat();
			waypoints[i] = new Vector2df(x * xneg, y * yneg);
		}
	}

	@Override
	public Vector2f[] getWayPoints() {
		return waypoints;
	}

	@Override
	public void setLocation(Vector2f location) {
	}

	@Override
	public void setTarget(Vector2f target) {
	}

	@Override
	public Vector2f getMovingDirection() {
		if (System.currentTimeMillis() - lastChange > 3000) {
			lastChange = System.currentTimeMillis();
			lastIndex = r.nextInt(10);
		}
		return waypoints[lastIndex];
	}

	@Override
	public boolean hasReachedTarget() {
		return false;
	}

	@Override
	public double getDistance() {
		return Double.MAX_VALUE;
	}
}
