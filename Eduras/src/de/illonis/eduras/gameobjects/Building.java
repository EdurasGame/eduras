package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.shapes.House;

/**
 * A simple building.
 * 
 * @author illonis
 * 
 */
public class Building extends GameObject {

	/**
	 * Creates a new building.
	 * 
	 * @param game
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public Building(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setObjectType(ObjectType.BUILDING);
		setShape(new House());
	}

	@Override
	public void onCollision(GameObject collidingObject) {
	}

}
