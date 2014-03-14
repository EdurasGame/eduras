package de.illonis.eduras.maps;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;

/**
 * This is a sample map containing
 * {@link de.illonis.eduras.gameobjects.BigBlock BigBlocks} in a square-like
 * fashion.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ManyBlocks extends Map {

	/**
	 * Create a new instance of the map.
	 */
	public ManyBlocks() {
		super("manyblocks", "Florian Mai", 500, 500);
		setCreated("2013-05-12");
	}

	@Override
	protected void buildMap() {
		addSupportedGameMode(GameModeNumber.DEATHMATCH);
		addSpawnArea(0, 0, getWidth(), getHeight(), SpawnType.ANY);

		int bigBlockWidth = 20;
		int bigBlockHeight = 20;

		final int space = 5;

		for (int i = 0; i < getWidth() / (space * bigBlockWidth); i++) {
			for (int j = 0; j < getHeight() / (space * bigBlockHeight); j++) {
				addObject(ObjectType.BIGBLOCK, space * i * bigBlockWidth, space
						* j * bigBlockHeight);
			}
		}
	}

}
