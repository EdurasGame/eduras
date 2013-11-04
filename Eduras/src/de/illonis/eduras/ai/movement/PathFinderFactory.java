package de.illonis.eduras.ai.movement;

/**
 * Generates a pathfinder for a specific {@link MotionType}.
 * 
 * @author illonis
 * 
 */
public final class PathFinderFactory {

	/**
	 * Returns a pathfinder that uses given motion type.
	 * 
	 * @param type
	 *            the motion type of the unit targeted.
	 * @return a pathfinder using this {@link MotionType}.
	 */
	static PathFinder getFinderFor(MotionType type) {
		switch (type) {
		case RANDOM:
			return new RandomPathFinder();
		case FLYING:
		default:
			return new DirectPathFinder();
		}
	}
}
