package de.illonis.eduras.maps;

import java.util.LinkedList;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.logger.EduLog;

public class ManyBlocks extends Map {

	public ManyBlocks() {
		super("manyblocks", 500, 500);

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

		setInitialObjects(initialObjects);
	}

}
