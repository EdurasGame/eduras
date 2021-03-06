package de.illonis.eduras.maps;

import java.util.Random;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ReferencedEntity;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.math.Vector2df;

/**
 * Specifies a rectangular spawning area for one or multiple units.
 * 
 * @author illonis
 * 
 */
public class SpawnPosition extends ReferencedEntity implements EditorPlaceable {

	private final static Logger L = EduLog.getLoggerFor(SpawnPosition.class
			.getName());

	private final static Random RANDOM = new Random();

	private final Rectangle area;
	private SpawnType teaming;

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
		setRefName("");
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
	 */
	public SpawnType getTeaming() {
		return teaming;
	}

	/**
	 * Sets the spawn configuration from editor.
	 * 
	 * @param teaming
	 *            new configuration.
	 */
	public void setTeaming(SpawnType teaming) {
		this.teaming = teaming;
	}

	/**
	 * Returns the spawn area as a rectangular area.
	 * 
	 * @return spawn area
	 */
	public Rectangle getArea() {
		return area;
	}

	@Override
	public float getXPosition() {
		return area.getX();
	}

	@Override
	public float getYPosition() {
		return area.getY();
	}

	@Override
	public void setPosition(float x, float y) {
		area.setLocation(x, y);
	}

	@Override
	public void setXPosition(float newX) {
		area.setX(newX);
	}

	@Override
	public void setYPosition(float newY) {
		area.setY(newY);
	}

	@Override
	public float getHeight() {
		return area.getHeight();
	}

	@Override
	public float getWidth() {
		return area.getWidth();
	}

	@Override
	public void setWidth(float width) {
		area.setWidth(width);
	}

	@Override
	public void setHeight(float height) {
		area.setHeight(height);
	}
}