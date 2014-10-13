package de.illonis.eduras.mapeditor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.geom.Line;

import de.illonis.eduras.FactoryException;
import de.illonis.eduras.ObjectCreator;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.ReferencedEntity;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.gameobjects.TriggerArea;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.shapecreator.EditablePolygon;

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
	private boolean showPortalLinks;
	private EditablePolygon editShape;

	private Set<GameModeNumber> supportedGameModes;

	private List<GameObject> gameObjects;
	private List<NodeData> bases;
	private List<SpawnPosition> spawnPoints;
	private Line tempLineA;
	private Line tempLineB;
	private Line removedLine;
	private DynamicPolygonObject editObject;
	private TextureKey mapBackground;

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
		editShape = null;
		mapBackground = TextureKey.NONE;
	}

	public EditablePolygon getEditShape() {
		return editShape;
	}

	public void setEditShape(EditablePolygon editShape) {
		this.editShape = editShape;
	}

	public DynamicPolygonObject getPlacingObject() {
		return placingObject;
	}

	public void setShowPortalLinks(boolean showPortalLinks) {
		this.showPortalLinks = showPortalLinks;
	}

	public boolean isShowPortalLinks() {
		return showPortalLinks;
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
		mapBackground = map.getBackground();
		spawnPoints.addAll(map.getSpawnAreas());
		supportedGameModes.addAll(map.getSupportedGameModes());
		bases.addAll(map.getNodes());
		List<InitialObjectData> objects = new LinkedList<InitialObjectData>(
				map.getInitialObjects());
		int id = 1;
		for (InitialObjectData object : objects) {
			try {
				if (object.getType() == ObjectType.MAPBOUNDS)
					continue;
				GameObject o = ObjectCreator.createObject(object.getType(),
						null, null);
				o.setRefName(object.getRefName());
				o.setId(id++);
				o.setPosition(object.getX(), object.getY());
				if (object.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK) {
					DynamicPolygonObject dyno = (DynamicPolygonObject) o;
					dyno.setPolygonVertices(object.getPolygonVector2dfs());
					dyno.setColor(object.getColor());
				}
				o.setTexture(object.getTexture());
				if (o instanceof TriggerArea && object.getWidth() > 0) {
					o.setWidth(object.getWidth());
					o.setHeight(object.getHeight());
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
			GameObject ref = findObjectByRef(portal.getRefName());
			if (ref == null)
				continue;
			Portal portalOne = (Portal) ref;
			GameObject refTwo = findObjectByRef(portal
					.getReference(Portal.OTHER_PORTAL_REFERENCE));
			if (refTwo == null)
				continue;
			Portal portalTwo = (Portal) refTwo;
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

	public ReferencedEntity findByRef(String name) {
		GameObject o = findObjectByRef(name);
		if (o != null)
			return o;
		NodeData node = findBaseByRef(name);
		if (node != null)
			return node;
		SpawnPosition spawn = findSpawnByRef(name);
		return spawn;
	}

	public GameObject findObjectByRef(String name) {
		for (GameObject o : gameObjects) {
			if (o.getRefName().equals(name))
				return o;
		}
		return null;
	}

	public SpawnPosition findSpawnByRef(String name) {
		for (SpawnPosition o : spawnPoints) {
			if (o.getRefName().equals(name))
				return o;
		}
		return null;
	}

	public NodeData findBaseByRef(String name) {
		for (NodeData o : bases) {
			if (o.getRefName().equals(name))
				return o;
		}
		return null;
	}

	public void clearTempLines() {
		tempLineA = null;
		tempLineB = null;
	}

	public Line getTempLineA() {
		return tempLineA;
	}

	public Line getTempLineB() {
		return tempLineB;
	}

	public void setTempLineA(Line line) {
		tempLineB = line;
	}

	public void setTempLineB(Line l) {
		tempLineA = l;
	}

	public void setEditObject(DynamicPolygonObject o) {
		this.editObject = o;
	}

	public Line getRemovedLine() {
		return removedLine;
	}

	public void setRemovedLine(Line removedLine) {
		this.removedLine = removedLine;
	}

	public DynamicPolygonObject getEditObject() {
		return editObject;
	}

	public void setMapBackground(TextureKey mapBackground) {
		this.mapBackground = mapBackground;
	}

	public TextureKey getMapBackground() {
		return mapBackground;
	}
}