package de.illonis.eduras.maps;

import java.util.Collection;
import java.util.Date;

import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.persistence.MapParser;

/**
 * A map that was loaded by the {@link MapParser}.
 * 
 * @author illonis
 * 
 */
public class LoadedMap extends Map {

	/**
	 * Creates a new map with the given name and size and initial data.
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
	 * @param spawnPositions
	 *            the spawn positions of this map.
	 * @param initialObjects
	 *            the initial objects of this map.
	 * @param supportedGameModes
	 *            the supported game modes of this map.
	 * @param nodes
	 *            the nodes on this map.
	 * @param background
	 *            the background texture.
	 */
	public LoadedMap(String name, String author, int width, int height,
			Date created, Collection<SpawnPosition> spawnPositions,
			Collection<InitialObjectData> initialObjects,
			Collection<GameModeNumber> supportedGameModes,
			Collection<NodeData> nodes, TextureKey background) {
		super(author, width, height, created, name);
		this.background = background;

		for (GameModeNumber gameModeNumber : supportedGameModes)
			addSupportedGameMode(gameModeNumber);

		for (InitialObjectData objectData : initialObjects)
			addObject(objectData);

		for (SpawnPosition position : spawnPositions)
			addSpawnArea(position);

		for (NodeData node : nodes) {
			addNode(node);
		}
	}
}
