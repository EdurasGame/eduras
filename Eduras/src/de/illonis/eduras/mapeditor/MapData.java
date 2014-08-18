package de.illonis.eduras.mapeditor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.illonis.eduras.FactoryException;
import de.illonis.eduras.ObjectCreator;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;
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
		supportedGameModes.add(GameModeNumber.DEATHMATCH);
	}

	private String mapName;
	private String author;
	private int width;
	private int height;
	private DynamicPolygonObject placingObject;
	private boolean showNodeConnections;

	private Set<GameModeNumber> supportedGameModes;

	private List<GameObject> gameObjects;
	private List<NodeData> bases;
	private List<SpawnPosition> spawnPoints;

	/**
	 * Resets data to provide an empty default map.
	 */
	public void reset() {
		gameObjects = new LinkedList<GameObject>();
		bases = new LinkedList<NodeData>();
		spawnPoints = new LinkedList<SpawnPosition>();
		supportedGameModes = new HashSet<GameModeNumber>();
		mapName = "unnamed Map";
		placingObject = null;
		author = "unknown";
		width = 500;
		height = 500;
		showNodeConnections = false;
	}

	public DynamicPolygonObject getPlacingObject() {
		return placingObject;
	}

	public void setShowNodeConnections(boolean showNodeConnections) {
		this.showNodeConnections = showNodeConnections;
	}

	public boolean isShowNodeConnections() {
		return showNodeConnections;
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
		placingObject = null;
		width = map.getWidth();
		height = map.getHeight();
		author = map.getAuthor();
		mapName = map.getName();
		LinkedList<InitialObjectData> portalData = new LinkedList<InitialObjectData>();

		spawnPoints.addAll(map.getSpawnAreas());
		supportedGameModes.addAll(map.getSupportedGameModes());
		bases.addAll(map.getNodes());
		List<InitialObjectData> objects = new LinkedList<InitialObjectData>(
				map.getInitialObjects());
		for (InitialObjectData object : objects) {
			try {
				if (object.getType() == ObjectType.MAPBOUNDS)
					continue;
				GameObject o = ObjectCreator.createObject(object.getType(),
						null, null);
				o.setRefName(object.getRefName());
				o.setPosition(object.getX(), object.getY());
				if (object.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK) {
					DynamicPolygonObject dyno = (DynamicPolygonObject) o;
					dyno.setPolygonVertices(object.getPolygonVector2dfs());
					dyno.setColor(object.getColor());
				}
				gameObjects.add(o);
				if (object.getType() == ObjectType.PORTAL) {
					portalData.add(object);
				}
			} catch (FactoryException | ShapeVerticesNotApplicableException e) {
			}
		}
		for (int i = 0; i < portalData.size(); i++) {
			InitialObjectData portal = portalData.get(i);
			Portal portalOne;
			try {
				System.out.println(portal.getRefName());
				portalOne = (Portal) findByRef(portal.getRefName());
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
				continue;
			}

			Portal portalTwo;
			try {
				portalTwo = (Portal) findByRef(portal
						.getReference(Portal.OTHER_PORTAL_REFERENCE));
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
				continue;
			}
			portalOne.setPartnerPortal(portalTwo);
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

	public GameObject findByRef(String name) throws ObjectNotFoundException {
		for (GameObject o : gameObjects) {
			if (o.getRefName().equals(name))
				return o;
		}
		throw new ObjectNotFoundException(-1);
	}

}
