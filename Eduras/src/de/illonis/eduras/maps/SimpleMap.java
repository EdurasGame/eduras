package de.illonis.eduras.maps;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.gameobjects.Building;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.weapons.ExampleWeapon;
import de.illonis.eduras.items.weapons.SniperWeapon;
import de.illonis.eduras.logger.EduLog;
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
	public Collection<GameObject> getInitialObjects() {
		LinkedList<GameObject> initialObjects = new LinkedList<GameObject>();

		ExampleWeapon weap1 = new ExampleWeapon(null, -1);
		weap1.setPosition(getWidth() * 0.75, getHeight() * 0.75);
		initialObjects.add(weap1);

		SniperWeapon tw = new SniperWeapon(null, -1);
		tw.setPosition(getWidth() * 0.5, getHeight() * 0.75);
		initialObjects.add(tw);

		ExampleWeapon weap2 = new ExampleWeapon(null, -1);
		weap2.setPosition(getWidth() * 0.75, getHeight() * 0.25);
		initialObjects.add(weap2);

		ExampleWeapon weap3 = new ExampleWeapon(null, -1);
		weap3.setPosition(getWidth() * 0.25, getHeight() * 0.75);
		initialObjects.add(weap3);

		ExampleWeapon weap4 = new ExampleWeapon(null, -1);
		weap4.setPosition(getWidth() * 0.25, getHeight() * 0.25);
		initialObjects.add(weap4);

		Building b = new Building(null, -1);
		b.setPosition(80, 80);
		initialObjects.add(b);

		try {
			BigBlock block = new BigBlock(null, getWidth() / 2,
					getHeight() / 2, -1);
			initialObjects.add(block);
		} catch (ShapeVerticesNotApplicableException e) {
			// choosing the right values above, this should never happen
			EduLog.passException(e);
		}

		return initialObjects;
	}

	@Override
	public Collection<SpawnPosition> getSpawnAreas() {
		SpawnPosition p = new SpawnPosition(new Rectangle2D.Double(0, 0,
				getWidth(), getHeight()), SpawnType.ANY);
		LinkedList<SpawnPosition> positions = new LinkedList<SpawnPosition>();
		positions.add(p);
		return positions;
	}

	@Override
	public Collection<GameModeNumber> getSupportedGameModes() {
		LinkedList<GameModeNumber> modes = new LinkedList<GameModeNumber>();
		modes.add(GameModeNumber.DEATHMATCH);
		return modes;
	}
}
