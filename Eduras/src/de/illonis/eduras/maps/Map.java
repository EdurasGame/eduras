package de.illonis.eduras.maps;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;

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

	private Collection<GameObject> initialObjects;
	private final Collection<SpawnPosition> spawnAreas;

	/**
	 * Creates a new map with the given name and size and empty spawnpoint- and
	 * objectlist.
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
		initialObjects = new LinkedList<GameObject>();
		spawnAreas = new LinkedList<SpawnPosition>();
	}

	/**
	 * Returns height of map.
	 * 
	 * @return height of map.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns width of map.
	 * 
	 * @return width of map.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns name of the map.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the objects the map contains initially.
	 * 
	 * @return The initial objects.
	 */
	public Collection<GameObject> getInitialObjects() {
		return initialObjects;
	}

	/**
	 * Returns bounds of map. A map's top left corner always has coordinates
	 * (0,0), so any negative coordinates are not in map area.
	 * 
	 * @return bounds of map as rectangle.
	 */
	public Rectangle getBounds() {
		return new Rectangle(0, 0, width, height);
	}

	/**
	 * Returns the possible spawnpoints.
	 * 
	 * @return A collection of spawnpoints.
	 */
	public Collection<SpawnPosition> getSpawnAreas() {
		return spawnAreas;
	}

	/**
	 * Checks whether given point is within map bounds.
	 * 
	 * @param point
	 *            point to test.
	 * @return true if point is on map.
	 */
	public boolean contains(Vector2D point) {
		return getBounds().contains(point.toPoint());
	}

	/**
	 * Sets the map's collection of initial Objects.
	 * 
	 * @param objects
	 */
	public void setInitialObjects(Collection<GameObject> objects) {
		this.initialObjects = objects;
	}
}