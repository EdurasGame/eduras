package de.illonis.eduras.ai.movement;

import de.illonis.eduras.math.Vector2D;

/**
 * Generates a pathfinder for a specific {@link MotionType}.
 * 
 * @author illonis
 * 
 */
public class PathFinderFactory {

	/**
	 * Returns a pathfinder that uses given motion type.
	 * 
	 * @param type
	 *            the motion type of the unit targeted.
	 * @return a pathfinder using this {@link MotionType}.
	 */
	static PathFinder getFinderFor(MotionType type) {
		return new PathFinder() {
			private Vector2D target = new Vector2D(0, 0),
					location = new Vector2D(0, 0);

			@Override
			public void setTarget(Vector2D target) {
				this.target = target;

			}

			@Override
			public void setLocation(Vector2D location) {
				this.location = location;
			}

			@Override
			public boolean hasReachedTarget() {
				return target.calculateDistance(location) < 1;
			}

			@Override
			public Vector2D[] getWayPoints() {
				return new Vector2D[] { target };
			}

			@Override
			public Vector2D getMovingDirection() {
				Vector2D v = new Vector2D(target);
				v.subtract(location);
				return v;
			}

			@Override
			public double getDistance() {
				return location.calculateDistance(target);
			}
		};
	}
}
