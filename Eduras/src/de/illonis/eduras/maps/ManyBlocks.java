package de.illonis.eduras.maps;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.logger.EduLog;
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
	public Collection<GameObject> getInitialObjects() {
		LinkedList<GameObject> initialObjects = new LinkedList<GameObject>();

		int bigBlockWidth = 20;
		int bigBlockHeight = 20;

		final int space = 5;

		for (int i = 0; i < getWidth() / (space * bigBlockWidth); i++) {
			for (int j = 0; j < getHeight() / (space * bigBlockHeight); j++) {
				try {
					BigBlock bigBlock = new BigBlock(null, space * i
							* bigBlockWidth, space * j * bigBlockHeight, -1);
					initialObjects.add(bigBlock);
				} catch (ShapeVerticesNotApplicableException e) {
					EduLog.passException(e);
				}
			}
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
