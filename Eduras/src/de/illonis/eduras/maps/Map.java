package de.illonis.eduras.maps;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.maps.persistence.InvalidDataException;
import de.illonis.eduras.maps.persistence.MapParser;
import de.illonis.eduras.math.Vector2D;

/**
 * A playable map for gaming.
 * 
 * @author illonis
 * 
 */
public abstract class Map {

	/**
	 * The date format used for date conversion.
	 */
	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private final String name;
	private final String author;
	private Date created;
	private final int width;
	private final int height;
	private final LinkedList<InitialObjectData> initialObjects;
	private final LinkedList<GameModeNumber> supportedGameModes;
	private final LinkedList<SpawnPosition> spawnPositions;

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
		supportedGameModes = new LinkedList<GameModeNumber>();
		spawnPositions = new LinkedList<SpawnPosition>();
		buildMap();
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
			e.printStackTrace();
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
	public final boolean contains(Vector2D point) {
		return getBounds().contains(point.toPoint());
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
	protected final void addSpawnArea(double x, double y, double w, double h,
			SpawnType spawnType) {
		addSpawnArea(new SpawnPosition(new Rectangle2D.Double(x, y, w, h),
				spawnType));
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
	protected final void addObject(ObjectType type, double xPos, double yPos) {
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
	protected final void loadFromFile(String mapFileName)
			throws InvalidDataException, IOException {
		Map map = MapParser.readMap(getClass().getResource(
				"data/" + mapFileName));
		initialObjects.addAll(map.getInitialObjects());
		spawnPositions.addAll(map.getSpawnAreas());
		supportedGameModes.addAll(map.getSupportedGameModes());
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
}