package de.illonis.eduras.mapeditor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.weapons.SniperWeapon;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;

/**
 * Holds all information about the currently edited map.
 * 
 * @author illonis
 * 
 */
public final class MapData {
	private static MapData instance;

	/**
	 * @return mapdata instance.
	 */
	public static MapData getInstance() {
		if (instance == null) {
			instance = new MapData();
		}
		return instance;
	}

	private MapData() {
		reset();
	}

	private String mapName;
	private String author;
	private int width;
	private int height;
	private DynamicPolygonObject placingObject;

	private Set<GameModeNumber> supportedGameModes;

	private List<GameObject> gameObjects;
	private List<NodeData> bases;
	private List<SpawnPosition> spawnPoints;

	/**
	 * Resets data to provide an empty default map.
	 */
	public void reset() {
		gameObjects = new LinkedList<GameObject>();
		SniperWeapon w = new SniperWeapon(null, null, 1);
		gameObjects.add(w);
		bases = new LinkedList<NodeData>();
		spawnPoints = new LinkedList<SpawnPosition>();
		supportedGameModes = new HashSet<GameModeNumber>();
		DynamicPolygonObject o = new DynamicPolygonObject(
				ObjectType.DYNAMIC_POLYGON_BLOCK, null, null, -1);
		Vector2f[] verts = new Vector2f[4];
		verts[0] = new Vector2f(5, 5);
		verts[1] = new Vector2f(15, 15);
		verts[2] = new Vector2f(50, 10);
		verts[3] = new Vector2f(50, 40);
		o.setPolygonVertices(verts);
		o.setXPosition(30);
		o.setYPosition(130);
		gameObjects.add(o);
		mapName = "unnamed Map";
		placingObject = null;
		author = "unknown";
		width = 500;
		height = 500;
	}

	public DynamicPolygonObject getPlacingObject() {
		return placingObject;
	}

	public void setPlacingObject(DynamicPolygonObject placingObject) {
		this.placingObject = placingObject;
	}

	/**
	 * Imports data from given map. This clears all previously existing data.
	 * 
	 * @param map
	 *            the map to import.
	 */
	public void importMap(Map map) {
		reset();
		width = map.getWidth();
		height = map.getHeight();
		author = map.getAuthor();
		mapName = map.getName();
		spawnPoints.addAll(map.getSpawnAreas());
		supportedGameModes.addAll(map.getSupportedGameModes());

		List<InitialObjectData> objects = new LinkedList<InitialObjectData>(
				map.getInitialObjects());
		for (InitialObjectData object : objects) {

		}
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Set<GameModeNumber> getSupportedGameModes() {
		return supportedGameModes;
	}

	public void setSupportedGameModes(List<GameModeNumber> supportedGameModes) {
		this.supportedGameModes = new HashSet<GameModeNumber>(
				supportedGameModes);
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public List<NodeData> getBases() {
		return bases;
	}

	public List<SpawnPosition> getSpawnPoints() {
		return spawnPoints;
	}

	public void addGameObject(GameObject o) {
		gameObjects.add(o);
	}

	public void addBase(NodeData node) {
		bases.add(node);
	}

	public void addSpawnPoint(SpawnPosition spawn) {
		spawnPoints.add(spawn);
	}

	public void remove(GameObject o) {
		gameObjects.remove(o);
	}

	public void remove(NodeData node) {
		bases.remove(node);
	}

	public void remove(SpawnPosition spawn) {
		spawnPoints.remove(spawn);
	}

}
