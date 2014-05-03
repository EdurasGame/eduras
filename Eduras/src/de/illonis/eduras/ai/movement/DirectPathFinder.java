package de.illonis.eduras.ai.movement;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.settings.S;

/**
 * A pathfinder that ignores other objects and calculates a direct way.<br>
 * This is intended for non-colliding flying objects.
 * 
 * @author illonis
 * 
 */
public class DirectPathFinder implements PathFinder {

	private Vector2f target = new Vector2f(0, 0),
			location = new Vector2f(0, 0);
	private Vector2f[] waypoints;
	private Vector2f direction;
	private float oldDistance = Float.MAX_VALUE;

	public DirectPathFinder() {
		oldDistance = Float.MAX_VALUE;
	}

	private void recalculateDirection() {
		direction = new Vector2f(target);
		direction.sub(location);
	}

	@Override
	public void setTarget(Vector2f target) {
		this.target = target;
		waypoints = new Vector2f[] { target };
		recalculateDirection();
	}

	@Override
	public void setLocation(Vector2f location) {
		this.location = location;
		recalculateDirection();
	}

	@Override
	public boolean hasReachedTarget() {
		float distance = getDistance();
		if (oldDistance < distance)
			return true;
		oldDistance = distance;
		return distance < S.ai_target_reached_distance;
	}

	@Override
	public Vector2f[] getWayPoints() {
		return waypoints;
	}

	@Override
	public Vector2f getMovingDirection() {
		return direction;
	}

	@Override
	public float getDistance() {
		return location.distance(target);
	}

}
