package de.illonis.eduras.maps;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;

/**
 * This is a very simple map containing 4 example weapons and a block.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SimpleMap extends Map {

	/**
	 * Creates a new simple map.
	 */
	public SimpleMap() {
		super("simple", "Eduras Team", 500, 500);
		setCreated("2012-11-10");
	}

	@Override
	protected void buildMap() {
		addSupportedGameMode(GameModeNumber.DEATHMATCH);
		addSpawnArea(0, 0, getWidth(), getHeight(), SpawnType.ANY);

		addObject(ObjectType.ITEM_WEAPON_SIMPLE, getWidth() * .75f,
				getHeight() * .75f);
		addObject(ObjectType.ITEM_WEAPON_SNIPER, getWidth() * .5f,
				getHeight() * .75f);
		addObject(ObjectType.ITEM_WEAPON_SIMPLE, getWidth() * .75f,
				getHeight() * .25f);
		addObject(ObjectType.ITEM_WEAPON_SIMPLE, getWidth() * .25f,
				getHeight() * .75f);
		addObject(ObjectType.BUILDING, getWidth() * .25f, getHeight() * .25f);
		addObject(ObjectType.BIGBLOCK, getWidth() / 2, getHeight() / 2);
	}
}
