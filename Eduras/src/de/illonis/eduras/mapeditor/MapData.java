package de.illonis.eduras.mapeditor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.SpawnPosition;

/**
 * Holds all information about the currently edited map.
 * 
 * @author illonis
 * 
 */
public final class MapData {
	private static MapData instance;

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

	private Set<GameModeNumber> supportedGameModes;

	private List<GameObject> gameObjects;
	private List<Base> bases;
	private List<SpawnPosition> spawnPoints;

	public void reset() {
		gameObjects = new LinkedList<GameObject>();
		bases = new LinkedList<Base>();
		spawnPoints = new LinkedList<SpawnPosition>();
		supportedGameModes = new HashSet<GameModeNumber>();
		mapName = "unnamed Map";
		author = "unknown";
		width = 500;
		height = 500;
	}

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

	public void setSupportedGameModes(Set<GameModeNumber> supportedGameModes) {
		this.supportedGameModes = supportedGameModes;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(List<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}

	public List<Base> getBases() {
		return bases;
	}

	public void setBases(List<Base> bases) {
		this.bases = bases;
	}

	public List<SpawnPosition> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints(List<SpawnPosition> spawnPoints) {
		this.spawnPoints = spawnPoints;
	}

}
