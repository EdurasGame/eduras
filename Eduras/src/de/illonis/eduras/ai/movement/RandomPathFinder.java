package de.illonis.eduras.ai.movement;

import java.util.Random;

import de.illonis.eduras.math.Vector2D;

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

	private Vector2D[] waypoints;
	private final static Random r = new Random();

	RandomPathFinder() {
		waypoints = new Vector2D[10];
		for (int i = 0; i < waypoints.length; i++) {
			int xneg = (r.nextBoolean()) ? 1 : -1;
			int yneg = (r.nextBoolean()) ? 1 : -1;

			double x = r.nextDouble();
			double y = r.nextDouble();
			waypoints[i] = new Vector2D(x * xneg, y * yneg);
		}
	}

	@Override
	public Vector2D[] getWayPoints() {
		return waypoints;
	}

	@Override
	public void setLocation(Vector2D location) {
	}

	@Override
	public void setTarget(Vector2D target) {
	}

	@Override
	public Vector2D getMovingDirection() {
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
