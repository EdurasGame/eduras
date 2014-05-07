package de.illonis.eduras.maps;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.persistence.InvalidDataException;
import de.illonis.eduras.maps.persistence.MapParser;

/**
 * An EduraMap is a map that contains nodes which are used for Edura! game mode.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EduraMap extends LoadedMap {

	private final static Logger L = EduLog.getLoggerFor(EduraMap.class
			.getName());

	private Collection<NodeData> nodes;

	/**
	 * Create an EduraMap.
	 * 
	 * @param name
	 *            The map's name.
	 * @param author
	 *            The map's author.
	 * @param width
	 *            The map's width.
	 * @param height
	 *            The map's height.
	 * @param created
	 *            The date of creation.
	 * @param spawnPositions
	 *            The spawn positions of the map.
	 * @param initialObjects
	 *            The objects on the map when it's created.
	 * @param supportedGameModes
	 * @param nodes
	 *            The map's nodes
	 */
	public EduraMap(String name, String author, int width, int height,
			Date created, Collection<SpawnPosition> spawnPositions,
			Collection<InitialObjectData> initialObjects,
			Collection<GameModeNumber> supportedGameModes,
			Collection<NodeData> nodes) {
		super(name, author, width, height, created, spawnPositions,
				initialObjects, supportedGameModes);

		if (nodes != null)
			this.nodes = nodes;
	}

	@Override
	protected void loadFromFile(String mapFileName)
			throws InvalidDataException, IOException {
		super.loadFromFile(mapFileName);

		EduraMap map = (EduraMap) MapParser.readMap(getClass().getResource(
				"data/" + mapFileName));

		if (nodes == null) {
			nodes = new LinkedList<NodeData>();
		}
		nodes.addAll(map.getNodes());
	}

	/**
	 * Returns the map's nodes.
	 * 
	 * @return nodes
	 */
	public Collection<NodeData> getNodes() {
		return nodes;
	}
}
