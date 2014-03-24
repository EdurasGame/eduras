package de.illonis.eduras.ai.movement;

import de.illonis.eduras.math.Vector2df;

/**
 * Provides pathfinding methods that allow a unit to receive its current moving
 * direction and remaining distance. This interface is intended to be fully
 * independend on concrete units. To learn more details, look at the
 * documentation on interaction modes.
 * 
 * @author illonis
 * 
 */
public interface PathFinder {

	/**
	 * Retrieves the required waypoints to reach target.
	 * 
	 * @return a list of waypoints that must be passed to reach the target.
	 *         Should return an empty array if {@link #hasReachedTarget()}
	 *         returns true.
	 */
	Vector2df[] getWayPoints();

	/**
	 * Sets location of the unit to given position.
	 * 
	 * @param location
	 *            unit's current position.
	 */
	void setLocation(Vector2df location);

	/**
	 * Sets the moving target.
	 * 
	 * @param target
	 *            target position.
	 */
	void setTarget(Vector2df target);

	/**
	 * Retrieves the current moving direction considering required waypoints,
	 * detours and obstacles. The moving direction directly heads to the next
	 * waypoint.
	 * 
	 * @return the current moving direction.
	 */
	Vector2df getMovingDirection();

	/**
	 * Determines whether unit has reached its goal or should move on.
	 * 
	 * @return true if unit has reached target, false otherwise. If no target is
	 *         specified, false is returned.
	 */
	boolean hasReachedTarget();

	/**
	 * Retrieves the distance to the currently active target. This considers the
	 * waypoints.
	 * 
	 * @return total distance.
	 */
	double getDistance();

}
