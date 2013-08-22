package de.illonis.eduras.maps;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;

/**
 * Specifies a rectangular spawning area for one or multiple units.
 * 
 * @author illonis
 * 
 */
public class SpawnPosition {

	private final static Random RANDOM = new Random();

	private final Rectangle2D.Double area;
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
		DEATHMATCH, /**
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
	public SpawnPosition(Rectangle2D.Double area, SpawnType teaming) {
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
	public Vector2D getAPoint(ObjectShape spawningShape) {
		Rectangle2D.Double shapeRect = spawningShape.getBoundingBox();
		double maxX = area.x + area.width - shapeRect.width;
		double maxY = area.y + area.height - shapeRect.height;
		double minX = area.x;
		double minY = area.y;

		double spawnX = RANDOM.nextDouble() * (maxX - minX) + minX;
		double spawnY = RANDOM.nextDouble() * (maxY - minY) + minY;

		if (spawnX < 0 || spawnY < 0) {
			EduLog.warning("Spawning object is too big for this spawning Point!"
					+ "Size of Object: "
					+ spawningShape.getBoundingBox().getBounds()
					+ " Size of Area: " + area.getBounds());
			spawnX = area.x;
			spawnY = area.y;
		}
		return new Vector2D(spawnX, spawnY);
	}

	/**
	 * @return the spawn configuration for this position.
	 * 
	 * @author illonis
	 */
	public SpawnType getTeaming() {
		return teaming;
	}
}