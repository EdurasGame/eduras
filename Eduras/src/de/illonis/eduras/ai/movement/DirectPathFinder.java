package de.illonis.eduras.ai.movement;

import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;

/**
 * A pathfinder that ignores other objects and calculates a direct way.<br>
 * This is intended for non-colliding flying objects.
 * 
 * @author illonis
 * 
 */
public class DirectPathFinder implements PathFinder {

	private Vector2df target = new Vector2df(0, 0), location = new Vector2df(0,
			0);
	private Vector2df[] waypoints;
	private Vector2df direction;

	private void recalculateDirection() {
		direction = new Vector2df(target);
		direction.sub(location);
	}

	@Override
	public void setTarget(Vector2df target) {
		this.target = target;
		waypoints = new Vector2df[] { target };
		recalculateDirection();
	}

	@Override
	public void setLocation(Vector2df location) {
		this.location = location;
		recalculateDirection();
	}

	@Override
	public boolean hasReachedTarget() {
		return getDistance() < S.ai_target_reached_distance;
	}

	@Override
	public Vector2df[] getWayPoints() {
		return waypoints;
	}

	@Override
	public Vector2df getMovingDirection() {
		return direction;
	}

	@Override
	public double getDistance() {
		return location.distance(target);
	}

}
