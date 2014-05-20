package de.illonis.eduras.maps;

import java.util.Random;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.math.Vector2df;

/**
 * Specifies a rectangular spawning area for one or multiple units.
 * 
 * @author illonis
 * 
 */
public class SpawnPosition {

	private final static Logger L = EduLog.getLoggerFor(SpawnPosition.class
			.getName());

	private final static Random RANDOM = new Random();

	private final Rectangle area;
	private final SpawnType teaming;

	/**
	 * Indicates in which role this spawnposition can be used. This let the map
	 * use multiple spawnpositions that guarantee that no enemies spawn in the
	 * same area. A player team that spawns in positions of type "A" can never
	 * spawn in a position of type "B" and vice versa.
	 * 
	 * @author illonis
	 * 
	 */
	public enum SpawnType {
		/**
		 * Indicates that only players of one team can spawn here.
		 */
		TEAM_A, /**
		 * Indicates that only players of a team other than
		 * {@link SpawnType#TEAM_A} can spawn here.
		 */
		TEAM_B, /**
		 * Indicates that every player can spawn here.
		 */
		ANY, /**
		 * Indicates that only one player can spawn here.
		 */
		SINGLE
	}

	/**
	 * Creates a new spawning position.
	 * 
	 * @param area
	 *            the area that should be marked as spawning area. Make sure it
	 *            is big enough because the whole spawning unit must fit into
	 *            it.
	 * @param teaming
	 *            the type of spawning.
	 */
	public SpawnPosition(Rectangle area, SpawnType teaming) {
		this.area = area;
		this.teaming = teaming;
	}

	/**
	 * Calculates a random position in the spawningArea. This method guarantees
	 * that the spawning object will be in the spawning area completely. This
	 * prevents being stucked in nearby objects. However, it does not care for
	 * other players currently in this area.
	 * 
	 * @param spawningShape
	 *            the shape of the object that will spawn here. This is used to
	 *            calculate offset so object remains in area.
	 * 
	 * @return A vector pointing at a random position in spawning area.
	 * 
	 * @author illonis
	 */
	public Vector2df getAPoint(Shape spawningShape) {
		float maxX = area.getX() + area.getWidth() - spawningShape.getWidth();
		float maxY = area.getY() + area.getHeight() - spawningShape.getHeight();
		float minX = area.getX();
		float minY = area.getY();

		float spawnX = RANDOM.nextFloat() * (maxX - minX) + minX;
		float spawnY = RANDOM.nextFloat() * (maxY - minY) + minY;

		if (spawnX < 0 || spawnY < 0) {
			L.warning("Spawning object is too big for this spawning Point!"
					+ "Size of Object: "

					+ " Size of Area: " + area.getWidth() + ","
					+ area.getHeight());
			spawnX = area.getX();
			spawnY = area.getY();
		}
		return new Vector2df(spawnX, spawnY);
	}

	/**
	 * @return the spawn configuration for this position.
	 * 
	 * @author illonis
	 */
	public SpawnType getTeaming() {
		return teaming;
	}

	/**
	 * Returns the spawn area as a rectangular area.
	 * 
	 * @return spawn area
	 */
	public Rectangle getArea() {
		return area;
	}
}