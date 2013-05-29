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

	public Building(GameInformation game, int id) {
		super(game, id);
		setObjectType(ObjectType.BUILDING);
		setShape(new House());
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// TODO Auto-generated method stub

	}

}
