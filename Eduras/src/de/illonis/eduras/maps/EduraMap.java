package de.illonis.eduras.maps;

import java.util.Collection;
import java.util.Date;

import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;

/**
 * An EduraMap is a map that contains nodes which are used for Edura! game mode.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EduraMap extends LoadedMap {

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
	 *            the supported game modes.
	 * @param nodes
	 *            The map's nodes
	 */
	public EduraMap(String name, String author, int width, int height,
			Date created, Collection<SpawnPosition> spawnPositions,
			Collection<InitialObjectData> initialObjects,
			Collection<GameModeNumber> supportedGameModes,
			Collection<NodeData> nodes) {
		super(name, author, width, height, created, spawnPositions,
				initialObjects, supportedGameModes, nodes);
	}
}
