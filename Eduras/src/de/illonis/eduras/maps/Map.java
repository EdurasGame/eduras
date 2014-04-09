package de.illonis.eduras.maps;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.NoSuchMapException;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.maps.persistence.InvalidDataException;
import de.illonis.eduras.maps.persistence.MapParser;
import de.illonis.eduras.math.Vector2df;

/**
 * A playable map for gaming.
 * 
 * @author illonis
 * 
 */
public abstract class Map {

	private final static Logger L = EduLog.getLoggerFor(Map.class.getName());

	/**
	 * The date format used for date conversion.
	 */
	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private final String name;
	private final String author;
	private Date created;
	private int width;
	private int height;
	protected final LinkedList<InitialObjectData> initialObjects;
	protected final LinkedList<GameModeNumber> supportedGameModes;
	protected final LinkedList<SpawnPosition> spawnPositions;
	private GameObject boundsObject;

	/**
	 * Creates a new map with the given name and size.
	 * 
	 * @param name
	 *            name of the map.
	 * @param author
	 *            name of the author of the map.
	 * @param width
	 *            width of the map.
	 * @param height
	 *            height of the map.
	 */
	public Map(String name, String author, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.author = author;
		created = new Date();
		initialObjects = new LinkedList<InitialObjectData>();
		addBoundsObject();
		supportedGameModes = new LinkedList<GameModeNumber>();
		spawnPositions = new LinkedList<SpawnPosition>();
		buildMap();
	}

	private void addBoundsObject() {
		Vector2df[] mapBoundsShape = new Vector2df[4];
		mapBoundsShape[0] = new Vector2df(0, 0);
		mapBoundsShape[1] = new Vector2df(width, 0);
		mapBoundsShape[2] = new Vector2df(width, height);
		mapBoundsShape[3] = new Vector2df(0, height);
		InitialObjectData boundsData = new InitialObjectData(
				ObjectType.MAPBOUNDS, 0, 0, mapBoundsShape);
		addObject(boundsData);

		boundsObject = null;
	}

	/**
	 * Creates a new map with the given name and size.
	 * 
	 * @param name
	 *            name of the map.
	 * @param author
	 *            name of the author of the map.
	 * @param width
	 *            width of the map.
	 * @param height
	 *            height of the map.
	 * @param created
	 *            the date of creation.
	 */
	public Map(String name, String author, int width, int height, Date created) {
		this(name, author, width, height);
		setCreated(created);
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
	 * Sets the date of map creation.
	 * 
	 * @param created
	 *            date of map creation.
	 */
	protected void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * Sets the date of map creation.
	 * 
	 * @param created
	 *            date of map creation as String
	 * @see #DATE_FORMAT
	 */
	protected void setCreated(String created) {
		try {
			setCreated(DATE_FORMAT.parse(created));
		} catch (ParseException e) {
			L.log(Level.SEVERE, "Cannot parse the created-date string.", e);
		}
	}

	/**
	 * Returns the author of the map.
	 * 
	 * @return author of map.
	 */
	public final String getAuthor() {
		return author;
	}

	/**
	 * @return date of creation.
	 */
	public final Date getCreated() {
		return created;
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
	public final boolean contains(Vector2df point) {
		return getBounds().contains(point.x, point.y);
	}

	/**
	 * Adds a new Spawn-position to this map.
	 * 
	 * @param position
	 *            the new spawnposition.
	 * 
	 * @see #addSpawnArea(double, double, double, double, SpawnType)
	 */
	protected final void addSpawnArea(SpawnPosition position) {
		spawnPositions.add(position);
	}

	/**
	 * Adds a new spawn-position to this map.
	 * 
	 * @param x
	 *            the x position of this area.
	 * @param y
	 *            the y position of this area.
	 * @param w
	 *            the width of this area.
	 * @param h
	 *            the height of this area.
	 * @param spawnType
	 *            the spawn type.
	 * @see #addSpawnArea(SpawnPosition)
	 */
	protected final void addSpawnArea(float x, float y, float w, float h,
			SpawnType spawnType) {
		addSpawnArea(new SpawnPosition(new Rectangle(x, y, w, h), spawnType));
	}

	/**
	 * Adds a new object to the map.
	 * 
	 * @param type
	 *            type of the object.
	 * @param xPos
	 *            x-position of that object.
	 * @param yPos
	 *            y-position of that object.
	 * @see #addObject(InitialObjectData)
	 */
	protected final void addObject(ObjectType type, float xPos, float yPos) {
		initialObjects.add(new InitialObjectData(type, xPos, yPos));
	}

	/**
	 * Adds a new object to the map.
	 * 
	 * @param objectData
	 *            the data of the new object.
	 * @see #addObject(ObjectType, double, double)
	 */
	protected final void addObject(InitialObjectData objectData) {
		initialObjects.add(objectData);
	}

	/**
	 * Adds a game mode to the list of supported game modes for this map.
	 * 
	 * @param mode
	 *            the supported game mode.
	 */
	protected final void addSupportedGameMode(GameModeNumber mode) {
		supportedGameModes.add(mode);
	}

	/**
	 * Loads mapdata from file instead of setting it manually using the
	 * add-methods. You can add more data manually after this if you want to.
	 * 
	 * @param mapFileName
	 *            the name of the mapfile. Must be located in the
	 *            "maps.data"-package.
	 * @throws InvalidDataException
	 *             if the map file contains invalid data.
	 * @throws IOException
	 *             if there was an error reading the map file.
	 * @see #addObject(ObjectType, double, double)
	 * @see #addSpawnArea(double, double, double, double, SpawnType)
	 * @see #addSupportedGameMode(GameModeNumber)
	 */
	protected void loadFromFile(String mapFileName)
			throws InvalidDataException, IOException {
		Map map = MapParser.readMap(getClass().getResource(
				"data/" + mapFileName));
		initialObjects.clear();
		initialObjects.addAll(map.getInitialObjects());
		spawnPositions.addAll(map.getSpawnAreas());
		supportedGameModes.addAll(map.getSupportedGameModes());

		width = map.getWidth();
		height = map.getHeight();
	}

	/**
	 * Returns the possible spawnpoints.
	 * 
	 * @return A collection of all spawnpoints on the map.
	 */
	public final Collection<SpawnPosition> getSpawnAreas() {
		return new LinkedList<SpawnPosition>(spawnPositions);
	}

	/**
	 * Returns the objects the map contains initially.
	 * 
	 * @return The initial objects.
	 */
	public final Collection<InitialObjectData> getInitialObjects() {
		return new LinkedList<InitialObjectData>(initialObjects);
	}

	/**
	 * Lists all gamemodes supported by this map.
	 * 
	 * @return a list of supported gamemodes.
	 */
	public final Collection<GameModeNumber> getSupportedGameModes() {
		return new LinkedList<GameModeNumber>(supportedGameModes);
	}

	/**
	 * Initializes and loads all map data.
	 */
	protected abstract void buildMap();

	/**
	 * Returns the bounds object if set already, null otherwise.
	 * 
	 * @return boundsobject
	 */
	public GameObject getBoundsObject() {
		return boundsObject;
	}

	/**
	 * Sets the bounds object of this map.
	 * 
	 * @param boundsObject
	 */
	public void setBoundsObject(GameObject boundsObject) {
		this.boundsObject = boundsObject;
	}

	/**
	 * Get the map that has the given name if it exists.
	 * 
	 * @param mapName
	 *            the name of the map
	 * @return The map of the given name.
	 * @throws NoSuchMapException
	 *             Thrown if the map of the given name cannot be found.
	 */
	public static Map getMapByName(String mapName) throws NoSuchMapException {
		switch (mapName.toLowerCase()) {
		case "funmap":
			return new FunMap();
		case "simple":
			return new SimpleMap();
		case "manyblocks":
			return new ManyBlocks();
		case "testmap":
			return new TestMap();
		case "eduratestmap":
			return new EduraTestMap();
		default:
			throw new NoSuchMapException(mapName);
		}
	}
}
