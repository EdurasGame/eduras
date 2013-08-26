package de.illonis.eduras.maps;

import java.awt.Rectangle;
import java.util.Collection;

import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.Vector2D;

/**
 * A playable map for gaming.
 * 
 * @author illonis
 * 
 */
public abstract class Map {

	private final String name;
	private final int width;
	private final int height;

	/**
	 * Creates a new map with the given name and size.
	 * 
	 * @param name
	 *            name of the map.
	 * @param width
	 *            width of the map.
	 * @param height
	 *            height of the map.
	 */
	public Map(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns height of map.
	 * 
	 * @return height of map.
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Returns width of map.
	 * 
	 * @return width of map.
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * Returns name of the map.
	 * 
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns bounds of map. A map's top left corner always has coordinates
	 * (0,0), so any negative coordinates are not in map area.
	 * 
	 * @return bounds of map as rectangle.
	 */
	public final Rectangle getBounds() {
		return new Rectangle(0, 0, width, height);
	}

	/**
	 * Checks whether given point is within map bounds.
	 * 
	 * @param point
	 *            point to test.
	 * @return true if point is on map.
	 */
	public final boolean contains(Vector2D point) {
		return getBounds().contains(point.toPoint());
	}

	/**
	 * Returns the possible spawnpoints.
	 * 
	 * @return A collection of all spawnpoints on the map.
	 */
	public abstract Collection<SpawnPosition> getSpawnAreas();

	/**
	 * Returns the objects the map contains initially.
	 * 
	 * @return The initial objects.
	 */
	public abstract Collection<GameObject> getInitialObjects();

	/**
	 * Lists all gamemodes supported by this map.
	 * 
	 * @return a list of supported gamemodes.
	 */
	public abstract Collection<GameModeNumber> getSupportedGameModes();
}