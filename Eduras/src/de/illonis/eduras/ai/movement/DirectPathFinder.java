package de.illonis.eduras.ai.movement;

import de.illonis.eduras.math.Vector2D;

/**
 * A pathfinder that ignores other objects and calculates a direct way.<br>
 * This is intended for non-colliding flying objects.
 * 
 * @author illonis
 * 
 */
public class DirectPathFinder implements PathFinder {

	private Vector2D target = new Vector2D(0, 0),
			location = new Vector2D(0, 0);
	private Vector2D[] waypoints;
	private Vector2D direction;

	private void recalculateDirection() {
		direction = new Vector2D(target);
		direction.subtract(location);
	}

	@Override
	public void setTarget(Vector2D target) {
		this.target = target;
		waypoints = new Vector2D[] { target };
		recalculateDirection();
	}

	@Override
	public void setLocation(Vector2D location) {
		this.location = location;
		recalculateDirection();
	}

	@Override
	public boolean hasReachedTarget() {
		return getDistance() < 10;
	}

	@Override
	public Vector2D[] getWayPoints() {
		return waypoints;
	}

	@Override
	public Vector2D getMovingDirection() {
		return direction;
	}

	@Override
	public double getDistance() {
		return location.calculateDistance(target);
	}

}
